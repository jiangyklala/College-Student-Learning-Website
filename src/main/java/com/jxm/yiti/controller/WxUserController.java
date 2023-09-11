package com.jxm.yiti.controller;

import com.jxm.yiti.interceptor.WxAppInterceptor;
import com.jxm.yiti.mapper.WxUserInfoMapper;
import com.jxm.yiti.req.PaymentReq;
import com.jxm.yiti.req.WxLoginReq;
import com.jxm.yiti.resp.CommonResp2;
import com.jxm.yiti.resp.WxLoginResp;
import com.jxm.yiti.service.WxUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

@Controller
@RequestMapping("/wxUser")
public class WxUserController {

    @Resource
    WxUserService wxUserService;

    @Resource
    WxUserInfoMapper wxUserInfoMapper;

    /**
     * 微信小程序登录
     */
    @PostMapping("/login")
    @ResponseBody
    public CommonResp2 Login(@RequestBody WxLoginReq wxLoginReq) throws IOException, URISyntaxException {
        CommonResp2<WxLoginResp> commonResp = new CommonResp2<>();
        wxUserService.login(commonResp, wxLoginReq.getCode());

        return commonResp;
    }

    /**
     * 验证是否扣有足够的积分, 并扣除
     */
    @PostMapping("/payForQuestion")
    @ResponseBody
    public CommonResp2 payForQuestion(@RequestBody PaymentReq paymentReq) {
        CommonResp2 commonResp = new CommonResp2<>();
        wxUserService.payForQuestion(commonResp, paymentReq, WxAppInterceptor.getWxUserId());

        return commonResp;
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {

        System.out.println("lala");

        return "";
    }


}
