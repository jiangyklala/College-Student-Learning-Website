package com.jxm.yiti.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxm.yiti.req.ConfigListReq;
import com.jxm.yiti.req.SetConfigReq;
import com.jxm.yiti.resp.CommonResp2;
import com.jxm.yiti.service.OtherService;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyunkai <jiangyunkai@kuaishou.com>
 * Created on 2024-04-18
 */
@Slf4j
@Controller
@RequestMapping("/other")
public class OtherController {

    @Resource
    OtherService otherService;

    /**
     * 动态配置项查找
     */
    @PostMapping("/selectAllConfig")
    @ResponseBody
    public CommonResp2<String> selectAllConfig(@RequestBody ConfigListReq req) {

        CommonResp2<String> resp = new CommonResp2<>();
        otherService.selectAllConfig(req, resp);

        return resp;
    }

    /**
     * 动态配置项添加
     */
    @PostMapping("/setConfig")
    @ResponseBody
    public CommonResp2 setConfig(@RequestBody SetConfigReq req) {

        CommonResp2 resp = new CommonResp2<>();
        otherService.setConfig(req, resp);

        return resp;
    }

    /**
     * 动态配置项添加
     */
    @PostMapping("/setConfigList")
    @ResponseBody
    public CommonResp2 setConfigList(@RequestBody List<String> req) {

        CommonResp2 resp = new CommonResp2<>();
        otherService.setConfigList(req, resp);

        return resp;
    }
}
