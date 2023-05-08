package com.jxm.yiti.service;

import com.jxm.yiti.domain.EmailActive;
import com.jxm.yiti.domain.MailObject;
import com.jxm.yiti.mapper.EmailActiveMapper;
import jakarta.annotation.Resource;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Service
public class MailService {

    //注入邮件发送对象
    @Resource
    private JavaMailSender mailSender;

    //注入邮件发送对象
    @Resource
    private EmailActiveMapper emailActiveMapper;


    public boolean simpleSend(MailObject mailObject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailObject.getTo());                  // 设置收件人邮箱  //"GPTalk<13837774652@163.com>"
        message.setFrom("GPTalk<13837774652@163.com>");
//        message.setCc("xiaofeng500@qq.com");              // 设置抄报人邮箱（可以不填写）
//        message.setBcc("575814158@qq.com");               // 设置密送人邮箱（可以不填写）
        message.setSubject(mailObject.getSubject());        // 设置邮件主题
        message.setText(mailObject.getText());              // 设置邮件文本内容
        message.setSentDate(new Date());                    // 设置邮件发送时间

        try {
            //发送
            mailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean sendActiveEmail(String email) {

        String verifyCode = String.valueOf((int)(Math.random() * 9000) + 1000);

        // 激活码有效期 5 min
        try (Jedis jedis = UserService.jedisPool.getResource()) {
            jedis.setex("yt:ac:email:" + email, 5 * 60, verifyCode);
        }

        SimpleMailMessage message = new SimpleMailMessage();

        String content = "邮箱验证码: " + verifyCode;

        message.setTo(email);                               // 设置收件人邮箱
        message.setFrom("GPTalk<13837774652@163.com>");
//        message.setCc("xiaofeng500@qq.com");              // 设置抄报人邮箱（可以不填写）
//        message.setBcc("575814158@qq.com");               // 设置密送人邮箱（可以不填写）
        message.setSubject("激活邮箱");                      // 设置邮件主题
        message.setText(content);                           // 设置邮件文本内容
        message.setSentDate(new Date());                    // 设置邮件发送时间

        try {
            //发送
            mailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean simpleSend(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);                            // 设置收件人邮箱
        message.setSubject(subject);                  // 设置邮件主题
        message.setText(text);                        // 设置邮件文本内容
        message.setSentDate(new Date());              // 设置邮件发送时间

        try {
            //发送
            mailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
