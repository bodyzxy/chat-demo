package com.example.config;

import com.example.interceptor.JwtTokenAdminInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 拦截器
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册拦截器.....");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/create","/user/login")
                .excludePathPatterns("/user/select","/user/select/**")
        ;
    }
}
