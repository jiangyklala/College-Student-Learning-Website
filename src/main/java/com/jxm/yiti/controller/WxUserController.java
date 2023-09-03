package com.jxm.yiti.controller;

import com.jxm.yiti.mapper.WxUserInfoMapper;
import com.jxm.yiti.req.WxLoginReq;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.WxLoginResp;
import com.jxm.yiti.service.WxUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public CommonResp Login(@RequestBody WxLoginReq wxLoginReq) throws IOException, URISyntaxException {
        CommonResp<WxLoginResp> commonResp = new CommonResp<>();
        wxUserService.login(commonResp, wxLoginReq.getCode());

        return commonResp;
    }

}
