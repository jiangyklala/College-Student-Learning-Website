package com.jxm.yiti.controller;

import com.jxm.yiti.mapper.WxUserInfoMapper;
import com.jxm.yiti.req.WxLoginReq;
import com.jxm.yiti.resp.CommonResp2;
import com.jxm.yiti.resp.WxLoginResp;
import com.jxm.yiti.service.WxUserService;
import com.jxm.yiti.utils.GenerateTokenUtil;
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

    @GetMapping("/encryptTest")
    @ResponseBody
    public String encryptTest() {

        String token = "ODyFhTWaRPmgqTnS9Z6FMWqXxMtYNuBQrdaZ+JI8oPl81tUKll0l41nF/cxUiLSn";

        System.out.println(GenerateTokenUtil.decryptToken(token, "USDjsfi2234jsdf1"));
//        System.out.println(GenerateTokenUtil.checkIfExpired(token, "USDjsfi2234jsdf1"));

        return "";
    }


}
