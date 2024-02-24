package com.jxm.yiti.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxm.yiti.interceptor.WxAppInterceptor;
import com.jxm.yiti.req.AppPayInfoReq;
import com.jxm.yiti.req.CDKeyReq;
import com.jxm.yiti.req.PaymentReq;
import com.jxm.yiti.req.WxLoginReq;
import com.jxm.yiti.req.WxOnePaymentReq;
import com.jxm.yiti.resp.CommonResp2;
import com.jxm.yiti.resp.WxLoginResp;
import com.jxm.yiti.resp.WxUserInfoResp;
import com.jxm.yiti.service.WxUserService;

import jakarta.annotation.Resource;

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
     * 刷新用户积分等信息
     */
    @PostMapping("/refreshAuthToken")
    @ResponseBody
    public CommonResp2<String> refreshAuthToken() {
        CommonResp2<String> resp = new CommonResp2<>();

        wxUserService.refreshAuthToken(WxAppInterceptor.getWxUserId(), resp);

        return resp;
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
     * 切换小程序用户的用户类型
     *
     * @param name   用户名
     * @param option 0 -- 普通用户, 1 -- 普通会员, 2 -- 训练营会员, 3 -- 超级会员
     */
    @GetMapping("/switchUserType/{name}/{option}")
    @ResponseBody
    public String switchUserType(@PathVariable String name, @PathVariable Integer option) {
        return wxUserService.switchUserType(name, option);
    }

    /**
     * 判断兑换码是否有效, 并重新生成 auth_token
     */
    @PostMapping("/isCDKeyValid")
    @ResponseBody
    public CommonResp2<String> isCDKeyValid(@RequestBody CDKeyReq cdKeyReq) {
        CommonResp2<String> resp = new CommonResp2<>();

        wxUserService.isCDKeyValid(resp, cdKeyReq.getCdKey(), WxAppInterceptor.getWxUserId());
        return resp;
    }

    /**
     * 支付接口
     */
    @PostMapping("/notifyAfter")
    @ResponseBody
    public void payAsyncNotifyAfter(@RequestBody AppPayInfoReq req) {

        wxUserService.payAsyncNotifyAfter(req);

        return;
    }

    /**
     * 支付接口
     */
    @PostMapping("/makeOrder")
    @ResponseBody
    public CommonResp2<String> makeOrder(@RequestBody AppPayInfoReq req) {

        CommonResp2<String> resp = new CommonResp2<>();
        wxUserService.makeOrder(req, WxAppInterceptor.getWxUserId(), resp);

        return resp;
    }

}
