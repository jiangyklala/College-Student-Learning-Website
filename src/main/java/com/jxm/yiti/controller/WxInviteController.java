package com.jxm.yiti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxm.yiti.interceptor.WxAppInterceptor;
import com.jxm.yiti.resp.CheckInviteCodeResp;
import com.jxm.yiti.resp.CommonResp2;
import com.jxm.yiti.resp.WxInviteCodeResp;
import com.jxm.yiti.resp.WxInviterResp;
import com.jxm.yiti.service.WxInviteService;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyunkai <jiangyunkai@kuaishou.com>
 * Created on 2024-04-05
 */
@Slf4j
@Controller
@RequestMapping("/wxInvite")
public class WxInviteController {

    @Resource
    WxInviteService wxInviteService;

    /**
     * 查询某个邀请人的全部信息
     */
    @GetMapping("/selectInfo")
    @ResponseBody
    public CommonResp2 selectInfoByID() {
        CommonResp2<WxInviterResp> resp = new CommonResp2<>();

        wxInviteService.selectInfo(WxAppInterceptor.getWxUserId(), resp);

        return resp;
    }

    /**
     * 查询某个用户下的所有邀请码
     * ps: 目前微信小程序的设计中, 每个用户只有一个邀请码
     */
    @GetMapping("/selectAllInviteCode")
    @ResponseBody
    public CommonResp2 selectAllInviteCode() {
        CommonResp2<WxInviteCodeResp> resp = new CommonResp2<>();

        wxInviteService.selectAllInviteCode(WxAppInterceptor.getWxUserId(), resp);

        return resp;
    }

    /**
     * 检验邀请码是否有效
     * ps: 目前微信小程序的设计中, 每个用户只有一个邀请码
     */
    @GetMapping("/checkInviteCode/{inviteCode}")
    @ResponseBody
    public CommonResp2<CheckInviteCodeResp> checkInviteCode(@PathVariable String inviteCode) {
        CommonResp2<CheckInviteCodeResp> resp = new CommonResp2<>();

        wxInviteService.checkInviteCode(WxAppInterceptor.getWxUserId(), inviteCode, resp);

        return resp;
    }
}
