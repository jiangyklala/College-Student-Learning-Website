package com.jxm.yiti.interceptor;

import com.jxm.yiti.utils.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class WxAppInterceptor implements HandlerInterceptor {

    @Value("${wxApp.login.secret}")
    private String loginSecret;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String authorization = request.getHeader("Authorization");
        if (authorization == null || authorization.length() < 7) {
            log.warn("auth_token 验证失败");
            response.setStatus(401);
            return false;
        }
        String authToken = authorization.substring(7);

        boolean ifExpired = TokenUtil.checkIfExpired(authToken, loginSecret);

        log.info("interceptor = {}", ifExpired);

        return ifExpired;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
