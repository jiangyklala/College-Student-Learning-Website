package com.jxm.yiti.controller;

import com.jxm.yiti.interceptor.WxAppInterceptor;
import com.jxm.yiti.req.PaymentReq;
import com.jxm.yiti.req.WxLoginReq;
import com.jxm.yiti.req.WxOnePaymentReq;
import com.jxm.yiti.resp.CommonResp2;
import com.jxm.yiti.resp.WxLoginResp;
import com.jxm.yiti.resp.WxUserInfoResp;
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
     * 验证是否已购买此题, 或者有足够的积分, 并扣除
     */
    @PostMapping("/payForQuestion")
    @ResponseBody
    public CommonResp2 payForQuestion(@RequestBody PaymentReq paymentReq) {
        CommonResp2 commonResp = new CommonResp2<>();
        wxUserService.payForQuestion(commonResp, paymentReq, WxAppInterceptor.getWxUserId());

        return commonResp;
    }

    /**
     * 购买某道面试题, 扣除 10 积分
     */
    @PostMapping("/payOneQuestion")
    @ResponseBody
    public CommonResp2 payOneQuestion(@RequestBody WxOnePaymentReq wxOnePaymentReq) {
        CommonResp2 commonResp = new CommonResp2<>();
        wxUserService.payOneQuestion(commonResp, wxOnePaymentReq, WxAppInterceptor.getWxUserId());

        return commonResp;
    }

    /**
     * 刷新用户积分等信息
     */
    @PostMapping("/refreshUserInfo")
    @ResponseBody
    public CommonResp2<WxUserInfoResp> refreshUserInfo() {
        CommonResp2<WxUserInfoResp> commonResp = new CommonResp2<>();

        wxUserService.refreshUserInfo(commonResp, WxAppInterceptor.getWxUserId());

        return commonResp;
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {

        System.out.println("lala");

        return "";
    }


}
