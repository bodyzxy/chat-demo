package com.example.interceptor;

import com.example.context.BaseContext;
import com.example.properties.JwtProperties;
import com.example.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletResponse;

/**
 * Jwt拦截器
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtProperties jwtProperties;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)){
            //当前拦截不是动态方法，放行
            return true;
        }
        //1.从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getAdminTokenName());

        //2.校验令牌
        try {
            log.info("jwt校验token:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(),token);
            Long empId = Long.valueOf(claims.get("empId").toString());
            log.info("员工id,empId:{}", empId);
            BaseContext.setCurrentId(empId);//将用户id存入线程
            //3.通过
            return true;
        }catch (Exception ex){
            response.setStatus(401);
            return false;
        }
    }
}
