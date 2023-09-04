package com.jxm.yiti.controller;

import com.jxm.yiti.mapper.WxUserInfoMapper;
import com.jxm.yiti.req.WxLoginReq;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.WxLoginResp;
import com.jxm.yiti.service.WxUserService;
import com.jxm.yiti.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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
    public CommonResp Login(@RequestBody WxLoginReq wxLoginReq) throws IOException, URISyntaxException {
        CommonResp<WxLoginResp> commonResp = new CommonResp<>();
        wxUserService.login(commonResp, wxLoginReq.getCode());

        return commonResp;
    }

    @GetMapping("/createToken")
    @ResponseBody
    public String createToken() {
        String token = null;

        Claims claims = (Claims) Jwts.claims().put("user_id", "user_id");
        token = JwtUtil.createToken(claims);

        return token;
    }

    @GetMapping("/refreshToken/{token}")
    @ResponseBody
    public String refreshToken(@PathVariable String token) {

        token = JwtUtil.refreshToken(token);

        return token;
    }

    @GetMapping("/getUserId/{token}")
    @ResponseBody
    public String getUserId(@PathVariable String token) {

        token = JwtUtil.getUserId(token);

        return token;
    }

}
