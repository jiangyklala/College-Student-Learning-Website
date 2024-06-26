package com.jxm.yiti.controller.gpt;

import com.jxm.yiti.req.AddAdminPermissionReq;
import com.jxm.yiti.req.AdminPermissionReq;
import com.jxm.yiti.req.UserQueryReq;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.UserQueryResp;
import com.jxm.yiti.service.GptInviteService;
import com.jxm.yiti.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @Resource
    GptInviteService gptInviteService;

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/signInPerDay")
    @ResponseBody
    public CommonResp signInPerDay(@RequestBody String userID) {
        CommonResp resp = new CommonResp();
        userService.signInPerDay(Long.valueOf(userID), resp);
        return resp;
    }

    /**
     * 设置用户类型
     *
     * @param userEmail 用户邮箱
     * @param type      1-普通会员, 2-会员, 3-超级会员
     * @param days      持续天数
     */
    @PostMapping("/setUserType")
    @ResponseBody
    public CommonResp setUserType(String userEmail, Integer type, Integer days) {
        CommonResp resp = new CommonResp();
        userService.setUserType(userEmail, type, days, resp);
        return resp;
    }

    /**
     * 获取当日的 [提问总数] 与 [消耗的总 token]
     */
    @GetMapping("/getGptTotalInfo")
    @ResponseBody
    public String getGptTotalInfo() {
        return userService.getGptTotalInfo();
    }

    /**
     * 获取进 n 日消耗信息
     *
     * @param days 指定近几天
     */
    @GetMapping("/getGptAllInfo/{days}")
    @ResponseBody
    public String getGptAllInfo(@PathVariable Integer days) throws ParseException {
        return userService.getGptAllInfo(days);
    }

    /**
     * 对某用户充值提问次数
     *
     * @param userEmail 用户 email
     * @param count     充值次数
     */
    @PostMapping("/payWith/{userEmail}&{count}")
    @ResponseBody
    public CommonResp payWith(@PathVariable String userEmail, @PathVariable Long count) {
        CommonResp resp = new CommonResp();
        userService.payWith(userEmail, count, resp);
        return resp;
    }

    /**
     * 对用户进行权限验证: 永久会员通行, 普通用户和普通会员扣费
     *
     * @param userID 用户 ID
     * @param count  扣费的多少
     */
    @GetMapping("/permissionValid/{userID}/{count}")
    @ResponseBody
    public CommonResp permissionValid(@PathVariable Long userID, @PathVariable Long count) {
        CommonResp resp = new CommonResp();
        userService.permissionValid(userID, count, resp);
        return resp;
    }


    /**
     * 获取邀请码 valid-24h
     *
     * @param num 激活码的数量
     * @return 激活码列表
     * @throws IOException IOException
     */
    @GetMapping("/getInviteCode/{num}")
    @ResponseBody
    public String getInviteCode(@PathVariable Integer num) throws IOException {
        ArrayList<String> inviteCode = userService.getInviteCode(num);
        return inviteCode.toString();
    }

    /**
     * 获取永久激活码
     *
     * @param num 激活码的数量
     * @return 激活码列表
     * @throws IOException IOException
     */
    @GetMapping("/getInviteCodePer/{num}")
    @ResponseBody
    public String getInviteCodePer(@PathVariable Integer num) throws IOException {
        ArrayList<String> inviteCode = userService.getInviteCodePer(num);
        return inviteCode.toString();
    }

    /**
     * 发送注册时的激活邮件
     */
    @PostMapping("/sendActiveEmail/{email}")
    @ResponseBody
    public CommonResp sendActiveEmail(@PathVariable String email) {
        CommonResp resp = new CommonResp<>();
        userService.sendActiveEmail(email, true, resp);
        return resp;
    }

    /**
     * 发送忘记密码时的激活邮件
     */
    @PostMapping("/sendActiveEmailForget/{email}")
    @ResponseBody
    public CommonResp sendActiveEmailForget(@PathVariable String email) {
        CommonResp resp = new CommonResp<>();
        userService.sendActiveEmail(email, false, resp);
        return resp;
    }

    /**
     * 账号登出接口
     */
    @PostMapping("/logoutByAccount")
    @ResponseBody
    public CommonResp logoutByAccount(@CookieValue(name = "yiti_userID", defaultValue = "-1", required = false) String userID, HttpServletResponse response) {
        CommonResp resp = new CommonResp<>();
        userService.logCookieLcOut(userID, response, resp);
        return resp;
    }

    /**
     * 账号登录接口
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
     * 邮箱登录接口
     */
    @PostMapping("/loginByEmail")
    @ResponseBody
    public CommonResp loginByEmail(@RequestBody UserQueryReq user, HttpServletResponse response) throws IOException {
        CommonResp resp = new CommonResp<>();
        userService.isLoginEmail(user.getEmail(), resp);
        userService.isLoginPassword(user, resp);                       // 验证密码, 如果密码有效, 根据此账号返回 user
        if (resp.getSuccess()) {
            // 登陆成功, 添加唯一登录凭证
            userService.setOnlyLoginCert(user.getId(), response);
        }

        return resp;
    }

    /**
     * 判断邀请码是否有效
     */
    @PostMapping("/isInvite/{inviteCode}")
    @ResponseBody
    public CommonResp isInvite(@PathVariable String inviteCode) {
        CommonResp resp = new CommonResp<>();
        return resp;
    }

    /**
     * 注册接口
     */
    @PostMapping("/register")
    @ResponseBody
    public CommonResp register(@RequestBody UserQueryReq user) {
        CommonResp resp = new CommonResp<>();
        userService.isRegisterPassword(user.getPassword(), resp);                 // 密码强度校验
        userService.isActiveEmail(user.getEmail(), user.getVerifyCode(), resp);   // 验证码校验
        gptInviteService.isInvite(user, resp);                                    // 邀请码校验
        if (resp.getSuccess()) {
            userService.encryptPassword(user, userService.setSalt(user));         // 设置盐值并密码加密
            userService.addUser(user, resp);
        }
        return resp;
    }

    /**
     * 忘记密码接口
     */
    @PostMapping("/forget")
    @ResponseBody
    public CommonResp forget(@RequestBody UserQueryReq user) {
        CommonResp resp = new CommonResp<>();
        userService.isRegisterPassword(user.getPassword(), resp);
        user = userService.isExitsUserEmail(user, resp);                          // 是否存在用户(by email)
        userService.isActiveEmail(user.getEmail(), user.getVerifyCode(), resp);   // 验证码校验
        if (resp.getSuccess()) {
            userService.encryptPassword(user, userService.setSalt(user));         // 设置盐值并密码加密
            userService.updateUser(user, resp);
        }
        return resp;
    }


    /**
     * 注册接口
     */
    @PostMapping("/register0")
    @ResponseBody
    public CommonResp register0(@RequestBody UserQueryReq user) {
        CommonResp resp = new CommonResp<>();
//        userService.isRegisterUserAccount(user.getUseraccount(), resp);
        userService.isRegisterPassword(user.getPassword(), resp);
        if (resp.getSuccess()) {
            userService.encryptPassword(user, userService.setSalt(user));  // 设置盐值并密码加密
            userService.addUser(user, resp);
        }
        return resp;
    }

//    /**
//     * GitHub 登录页面
//     */
//    @GetMapping("/render")
//    @ResponseBody
//    public void renderAuth(HttpServletResponse response) throws IOException {
//        AuthRequest authRequest = userService.getAuthRequest();
//        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
//    }

    /**
     * 根据 userID 查询 user 的全部信息
     *
     * @param userID 登录用户的 userID 值
     * @return 返回 CommonResp<UserQueryResp>
     */
    @PostMapping("/loginByID/{userID}")
    @ResponseBody
    public CommonResp<UserQueryResp> autoLogin(@PathVariable String userID) throws IOException {
        CommonResp<UserQueryResp> resp = new CommonResp<>();
        resp.setContent(userService.selectUserByID(Long.valueOf(userID)));   // 将 user 信息返回
        return resp;
    }

    /**
     * 检测登录凭证是否有效
     *
     * @param loginCert 本地 cookie 值
     * @param userID    本地 cookie 值
     * @return 有效则返回 userID
     */
    @PostMapping("/checkLoginCert")
    @ResponseBody
    public CommonResp<String> checkLoginCert(@CookieValue(value = "yiti_loginCert", defaultValue = "null") String loginCert,
                                             @CookieValue(value = "yiti_userID", defaultValue = "null") String userID) throws IOException {
        CommonResp<String> resp = new CommonResp<>();
        if (Objects.equals(loginCert, "null")
                || Objects.equals(userID, "null")
                || !userService.checkLoginCert(loginCert, userID)) {
            // 本地无 cookie 或 cookie 中的自动登录凭证失效
            resp.setSuccess(false);
        } else {
            resp.setContent(userID);
        }

        return resp;
    }

    /**
     * 页面管理权限验证
     */
    @PostMapping("/checkAdminPermission")
    @ResponseBody
    public CommonResp checkAdminPermission(@RequestBody AdminPermissionReq req) {
        CommonResp resp = new CommonResp<>();

        userService.checkAdminPermission(resp, req.getUserId());

        return resp;
    }

    /**
     * 增加页面管理权限
     */
    @PostMapping("/addAdminPermission")
    @ResponseBody
    public CommonResp addAdminPermission(@RequestBody AddAdminPermissionReq req) {
        CommonResp resp = new CommonResp<>();

        userService.addAdminPermission(resp, req.getUserId());

        return resp;
    }

//    /**
//     * GitHub 登录成功后的回调函数
//     */
//    @GetMapping("/github/callback")
//    public void githubCallback(AuthCallback callback, HttpServletResponse response) throws IOException {
//        AuthRequest authRequest = userService.getAuthRequest();
//        AuthResponse<AuthUser> authResponse = authRequest.login(callback);
//
//        LOG.info("code= " + authResponse.getCode());
//        if (authResponse.getCode() == 2000) {
//            Long userID = userService.signInOrUp(authResponse.getData());                 // 通过(新增/查找已经存在的)用户, 获取 userID
//            if (userID == -1L) return;
//            userService.setOnlyLoginCert(userID, response);                               // 设置唯一登录凭证
//        }
//
//        response.sendRedirect("http://165.154.36.46:8110/");
//    }

}
