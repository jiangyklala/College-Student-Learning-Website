package com.jxm.yiti.service;

import com.github.pagehelper.PageHelper;
import com.jxm.yiti.domain.ChatRecordInfo;
import com.jxm.yiti.domain.ChatRecordInfoExample;
import com.jxm.yiti.domain.User;
import com.jxm.yiti.domain.UserExample;
import com.jxm.yiti.mapper.ChatRecordInfoMapper;
import com.jxm.yiti.mapper.UserMapper;
import com.jxm.yiti.mapper.cust.UserMapperCust;
import com.jxm.yiti.req.UserQueryReq;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.UserQueryResp;
import com.jxm.yiti.utils.*;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.params.SetParams;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.regex.Pattern;

import static com.jxm.yiti.utils.plainUtil.getTodayRemainSec;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserMapperCust userMapperCust;

    @Resource
    private ChatRecordInfoMapper chatRecordInfoMapper;

    @Resource
    private MailService mailService;

    public static JedisPool jedisPool;

    public static SnowFlakeIdWorker snowFlakeIdWorker;

    @Value("${my.addr}")
    private String myAddr;

    @Value("${my.redis.ip}")
    private String myRedisIP;

    public static final long initBalance = 20L;

    @PostConstruct
    public void init() {
        snowFlakeIdWorker = new SnowFlakeIdWorker(0, 0);            // 初始化雪花ID生成器
        jedisPool = new JedisPool(setJedisPoolConfig(), myRedisIP, 6379, 5000, "jiang", 1);
//        initJedisPool(jedisPool);
    }


    /**
     * 预热 jedisPool
     */
    public void initJedisPool(JedisPool jedisPool) {
        int minIdle = 100;
        List<Jedis> minIdleJedisList = new ArrayList<>(minIdle);
        for (int i = 0; i < minIdle; i++) {
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                minIdleJedisList.add(jedis);
                jedis.ping();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < minIdle; i++) {
            Jedis jedis = null;
            try {
                jedis = minIdleJedisList.get(i);
                jedis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 配置连接池
     */
    public JedisPoolConfig setJedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(200);                                    // 最大连接对象
        config.setJmxEnabled(true);
        config.setMaxIdle(100);                                     // 最大闲置对象
        config.setMinIdle(100);                                     // 最小闲置对象
        config.setTestOnBorrow(true);                               // 向资源池借用连接时是否做有效性检测
        config.setTestOnReturn(true);                               // 向资源池归还连接时是否做有效性检测
        config.setTestWhileIdle(true);                              // 是否在空闲资源检测时通过 ping 命令检测连接的有效性,无效连接将被销毁
        config.setTimeBetweenEvictionRuns(Duration.ofSeconds(5));   // 空闲资源的检测周期
        config.setMaxWait(Duration.ofSeconds(5));                   // 当资源池连接用尽后，调用者的最大等待时间
        config.setMinEvictableIdleTime(Duration.ofSeconds(10));
        config.setBlockWhenExhausted(true);                         // 当获取不到连接时应阻塞

        return config;
    }

    public AuthRequest getAuthRequest() {
        return new AuthGithubRequest(AuthConfig.builder()
                .clientId("7f64b8527592ce2d4d7c")
                .clientSecret("bc5d42bee691771f547351fec23f445676aeb10b")
                .redirectUri(myAddr + "/user/github/callback")
                .build());
    }

    /**
     * 设置用户登录凭证
     */
    public void setOnlyLoginCert(Long userID, HttpServletResponse response) throws IOException {
        String onlyLoginCert = Long.toString(snowFlakeIdWorker.nextId());           // 生成唯一登录凭证

        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", myAddr);

        Cookie cookieLoginCert = new Cookie("yiti_loginCert", onlyLoginCert);       // 增加本地唯一登录凭证 Cookie
        cookieLoginCert.setMaxAge(60 * 60 * 24 * 15);                               // 设置 cookie 有效期
        cookieLoginCert.setPath("/");
        response.addCookie(cookieLoginCert);

        Cookie cookieUserID = new Cookie("yiti_userID", Long.toString(userID));     // 增加本地自动登录账号信息 Cookie
        cookieUserID.setMaxAge(60 * 60 * 24 * 15);
        cookieUserID.setPath("/");
        response.addCookie(cookieUserID);

        try (Jedis jedis = jedisPool.getResource()) {                               // Redis 中存储 string 类型 yiti:lc:userID=onlyLoginCert
            jedis.set("yt:lc:" + userID, onlyLoginCert);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证登录凭证, cookie 和 redis 中的是否一致
     *
     * @param loginCert 本地 cookie 中获取登录凭证
     * @param userID    本地 cookie 中获取用户ID
     * @return
     */
    public boolean checkLoginCert(String loginCert, String userID) {
        try (Jedis jedis = jedisPool.getResource()) {
            return Objects.equals(jedis.get("yt:lc:" + userID), loginCert);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取 userID, 并选择进行添加新用户操作
     *
     * @param authUser GitHub 登录成功后返回的用户信息
     * @return DB 中的 userID
     */
    public Long signInOrUp(AuthUser authUser) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andGithubIdEqualTo(authUser.getUuid());

        List<User> resList = userMapper.selectByExample(userExample);
        if (resList.isEmpty()) {
            // 为新用户
            User user = new User();
            user.setId(snowFlakeIdWorker.nextId());
            user.setUsername("新用户" + user.getId().toString().substring(4, 10));   // 初始名称
            user.setGithubId(authUser.getUuid());
            user.setEmail(authUser.getEmail());
            user.setBalance(100L);                                                    // 初始余额设为 100

            if (userMapper.insert(user) <= 0) {
                return -1L;
            }
            return user.getId();
        }

        return resList.get(0).getId();
    }

    /**
     * 根据用户 ID 获取用户信息
     *
     * @param userID DB 中的 userID
     * @return 用户信息
     */
    public UserQueryResp selectUserByID(Long userID) {
        UserQueryResp userQueryResp = null;
        try {
            User user = userMapper.selectByPrimaryKey(userID);
            userQueryResp = CopyUtil.copy(user, UserQueryResp.class);
//            LOG.info("user: {}", userQueryResp.toString());

            // 检查用户 vip 是否到期
            ifVipEnd(user);

            // 查询 vip 剩余时间
            if (user.getType() == 2) {
                try (Jedis jedis = jedisPool.getResource()) {
                    String vipKey = "yt:vip:" + user.getEmail();
                    userQueryResp.setRemainDays(String.valueOf(jedis.ttl(vipKey) / (60L * 60 * 24)));
                } catch (Exception e) {
                    LOG.info("查询 vip 剩余时间出错");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userQueryResp;
    }

    /**
     * 注册时账号是否符合限制
     *
     * @param resp 传入最终返回结果类的引用, 进行修改
     */
    public void isRegisterUserAccount(String userAccount, CommonResp resp) {

        // 判断账号长度
        if (userAccount.length() < 5 || userAccount.length() > 12) {
            resp.setSuccess(false);
            resp.setMessage(resp.getMessage() + "账号需要 5 - 12 个位; ");
        }

        // 判断账号是否有中文
        Pattern pattern1 = Pattern.compile("^[a-zA-Z0-9]*$");
        if (!pattern1.matcher(userAccount).matches()) {
            resp.setSuccess(false);
            resp.setMessage(resp.getMessage() + "账号只允许有数字和大小写字母; ");
        }

        //判断账号是否有空格
        if (UserAccountLimit.userAccountSpace(userAccount)) {
            resp.setSuccess(false);
            resp.setMessage(resp.getMessage() + "账号中不能含有空格; ");
            return;
        }

        // 判断账号唯一
        if (null != UserAccountLimit.existAUserByAc(userAccount, new UserExample(), userMapper)) {
            resp.setSuccess(false);
            resp.setMessage(resp.getMessage() + "账号重复; ");
            return;

        }

        // 判断敏感词
        if (UserAccountLimit.userAccountPolite(userAccount, DisallowWordService.root) == 1) {
            resp.setSuccess(false);
            resp.setMessage(resp.getMessage() + "含有敏感词; ");  // 原有的信息也不要丢
            return;
        }
    }

    /**
     * 登录时账号是否符合限制
     *
     * @param resp 传入最终返回结果类的引用, 进行修改
     */
    public void isLoginUserAccount(String userAccount, CommonResp resp) {

        // 判断账号长度
        if (userAccount.length() < 5 || userAccount.length() > 12) {
            resp.setSuccess(false);
            resp.setMessage(resp.getMessage() + "账号需要 5 - 12 个位; ");
        }

        // 判断账号是否有中文
        Pattern pattern1 = Pattern.compile("^[a-zA-Z0-9]*$");
        if (!pattern1.matcher(userAccount).matches()) {
            resp.setSuccess(false);
            resp.setMessage(resp.getMessage() + "账号不存在; ");
        }

        //判断账号是否有空格, 是否有敏感词
        if (UserAccountLimit.userAccountSpace(userAccount)
                || UserAccountLimit.userAccountPolite(userAccount, DisallowWordService.root) == 1) {
            resp.setSuccess(false);
            resp.setMessage(resp.getMessage() + "账号不存在; ");
            return;
        }

        // 判断是否未注册
        if (null == UserAccountLimit.existAUserByAc(userAccount, new UserExample(), userMapper)) {
            resp.setSuccess(false);
            resp.setMessage("账号未注册");
            return;
        }
    }

    /**
     * 注册时密码是否符合限制
     *
     * @param resp 传入最终返回结果类的引用, 进行修改
     */
    public void isRegisterPassword(String password, CommonResp resp) {
        switch (PasswordLimit.passWordLimit(password)) {
            case 3 -> {
                resp.setSuccess(false);
                resp.setMessage(resp.getMessage() + "密码需要至少两种字符; ");
            }
            case 2 -> {
                resp.setSuccess(false);
                resp.setMessage(resp.getMessage() + "密码只能包含数字和英文大小写三种字符; ");
            }
            case 1 -> {
                resp.setSuccess(false);
                resp.setMessage(resp.getMessage() + "密码长度只能在 6 - 16 位; ");
            }
        }
    }

    /**
     * 验证登录时的"密码"是否符合限制
     *
     * @param resp 传入最终返回结果类的引用, 进行修改
     */
    public void isLoginPassword(UserQueryReq userQueryReq, CommonResp resp) {
        // 如果邮箱已经错误, 返回
        if (!resp.getSuccess()) {
            return;
        }

        // 判断密码是否符合限制
        if (PasswordLimit.passWordLimit(userQueryReq.getPassword()) != 0) {
            resp.setSuccess(false);
            resp.setMessage("密码错误");
            return;
        }

        // 判断密码是否正确
        User user = selectAUserByEmail(userQueryReq.getEmail());                                        // 根据 email 获取数据库中的 user, 这里的 user 在数据库中必定存在; 如果不存在, 在第一个判断中已经返回了
        if (user == null) {
            resp.setSuccess(false);
            resp.setMessage("此邮箱是不是还没注册呢?");
            return;
        }
        userQueryReq.setId(user.getId());
        String nowPassword = userQueryReq.getPassword();                                                   // 从前端传过来的 password
        nowPassword = Md5Encrypt.mdtEncrypt(nowPassword, user.getSalt(), (long) nowPassword.length());     // 对前端传过来的 password 进行加密
        if (!Objects.equals(user.getPassword(), nowPassword)) {                                            // 将前端传过来的密码加密后与数据库中的 user 密码比较
            resp.setSuccess(false);
            resp.setMessage("密码错误");
            return;
        }

    }

    /**
     * 验证登录时的"密码"是否符合限制
     *
     * @param resp 传入最终返回结果类的引用, 进行修改
     */
    public void isLoginPassword0(UserQueryReq userQueryReq, CommonResp resp) {
        // 如果账号已经不对, 直接返回
        if (!resp.getSuccess()) {
            return;
        }

        // 判断密码是否符合限制
        if (PasswordLimit.passWordLimit(userQueryReq.getPassword()) != 0) {
            resp.setSuccess(false);
            resp.setMessage("密码错误");
            return;
        }

        // 判断密码是否正确
        User user = selectAUserByAc(userQueryReq.getUseraccount());                                        // 根据账号获取数据库中的 user, 这里的 user 在数据库中必定存在; 如果不存在, 在第一个判断中已经返回了
        userQueryReq.setId(user.getId());
        String nowPassword = userQueryReq.getPassword();                                                   // 从前端传过来的 password
        nowPassword = Md5Encrypt.mdtEncrypt(nowPassword, user.getSalt(), (long) nowPassword.length());     // 对前端传过来的 password 进行加密
        if (!Objects.equals(user.getPassword(), nowPassword)) {                                            // 将前端传过来的密码加密后与数据库中的 user 密码比较
            resp.setSuccess(false);
            resp.setMessage("密码错误");
            return;
        }

    }

    /**
     * 加密传入User类中的密码, 并将加密后的密码设置到 user 中
     */
    public void encryptPassword(UserQueryReq user, String salt) {
        // 传入密码长度作为盐值置乱种子
        user.setPassword(Md5Encrypt.mdtEncrypt(user.getPassword(), salt, (long) user.getPassword().length()));
    }

    /**
     * 使用雪花算法设置盐值
     *
     * @return 返回盐值
     */
    public String setSalt(UserQueryReq user) {
        SnowFlakeIdWorker snowFlakeIdWorker = new SnowFlakeIdWorker(0, 0);
        String salt = Long.toString(snowFlakeIdWorker.nextId());
        user.setSalt(salt);
        return salt;
    }

    /**
     * 根据 email 返回这个 user
     *
     * @return 是 - 返回 user 对象; 否 - 返回 null
     */
    public User selectAUserByEmail(String email) {
        List<User> userList = new ArrayList<>();
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andEmailEqualTo(email);

        try {
            userList = userMapper.selectByExample(userExample);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userList.size() == 0 ? null : userList.get(0);

    }

    /**
     * 根据 userAccount 返回这个 user
     *
     * @return 是 - 返回 user 对象; 否 - 返回 null
     */
    public User selectAUserByAc(String userAccount) {
        List<User> userList = null;
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andUseraccountEqualTo(userAccount);

        try {
            userList = userMapper.selectByExample(userExample);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userList == null ? null : userList.get(0);

    }

    /**
     * 添加用户
     */
    public void addUser(UserQueryReq user, CommonResp resp) {
        User userInsert = CopyUtil.copy(user, User.class);

        // 如果没在 gptInviteService.isInvite(user, resp); 中设置的话, 在这设置ID
        if (userInsert.getId() == null) {
            userInsert.setId(snowFlakeIdWorker.nextId());
        }

        // 如果没在 gptInviteService.isInvite(user, resp); 中设置的话, 在这设置昵称
        if (userInsert.getUsername() == null) {
            userInsert.setUsername("新用户" + userInsert.getId().toString().substring(4, 10));
        }

        userInsert.setBalance(initBalance);             // 用户初始提问次数
        userInsert.setType(1);
        try {
            if (userMapper.insert(userInsert) != 0) {
                resp.setMessage(resp.getMessage() + "用户添加成功");
            } else {
                resp.setSuccess(false);
                resp.setMessage(resp.getMessage() + "用户插入失败");
            }
        } catch (DuplicateKeyException e) {
            resp.setMessage("用户已经存在了呦~");
        }
    }

    /**
     * 更新用户
     */
    public void updateUser(UserQueryReq user, CommonResp resp) {
        User userUpdate = CopyUtil.copy(user, User.class);

        try {
            userMapper.updateByPrimaryKeySelective(userUpdate);
        } catch (Exception e) {
            LOG.error("更新用户失败", e);
        }
    }

    /**
     * 验证邮箱格式
     *
     * @param email email
     * @param resp  回调 resp
     */
    public void isLoginEmail(String email, CommonResp resp) {
        Pattern pattern1 = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
        if (!pattern1.matcher(email).matches()) {
            resp.setSuccess(false);
            resp.setMessage("邮箱格式不对呦");
        }
        return;
    }

    /**
     * 发送激活邮件
     *
     * @param email 目的 email
     * @param resp  回调 resp
     * @return 是否发送成功
     */
    public void sendActiveEmail(String email, Boolean isExistUser, CommonResp resp) {
        Pattern pattern1 = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
        if (!pattern1.matcher(email).matches()) {
            resp.setSuccess(false);
            resp.setMessage("邮箱格式错误");
            return;
        }

        // 是否需要检测用户存在性
        if (isExistUser) {
            // 检测用户是否存在
            if (selectAUserByEmail(email) != null) {
                resp.setSuccess(false);
                resp.setMessage("用户已经存在喽");
                return;
            }
        }

        // 发送邮件
        if (!mailService.sendActiveEmail(email)) {
            LOG.error("发送邮件失败, email: {}", email);
        }
    }

    /**
     * 验证邮箱是否有效
     *
     * @param email      email
     * @param verifyCode 验证码
     * @param resp       回调 resp
     */
    public void isActiveEmail(String email, String verifyCode, CommonResp resp) {
        if (!resp.getSuccess()) {
            return;
        }
        try (Jedis jedis = UserService.jedisPool.getResource()) {
            if (Objects.equals(jedis.get("yt:ac:email:" + email), verifyCode)) {
                resp.setSuccess(true);
                return;
            }
        }
        resp.setMessage("验证码不对呦");
        resp.setSuccess(false);
    }

    /**
     * 验证邀请码是否有效
     */
    public void isInvite(String inviteCode, CommonResp resp) {
        if (!resp.getSuccess()) {
            return;
        }
        try (Jedis jedis = UserService.jedisPool.getResource()) {
            if (jedis.exists("yt:ac:code:" + inviteCode)) {
                jedis.expire("yt:ac:code:" + inviteCode, -2);
                return;
            }
        }
        resp.setSuccess(false);
        resp.setMessage("邀请码无效");
    }

    /**
     * 获取 num 个邀请码
     *
     * @return 生成所有邀请码的 List
     */
    public ArrayList<String> getInviteCode(Integer num) {
        ArrayList<String> codes = new ArrayList<>(num);
        try (Jedis jedis = UserService.jedisPool.getResource()) {
            for (int i = 0; i < num; ++i) {
                String code = InviteCodeGenerate.next();
                jedis.setex("yt:ac:code:" + code, 24 * 60 * 60, "");
                codes.add(code);
            }
        }
        return codes;
    }

    /**
     * 获取 num 个 [永久] 邀请码
     *
     * @return 生成所有邀请码的 List
     */
    public ArrayList<String> getInviteCodePer(Integer num) {
        ArrayList<String> codes = new ArrayList<>(num);
        try (Jedis jedis = UserService.jedisPool.getResource()) {
            for (int i = 0; i < num; ++i) {
                String code = InviteCodeGenerate.next();
                jedis.set("yt:ac:code:" + code, "");
                codes.add(code);
            }
        }
        return codes;
    }

    /**
     * 验证用户 email 是否存在; 若存在, 返回查询信息
     *
     * @return 查询信息 or null
     */
    public UserQueryReq isExitsUserEmail(UserQueryReq user, CommonResp resp) {
        if (!resp.getSuccess()) return user;

        User oldUser = selectAUserByEmail(user.getEmail());
        if (oldUser == null) {
            resp.setSuccess(false);
            resp.setMessage("用户不存在");
        } else {
            String password = user.getPassword();
            String verifyCode = user.getVerifyCode();
            user = CopyUtil.copy(oldUser, UserQueryReq.class);
            user.setPassword(password);
            user.setVerifyCode(verifyCode);
        }

        return user;
    }

    public void permissionValid(Long userID, Long count, CommonResp resp) {
        try {
            User user = userMapper.selectByPrimaryKey(userID);  // 查的是整个 user, 性能可提升
            switch (user.getType()) {
                case 2 -> {
                    return;
                }
                case 1, 0 -> {
                    if (user.getBalance() <= count) {
                        resp.setSuccess(false);
                        resp.setMessage("剩余提问次数不足");
                    }
                    userMapperCust.balanceGetAndDecrNum(userID, count);
                }
            }
        } catch (Exception e) {
            resp.setSuccess(false);
            resp.setMessage("用户权限验证出错");
            LOG.error("权限验证(扣除提问次数)出错");
        }

    }

    public void payWith(String userEmail, Long count, CommonResp resp) {
        try {
            if (selectAUserByEmail(userEmail) == null) {
                resp.setSuccess(false);
                resp.setMessage("用户不存在, 充值失败!");
                return;
            }
            userMapperCust.payWithCount(userEmail, count);
            resp.setMessage("充值成功!");
        } catch (Exception e) {
            LOG.error("用户{} 充值失败", userEmail, e);
            resp.setSuccess(false);
            resp.setMessage("充值失败!");
        }
    }

    public String getGptTotalInfo() {
        String res = "";

        // 获取当日日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");   // 只要年月日
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String nowTime = sdf.format(new Date());

        try (Jedis jedis = jedisPool.getResource()) {
            String ntimes = jedis.get("yt:gpt:ntimes:" + nowTime);
            String ntokens = jedis.get("yt:gpt:ntokens:" + nowTime);
            String vtimes = jedis.get("yt:gpt:vtimes:" + nowTime);
            String vtokens = jedis.get("yt:gpt:vtokens:" + nowTime);
            String iVtimes = JedisUtil.getIfNotToZero(jedis, "yt:gpti:vtimes:" + nowTime);
            res += "当日普通用户: 提问总数: " + ntimes + ", 消耗的总token: " + ntokens + "\n"
                    + "当日会员用户: 提问总数: " + vtimes + ", 消耗的总token: " + vtokens + "\n"
                    + "当日画图使用次数: " + iVtimes + "\n"
                    + "截止到目前普通用户: 总共消耗: 提问次数: " + (Long.parseLong(jedis.get("yt:gpt:ntimes:ntotal")) + Long.parseLong(ntimes))
                    + ", 消耗的总token: " + (Long.parseLong(jedis.get("yt:gpt:ntokens:ntotal")) + Long.parseLong(ntokens)) + "\n"
                    + "截止到目前会员用户: 总共消耗: 提问次数: " + (Long.parseLong(jedis.get("yt:gpt:vtimes:vtotal")) + Long.parseLong(vtimes))
                    + ", 消耗的总token: " + (Long.parseLong(jedis.get("yt:gpt:vtokens:vtotal")) + Long.parseLong(vtokens)) + "\n"
                    + "截止到目前画图使用次数: " + (Long.parseLong(jedis.get("yt:gpti:vtimes:vtotal")) + Long.parseLong(iVtimes));

        } catch (Exception e) {
            LOG.error("获取当日的 [提问总数] 与 [消耗的总 token] 失败", e);
        }

        return res;
    }

    public void loginPerDay(String userID) {
        UserQueryResp userQueryResp = selectUserByID(Long.valueOf(userID));
    }

    private String getUserTypeStr(Integer userType) {
        return switch (userType) {
            case 1 -> "普通用户";
            case 2 -> "会员";
            case 3 -> "超级会员";
            default -> "未知用户";
        };
    }

    public void setUserType(String userEmail, Integer type, Integer days, CommonResp resp) {
        User user = selectAUserByEmail(userEmail);

        if (user == null) {
            resp.setSuccess(false);
            resp.setMessage("用户不存在!");
            return;
        }

        Integer oldType = user.getType();
        if (Objects.equals(oldType, type)) {
            resp.setSuccess(false);
            resp.setMessage("已经是" + getUserTypeStr(type) + "了!");
            return;
        }
        user.setType(type);

        try {
            userMapper.updateByPrimaryKey(user);
            if (type == 2) {
                String vipKey = "yt:vip:" + userEmail;
                try (Jedis jedis = jedisPool.getResource()) {
                    jedis.setex(vipKey, 60L * 60 * 24 * days, "");
                }
            }
        } catch (Exception e) {
            resp.setSuccess(false);
            resp.setMessage("用户类型修改出错");
            LOG.error("用户类型修改出错");
            return;
        }

        resp.setMessage("原来: " + getUserTypeStr(oldType) + ", 现在: " + getUserTypeStr(type));
    }

    private void ifVipEnd(User user) {
        if (user.getType() != 2) {
            return;
        }

        try (Jedis jedis = jedisPool.getResource()) {
            String vipKey = "yt:vip:" + user.getEmail();
            boolean exists = jedis.exists(vipKey);

            if (!exists) {
                // 到期了
                user.setType(1);
                try {
                    userMapper.updateByPrimaryKey(user);
                    LOG.info("vip 到期, userEmail: {}", user.getEmail());
                } catch (Exception e) {
                    LOG.error("权限验证(会员到期, 更改用户类型)出错");
                }
            }
        }
    }

    public String getGptAllInfo(Integer days) throws ParseException {
        if (days < 1) {
            return "days 参数非法";
        }
        List<ChatRecordInfo> res = null;
        ChatRecordInfoExample chatRecordInfoExample = new ChatRecordInfoExample();
        chatRecordInfoExample.setOrderByClause("id desc");      // 按 id 倒序返回, 即离现在时间最近的 20 条记录

        try {
            PageHelper.startPage(1, days, true);
            res = chatRecordInfoMapper.selectByExample(chatRecordInfoExample);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

        StringBuilder resStr = new StringBuilder();

        for (ChatRecordInfo chatRecordInfo : res) {
            resStr.append("日期:").append(transferDateFormat(chatRecordInfo.getDate())).append("\n")
                    .append("普通用户消耗提问次数:").append(chatRecordInfo.getNtimes())
                    .append(", 消耗token: ").append(chatRecordInfo.getNtokens()).append("\n")
                    .append("会员消耗提问次数:").append(chatRecordInfo.getVtimes())
                    .append(", 消耗token: ").append(chatRecordInfo.getVtokens()).append("\n\n");
        }

        return resStr.toString();
    }

    private String transferDateFormat(String oldDate) throws ParseException {
        // 定义原始日期格式
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");

        // 将原始日期字符串解析为 Date 对象
        Date date = originalFormat.parse(oldDate);

        // 定义目标日期格式
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy年MM月dd日");

        // 将 Date 对象格式化为目标日期字符串
        String targetDateStr = targetFormat.format(date);

        return targetDateStr;
    }

    public void signInPerDay(Long userID, CommonResp resp) {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = "yt:spd:" + userID;

            // 设置 ex, nx 选项
            SetParams setParams = new SetParams();
            setParams.nx().ex(getTodayRemainSec());

            if (Objects.equals(jedis.set(key, "", setParams), "0")) {
                // 设置失败, 已经存在该键
                resp.setSuccess(false);
                resp.setMessage("今天已经签过到嘞");
                return;
            } else {
                // 设置成功
                try {
                    userMapperCust.balanceGetAndDecrNum(userID, 10L);
                    resp.setMessage("签到成功!");
                } catch (Exception e) {
                    resp.setSuccess(false);
                    resp.setMessage("签到出错! 请联系客服");
                    return;
                }
            }
        }
    }


    public void logCookieLcOut(String userID, HttpServletResponse response, CommonResp resp) {
        if (Objects.equals(userID, "-1")) {
            resp.setSuccess(false);
            resp.setMessage("用户登出异常, 请尝试关闭当前页面, 重新打开");
            return;
        }

        // 设置: 携带cookie, 跨域的头部
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", myAddr);

        // 删除本地唯一登录凭证 Cookie
        Cookie cookieLoginCert = new Cookie("yiti_loginCert", "");
        cookieLoginCert.setMaxAge(0);
        cookieLoginCert.setPath("/");
        response.addCookie(cookieLoginCert);

        // 删除本地自动登录账号信息 Cookie
        Cookie cookieUserID = new Cookie("yiti_userID", "");
        cookieUserID.setMaxAge(0);
        cookieUserID.setPath("/");
        response.addCookie(cookieUserID);

        // 删除 redis 中的凭证
        String lcKey = "yt:lc:" + userID;
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(lcKey);
        } catch (Exception e) {
            LOG.error("用户登出异常, useID:{}", userID, e);
            resp.setSuccess(false);
            resp.setMessage("用户登出异常");
            return;
        }
    }
}
