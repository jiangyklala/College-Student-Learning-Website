package com.jxm.yiti.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
public class JwtUtil {

    // 添加上 "认证" 头部
    public static final String TOKEN_HEADER = "Authorization";

    // token 的前缀
    public static final String TOKEN_PREFIX = "Bearer ";

    // 本地存储的对称密钥
//    @Value("${jwt.secret}")
//    private static String secret;
    private static final SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode("asjfiaosfaiosfiaosjfsafasfaasfasfasfafasfasfafasd"));

    // JWT payload 中存储的字段
    public static final String userId = "userId";

    // token 有效期
    private static final long EXPIRATION = 60 * 60 * 24 * 30L;

    /**
     * 创建微信小程序中, 用于验证用户身份的 token 令牌
     *
     * @param claims 自定义 claim
     */
    public static String createToken(Claims claims) {

        String finalToken = Jwts.builder()
                .setSubject("WxAppAuth")
                .setClaims(claims)  // 自定义 claim
                .signWith(key)
                .setIssuedAt(new Date())  // 生成时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION * 1000)) // 过期时间
                .compact();

        log.info("token = " + finalToken);

        return finalToken;
    }

    // 公共获取自定义数据
    public static Claims getTokenClaims(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            log.error("获取 Token Body 失效", e);
        }

        return claims;
    }

    // 验证 Token 是否过期
    public static boolean isExpiration(String token) {
        try {
            return getTokenClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    // 获取 userId
    public static String getUserId(String token) {
        log.info(getTokenClaims(token).toString());
        return getTokenClaims(token).get("user_id").toString();
    }

    // 刷新 token
    public static String refreshToken(String token) {
        if (isExpiration(token)) {
            log.error("token 刷新失败！");
            return null;
        }

        token = createToken(getTokenClaims(token));
        log.info("token = " + token);

        return token;
    }
}
