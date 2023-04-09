package com.jxm.yiti.service;

import com.jxm.yiti.domain.MailObject;
import jakarta.annotation.Resource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MailService {

    //注入邮件发送对象
    @Resource
    private JavaMailSender mailSender;

    public boolean simpleSend(MailObject mailObject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailObject.getTo());                  // 设置收件人邮箱
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
