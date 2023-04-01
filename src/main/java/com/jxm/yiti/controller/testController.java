package com.jxm.yiti.controller;

import jakarta.annotation.Resource;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/test")
public class testController {

    @Resource
    public RocketMQTemplate rocketMQ;

    @GetMapping("/test1")
    @ResponseBody
    public void test1() throws MQClientException {
        rocketMQ.convertAndSend("yiti_newItem_notifyAll", "有新文档发布");

    }

}
