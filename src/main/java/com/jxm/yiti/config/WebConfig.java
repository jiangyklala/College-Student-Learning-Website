package com.jxm.yiti.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.jxm.yiti.interceptor.WxAppInterceptor;

import jakarta.annotation.Resource;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    WxAppInterceptor wxAppInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(wxAppInterceptor)
                .addPathPatterns("/wxUser/**",
                        "/wxCollect/**",
                        "/wxQuestion/**")
                .excludePathPatterns("/wxUser/login",
                        "/wxUser/getNSVCDKey/**",
                        "/wxUser/switchUserType/**",
                        "/wxQuestion/save",
                        "/wxQuestion/selectAllAdmin",
                        "/wxQuestion/delete/**",
                        "/wxQuestion/selectAnswerAdmin");
    }
}
