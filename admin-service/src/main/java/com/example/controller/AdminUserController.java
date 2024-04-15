package com.example.controller;


import com.example.clients.UserClients;
import com.example.common.BaseResponse;
import com.example.entity.AdminUser;
import com.example.entity.dto.AdminUserDTO;
import com.example.pojo.Users;
import com.example.repository.AdminUserRepository;
import com.example.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminUserController {
    @Autowired
    private AdminUserRepository adminUserRepository;
    @Autowired
    private UserClients userClients;

    @PostMapping("/create")
    public BaseResponse create(@RequestBody AdminUserDTO adminUserDTO){
        AdminUser adminUser = AdminUser.builder()
                .username(adminUserDTO.getUsername())
                .password(adminUserDTO.getPassword())
                .userId(adminUserDTO.getUserId())
                .build();
        adminUserRepository.save(adminUser);
        return ResultUtils.success(adminUser);
    }

    @GetMapping("/select")
    public BaseResponse select(@RequestParam("username") String username){
        AdminUser adminUser = adminUserRepository.findByUsername(username);

        Users users = userClients.select(adminUser.getUserId());
        adminUser.setUsers(users);
        return ResultUtils.success(adminUser);

    }
}
