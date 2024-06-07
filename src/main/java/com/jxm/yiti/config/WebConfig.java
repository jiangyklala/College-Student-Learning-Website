package com.jxm.yiti.config;

import com.jxm.yiti.interceptor.WxAppInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    WxAppInterceptor wxAppInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(wxAppInterceptor)
                .addPathPatterns("/wxUser/**",
                        "/wxCollect/**",
                        "/wxQuestion/**",
                        "/wxSpecial/**",
                        "/wxInvite/**")
                .excludePathPatterns("/wxUser/login",
                        "/wxUser/getNSVCDKey/**",
                        "/wxUser/switchUserType/**",
                        "/wxUser/notifyAfter/**",
                        "/wxUser/searchLimits/**",
                        "/wxUser/searchLimitsSubmit/**",
                        "/wxUser/deleteAllUser/**",
                        "/wxQuestion/save",
                        "/wxQuestion/selectAllAdmin",
                        "/wxQuestion/delete/**",
                        "/wxQuestion/selectAnswerAdmin",
                        "/wxSpecial/selectAllAdmin",
                        "/wxSpecial/save");
    }
}
