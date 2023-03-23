package com.jxm.yiti.service;

import com.jxm.yiti.domain.User;
import com.jxm.yiti.domain.UserExample;
import com.jxm.yiti.mapper.UserMapper;
import com.jxm.yiti.req.UserSaveReq;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.UserQueryResp;
import com.jxm.yiti.utils.CopyUtil;
import com.jxm.yiti.utils.SnowFlakeIdWorker;
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
}
