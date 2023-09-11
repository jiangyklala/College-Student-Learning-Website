package com.jxm.yiti.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

@Slf4j
public class TokenUtil {

    // 加密算法选择
    private static final String ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * 生成微信小程序用户身份认证 auth_token
     *
     * @param encryptInfo 带加密的用户信息 --- json 对象
     * @param loginSecret 16字节的密钥
     */
    public static String wxAppAuthToken(String encryptInfo, String loginSecret, Duration lastTime) {
        try {

            // 将 lastTime 加到 encryptInfo 中
            JSONObject jsonObject = JSON.parseObject(encryptInfo);

            // 计算 Token 过期时间, 并转化为秒数
            long expirationTimestamp = Instant.now().plus(lastTime).toEpochMilli() / 1000;
            jsonObject.put("Expired", expirationTimestamp);
            encryptInfo = jsonObject.toJSONString();

//            log.info("encryptInfo = {}", encryptInfo);

            // 将密钥转化为 SecretKeySpec 对象
            Key secretKey = new SecretKeySpec(loginSecret.getBytes(StandardCharsets.UTF_8), ALGORITHM);

            // 创建一个AES密码（Cipher）对象，指定使用 ECB 模式和 PKCS5 填充
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

            // 使用密钥初始化密码对象, 加密模式
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // 加密
            byte[] encryptedBytes = cipher.doFinal(encryptInfo.getBytes(StandardCharsets.UTF_8));

            // 返回 Base64 编码字符串
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密 auth_token, 获取原始信息
     */
    public static String decryptToken(String token, String loginSecret) {
        try {
            Key secretKey = new SecretKeySpec(loginSecret.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

            // 使用解密模式
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] encryptedBytes = Base64.getDecoder().decode(token);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            String encryptInfo = new String(decryptedBytes, StandardCharsets.UTF_8);

//            log.info("encryptInfo = {}", encryptInfo);

            return encryptInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 验证 auth_token 是否过期
     *
     * @param token       auth_token
     * @param loginSecret 密钥
     */
    public static boolean checkIfExpired(String token, String loginSecret) {
        try {
            // 获取到 Expired 字段
            String data = decryptToken(token, loginSecret);
            JSONObject jsonObject = JSON.parseObject(data);
            String expiredTimestamp = jsonObject.getString("Expired");


            // 获取当前时间
            Instant now = Instant.now();

            // 将 Expired 转换为Instant对象
            Instant expiration = Instant.ofEpochSecond(Long.parseLong(expiredTimestamp));

//            log.info("expiration = {}", expiration.toString());

            // 检查Token是否过期
            return now.isBefore(expiration);
        } catch (RuntimeException e) {
            return false;
        }
    }

}
