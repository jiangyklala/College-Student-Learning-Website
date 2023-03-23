package com.jxm.yiti.controller;

import com.jxm.yiti.domain.User;
import com.jxm.yiti.req.UserQueryReq;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.UserQueryResp;
import com.jxm.yiti.service.UserService;
import com.jxm.yiti.utils.CopyUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    public UserService userService;

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    /**
     * 登录接口
     */
    @PostMapping("/loginByAccount")
    @ResponseBody
    public CommonResp loginByAccount(@RequestBody UserQueryReq user, HttpServletResponse response) throws IOException {
        CommonResp resp = new CommonResp<>();
        LOG.info(user.toString());
        userService.isLoginUserAccount(user.getUseraccount(), resp);   // 验证账号
        userService.isLoginPassword(user, resp);                       // 验证密码, 如果密码有效, 根据此账号返回 user
        if (resp.getSuccess()) {
            // 登陆成功, 添加唯一登录凭证
            userService.setOnlyLoginCert(user.getId(), response);
        }

        return resp;
    }

    /**
     * 注册接口
     */
    @PostMapping("/register")
    @ResponseBody
    public CommonResp register(@RequestBody UserQueryReq user) {
        CommonResp resp = new CommonResp<>();
        userService.isRegisterUserAccount(user.getUseraccount(), resp);
        userService.isRegisterPassword(user.getPassword(), resp);
        if (resp.getSuccess()) {
            userService.encryptPassword(user, userService.setSalt(user));  // 设置盐值并密码加密
            userService.addUser(user, resp);
        }
        return resp;
    }

    /**
     * GitHub 登录页面
     */
    @GetMapping("/render")
    @ResponseBody
    public void renderAuth(HttpServletResponse response) throws IOException {
        AuthRequest authRequest = userService.getAuthRequest();
        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
    }

    /**
     * 自动登录
     * @param loginCert 本地 cookie 值
     * @param userID 本地 cookie 值
     */
    @PostMapping("/autoLogin")
    @ResponseBody
    public CommonResp<UserQueryResp> autoLogin(@CookieValue(value = "yiti_loginCert", defaultValue = "null") String loginCert,
                                                       @CookieValue(value = "yiti_userID", defaultValue = "null") String userID) throws IOException {
        CommonResp<UserQueryResp> resp = new CommonResp<>();
        if (Objects.equals(loginCert, "null")
                || Objects.equals(userID, "null")
                || !userService.checkLoginCert(loginCert, userID)) {
            // 本地无 cookie 或 cookie 中的自动登录凭证失效
            resp.setSuccess(false);
        } else {
            resp.setContent(userService.selectUserByID(Long.valueOf(userID)));   // 将 user 信息返回
        }

        return resp;
    }

    /**
     * GitHub 登录成功后的回调函数
     */
    @GetMapping("/github/callback")
    public void githubCallback(AuthCallback callback, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = userService.getAuthRequest();
        AuthResponse<AuthUser> authResponse = authRequest.login(callback);

        LOG.info("code= " + authResponse.getCode());
        if (authResponse.getCode() == 2000) {
            Long userID = userService.signInOrUp(authResponse.getData());                 // 通过(新增/查找已经存在的)用户, 获取 userID
            if (userID == -1L) return;
            userService.setOnlyLoginCert(userID, response);                               // 设置唯一登录凭证
        }

        response.sendRedirect("http://124.223.184.187:8110/");
    }

    @GetMapping("/getCookie1")
    @ResponseBody
    public String getCookie1(@CookieValue(value = "yiti_loginCert", defaultValue = "lala") String cookieName) throws IOException {
        return cookieName;
    }

    @GetMapping("/getCookie2")
    @ResponseBody
    public String getCookie1(HttpServletRequest request) throws IOException {
        Cookie yiti_loginCert = WebUtils.getCookie(request, "yiti_loginCert");
        if (yiti_loginCert != null) {
            return yiti_loginCert.getValue();
        }
        return "null";
    }

}
