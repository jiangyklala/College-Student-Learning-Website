package com.jxm.yiti.service;

import com.jxm.yiti.domain.User;
import com.jxm.yiti.domain.UserExample;
import com.jxm.yiti.mapper.UserMapper;
import com.jxm.yiti.req.UserQueryReq;
import com.jxm.yiti.req.UserSaveReq;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.UserQueryResp;
import com.jxm.yiti.utils.*;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public static JedisPool jedisPool;

    private static SnowFlakeIdWorker snowFlakeIdWorker;

    @PostConstruct
    public void init() {
        snowFlakeIdWorker  = new SnowFlakeIdWorker(0, 0);            // 初始化雪花ID生成器
        jedisPool = new JedisPool(setJedisPoolConfig(), "124.223.184.187", 6379, 5000, "jiang", 1);
//        initJedisPool(jedisPool);
    }


    /**
     * 预热
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
                .redirectUri("http://124.223.184.187:8111/user/github/callback")
                .build());
    }

    /**
     * 设置用户登录凭证
     */
    public void setOnlyLoginCert(Long userID, HttpServletResponse response) throws IOException {
        String onlyLoginCert = Long.toString(snowFlakeIdWorker.nextId());           // 生成唯一登录凭证

        response.setHeader("Access-Control-Allow-Credentials","true");
        response.setHeader("Access-Control-Allow-Origin", "http://124.223.184.187:8110");

        Cookie cookieLoginCert = new Cookie("yiti_loginCert", onlyLoginCert);       // 增加本地唯一登录凭证 Cookie
        cookieLoginCert.setMaxAge(60 * 60 * 24);
        cookieLoginCert.setPath("/");
        response.addCookie(cookieLoginCert);

        Cookie cookieUserID = new Cookie("yiti_userID", Long.toString(userID));     // 增加本地自动登录账号信息 Cookie
        cookieUserID.setMaxAge(60 * 60 * 24);
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
     * @param loginCert 本地 cookie 中获取登录凭证
     * @param userID 本地 cookie 中获取用户ID
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
            user.setBalance(0L);                        // 初始余额设为 0

            if (userMapper.insert(user) <= 0) {
                return -1L;
            }
            return user.getId();
        }

        return resList.get(0).getId();
    }

    /**
     * 根据用户 ID 获取用户信息
     * @param userID DB 中的 userID
     * @return 用户信息
     */
    public UserQueryResp selectUserByID(Long userID) {
        UserQueryResp userQueryResp = null;
        try {
            User user = userMapper.selectByPrimaryKey(userID);
            userQueryResp = CopyUtil.copy(user, UserQueryResp.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userQueryResp;
    }

    /**
     * 注册时账号是否符合限制
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
     * @param resp 传入最终返回结果类的引用, 进行修改
     */
    public void isLoginPassword(UserQueryReq userQueryReq, CommonResp resp) {
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
     * @return 返回盐值
     */
    public String setSalt(UserQueryReq user) {
        SnowFlakeIdWorker snowFlakeIdWorker = new SnowFlakeIdWorker(0, 0);
        String salt = Long.toString(snowFlakeIdWorker.nextId());
        user.setSalt(salt);
        return salt;
    }

    /**
     * 根据 userAccount 返回这个 user
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
        userInsert.setId(snowFlakeIdWorker.nextId());
        userInsert.setUsername("新用户" + userInsert.getId().toString().substring(4, 10));   // 初始名称
        userInsert.setBalance(0L);
        if (userMapper.insert(userInsert) != 0) {
            resp.setMessage(resp.getMessage() + "用户添加成功");
        } else {
            resp.setSuccess(false);
            resp.setMessage(resp.getMessage() + "用户插入失败");
        }
    }
}