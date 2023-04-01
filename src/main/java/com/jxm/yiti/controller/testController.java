package com.jxm.yiti.controller;

import com.jxm.yiti.rabbitmq.TestRabbit;
import jakarta.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/test")
public class testController {



    @GetMapping("/test1")
    @ResponseBody
    public void test1() throws Exception {
        TestRabbit.main(new String[0]);
    }

}
