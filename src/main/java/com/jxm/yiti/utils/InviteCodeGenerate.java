package com.jxm.yiti.utils;

import java.util.Random;

public class InviteCodeGenerate {
    // 生成随机字符串的方法
    public static String next() {
        // 定义字符串字符集，包含字母和数字
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        // 定义随机数生成器
        Random random = new Random();

        // 生成 6 位长度的随机字符串
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            // 从字符集中随机选取一个字符
            char randomChar = characters.charAt(random.nextInt(characters.length()));
            // 将随机字符加入字符串中
            sb.append(randomChar);
        }

        // 返回生成的随机字符串
        return sb.toString();
    }
}
