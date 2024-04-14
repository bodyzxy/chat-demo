package com.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 拉取nacos的配置自动更新（热部署）
 */
@Data
@Component
@ConfigurationProperties(prefix = "pattern") //nacos上面的名字一样
public class PatternProperties {
    private String dateformat;
}
