package com.jxm.yiti.controller;

import com.jxm.yiti.interceptor.WxAppInterceptor;
import com.jxm.yiti.req.CDKeyReq;
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
import java.util.ArrayList;

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

    /**
     * 生成升级为普通会员/训练营会员的激活码
     *
     * @param option 1 -- 升级为普通会员, 2 -- 升级为训练营会员
     * @param num    激活码的数量
     * @param time   激活码的持续时间(分钟),
     * @return 激活码列表
     * @throws IOException IOException
     */
    @GetMapping("/getNSVCDKey/{option}/{num}/{time}")
    @ResponseBody
    public String getNVCDKeyWithNumAndTime(@PathVariable Integer option, @PathVariable Integer num, @PathVariable Integer time) throws IOException {
        ArrayList<String> inviteCode = wxUserService.getNVCDKeyWithNumAndTime(option, num, time);
        return inviteCode.toString();
    }

    /**
     * 判断兑换码是否有效
     */
    @PostMapping("/isCDKeyValid")
    @ResponseBody
    public CommonResp2 isCDKeyValid(@RequestBody CDKeyReq cdKeyReq) {
        CommonResp2 resp = new CommonResp2();

        wxUserService.isCDKeyValid(resp, cdKeyReq.getCdKey(), WxAppInterceptor.getWxUserId());
        return resp;
    }

}
