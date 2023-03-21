package com.jxm.yiti.controller;

import com.alibaba.fastjson2.JSON;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);


    @GetMapping("/render")
    @ResponseBody
    public void renderAuth(HttpServletResponse response) throws IOException {
        AuthRequest authRequest = userService.getAuthRequest();
        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
    }

    @GetMapping("/github/callback")
    @ResponseBody
    public CommonResp githubLogin(AuthCallback callback) {
        CommonResp<AuthUser> resp = new CommonResp();
        AuthRequest authRequest = userService.getAuthRequest();
        AuthResponse<AuthUser> authResponse = authRequest.login(callback);
        String jsonString = JSON.toJSONString(authRequest);
        if (authResponse.getCode() == 2000 && userService.signInOrUp(authResponse.getData()) > 0) {
            resp.setMessage("登陆成功");
        }

        LOG.info(jsonString);
        resp.setContent(authResponse.getData());
        return resp;
    }
}
