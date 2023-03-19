package com.jxm.yiti.controller;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletResponse;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthGithubRequest;
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
@RequestMapping("/oauth")
public class AuthController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/render")
    @ResponseBody
    public void renderAuth(HttpServletResponse response) throws IOException {
        AuthRequest authRequest = getAuthRequest();
        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
    }

    @GetMapping("/callback")
    @ResponseBody
    public Object login(AuthCallback callback) {
        AuthRequest authRequest = getAuthRequest();
        AuthResponse<AuthUser> authResponse = authRequest.login(callback);

        //打印授权返回代码（2000 表示成功，可以用来判断用户登录成功与否）
        System.out.println("状态码：" + authResponse.getCode());

        //打印用户的昵称、ID、头像等基本信息
        LOG.info("用户的UnionID：" + authResponse.getData().getUuid());
        LOG.info("用户的昵称：" + authResponse.getData().getNickname());
        LOG.info("用户的头像：" + authResponse.getData().getAvatar());

        //打印用户的Token中的信息
        LOG.info("access_token：" + authResponse.getData().getToken().getAccessToken());
        LOG.info("用户的OpenId：" + authResponse.getData().getToken().getOpenId());

        // 打印更加详细的信息（第三方平台返回的原始用户信息）
        LOG.info("用户的城市：" + authResponse.getData().getRawUserInfo().getInnerMap().get("city"));
        LOG.info("用户的年份：" + authResponse.getData().getRawUserInfo().getInnerMap().get("year"));

        LOG.info(JSON.toJSONString(authResponse));
        return authResponse;
    }

    private AuthRequest getAuthRequest() {
        return new AuthGithubRequest(AuthConfig.builder()
                .clientId("7f64b8527592ce2d4d7c")
                .clientSecret("bc5d42bee691771f547351fec23f445676aeb10b")
                .redirectUri("http://124.223.184.187:8111/oauth/callback")
                .build());
    }
}
