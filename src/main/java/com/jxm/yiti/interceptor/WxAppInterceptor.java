package com.jxm.yiti.interceptor;

import com.alibaba.fastjson2.JSONObject;
import com.jxm.yiti.annotation.AccessLimit;
import com.jxm.yiti.enums.WxUserConst;
import com.jxm.yiti.utils.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;

@Slf4j
@Component
public class WxAppInterceptor implements HandlerInterceptor {

    @Value("${wxApp.login.secret}")
    private String loginSecret;

    // 方便业务层直接在 WxAppInterceptor 就拿到用户的 user_id
    private static final ThreadLocal<Integer> wxUserIdTL = new ThreadLocal<>();
    private static final ThreadLocal<WxUserConst> wxUserTypeTL = new ThreadLocal<>();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (ifNoLogin(request, response)) return false;

        if (ifNoAccess(request, response, handler)) return false;

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    public static Integer getWxUserId() {
        return wxUserIdTL.get();
    }

    public static WxUserConst getWxUserType() {
        return wxUserTypeTL.get();
    }

    /**
     * 是否是未登录
     */
    private boolean ifNoLogin(HttpServletRequest request, HttpServletResponse response) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || authorization.length() < 7) {
            log.warn("auth_token 验证失败");
            response.setStatus(401);
            return true;
        }
        String authToken = authorization.substring(7);

        boolean ifExpired = TokenUtil.checkIfExpired(authToken, loginSecret);
        if (!ifExpired) {
            log.warn("auth_token 错误或失效");
            response.setStatus(401);
            return true;
        }
        JSONObject jsonObject = JSONObject.parseObject(TokenUtil.decryptToken(authToken, loginSecret));
        if (jsonObject == null) return true;
//        log.info("decryptToken: {}", jsonObject.toString());
        wxUserIdTL.set(Integer.valueOf(jsonObject.getString("user_id")));
        wxUserTypeTL.set(WxUserConst.of(Integer.parseInt(jsonObject.getString("user_type"))));

        return false;
    }

    /**
     * 是否没有权限
     */
    private boolean ifNoAccess(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        log.info(handler.toString());
//        if (!(handler instanceof HandlerMethod)) {
//            return false;
//        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 在对登录拦截的所有接口中, 检查是否含有用户权限访问的限制
        if (method.isAnnotationPresent(AccessLimit.class)) {
            AccessLimit accessLimit = method.getAnnotation(AccessLimit.class);
            WxUserConst[] wxUserConsts = accessLimit.type();

            log.info("user_type == {}", getWxUserType());
            if (!ifWxUserTypeExists(wxUserConsts, getWxUserType())) {
                response.setStatus(402);
                log.warn("没有权限");
                return true;
            }
        }

        return false;
    }

    /**
     * 判断当前用户类型是否在接口指定能够访问的用户类型中
     */
    private boolean ifWxUserTypeExists(WxUserConst[] wxUserConsts, WxUserConst value) {
        for (WxUserConst wxUserConst : wxUserConsts) {
            if (wxUserConst == value) {      // == 就足够
                return true;
            }
        }

        return false;
    }
}
