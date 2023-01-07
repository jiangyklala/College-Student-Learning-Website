package com.jxm.yiti.controller;

import com.jxm.yiti.domain.Demo;
import com.jxm.yiti.service.DemoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class DemoController {

    @Resource
    private DemoService demoService;

    @GetMapping("/demo/list")
    @ResponseBody
    public List<Demo> list() {
        return demoService.list();
    }

}
