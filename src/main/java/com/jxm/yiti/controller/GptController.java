package com.jxm.yiti.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.jxm.yiti.req.ChatCplQueryReq;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.service.GptService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/gpt")
public class GptController {

    private static final Logger LOG = LoggerFactory.getLogger(GptController.class);

    @Resource
    GptService gptService;

    @PostMapping("/getData")
    @ResponseBody
    public String get(@RequestBody JSONObject jsonObject){
        String res = gptService.sendPost(jsonObject.getString("data"));
        System.out.println(jsonObject.toJSONString());
        return JSON.toJSONString(res);
    }

    @PostMapping("/getData2/{data}")
    @ResponseBody
    public CommonResp<String> getData2(@PathVariable String data){
        CommonResp<String> resp = new CommonResp<>();
        String res = gptService.sendPost(data);
        resp.setContent(res);
        return resp;
    }

    @PostMapping("/chatCompletion")
    @ResponseBody
    public CommonResp<String> chatCompletion(@RequestBody ChatCplQueryReq chatCplQueryReq){
        CommonResp<String> resp = new CommonResp<>();
        String res = gptService.chatCompletion(chatCplQueryReq);
        resp.setContent(res);
        return resp;
    }

    @PostMapping("/chatCompletion2")
    @ResponseBody
    public CommonResp<String> chatCompletion2(@RequestBody @Valid ChatCplQueryReq chatCplQueryReq){
        CommonResp<String> resp = new CommonResp<>();
        String res = gptService.chatCompletion2(chatCplQueryReq);
        resp.setContent(res);
        return resp;
    }

    @PostMapping("/test1")
    @ResponseBody
    public void test1(){
        ArrayList<Thread> list = new ArrayList<>(10);

        for (int i = 0; i < 10; ++i) {
            int finalI = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    long l = System.currentTimeMillis();
                    String hello = gptService.sendPost("Hello");
                    LOG.info("线程" + finalI + "完成, 时间:" + String.valueOf(System.currentTimeMillis() - l) + ", 数据:" + hello);
                }
            });
            list.add(thread);
        }

        for (Thread t : list) {
            t.start();
        }

        for (Thread t : list) {
            try {
                t.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        System.out.println("jaja");
    }

    @PostMapping("/test2/{data}")
    @ResponseBody
    public void test2(@PathVariable String data){
        ArrayList<Thread> list = new ArrayList<>(10);

        for (int i = 0; i < 10; ++i) {
            int finalI = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    long l = System.currentTimeMillis();
                    String hello = gptService.sendPost(data);
                    LOG.info("线程" + finalI + "完成, 时间:" + String.valueOf(System.currentTimeMillis() - l) + ", 数据:" + hello);
                }
            });
            list.add(thread);
        }

        for (Thread t : list) {
            t.start();
        }

        for (Thread t : list) {
            try {
                t.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        System.out.println("jaja");
    }

}
