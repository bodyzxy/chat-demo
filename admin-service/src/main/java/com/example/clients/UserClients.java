package com.example.clients;

import com.example.config.FeignClientConfiguration;
import com.example.pojo.Users;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "user-service",configuration = FeignClientConfiguration.class)
public interface UserClients {
    @GetMapping("/user/select/{id}")
    Users select(@PathVariable("id") Long id);
}
