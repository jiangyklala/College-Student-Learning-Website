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
                        "/wxQuestion/**")
                .excludePathPatterns("/wxUser/login",
                        "/wxQuestion/save",
                        "/wxQuestion/selectAllAdmin",
                        "/wxQuestion/delete/**",
                        "/wxQuestion/selectAnswer");
    }
}
