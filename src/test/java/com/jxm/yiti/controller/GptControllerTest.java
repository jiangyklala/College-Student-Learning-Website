package com.jxm.yiti.controller;

import com.jxm.yiti.service.GptService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GptControllerTest {


//    @Test
//    void getData2() {
//
//        ArrayList<Thread> list = new ArrayList<>(10);
//
//        for (int i = 0; i < 10; ++i) {
//            int finalI = i;
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    long l = System.currentTimeMillis();
//                    String hello = gptService.sendPost("帮我写一个 C 语言程序");
//                    System.out.println("线程" + finalI + "完成, 时间:" + String.valueOf(System.currentTimeMillis() - l) + ", 数据:" + hello);
//                }
//            });
//            list.add(thread);
//        }
//
//        for (Thread t : list) {
//            t.start();
//        }
//
//        for (Thread t : list) {
//            try {
//                t.join();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
////        System.out.println("jaja");
//    }
}