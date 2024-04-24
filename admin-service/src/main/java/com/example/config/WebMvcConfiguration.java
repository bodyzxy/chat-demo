package com.example.config;

import com.example.interceptor.JwtTokenAdminInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器
 */
@Configuration
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册拦截器.....");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .addPathPatterns("/pdf/**")
                .excludePathPatterns("/admin/login")
        ;
    }
}
