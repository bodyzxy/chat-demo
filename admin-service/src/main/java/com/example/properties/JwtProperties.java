package com.example.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sky.jwt")
@Data
public class JwtProperties {

//    @Value("${sky.jwt.admin-secret-key}")
    private String adminSecretKey;

//    @Value("${sky.jwt.admin-ttl}")
    private long adminTtl;

//    @Value("${sky.jwt.admin-token-name}")
    private String adminTokenName;
}
