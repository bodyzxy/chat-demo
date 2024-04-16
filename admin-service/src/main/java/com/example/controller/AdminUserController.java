package com.example.controller;


import com.example.clients.UserClients;
import com.example.common.BaseResponse;
import com.example.dto.AdminUserDTO;
import com.example.dto.EmployeeDTO;
import com.example.properties.JwtProperties;
import com.example.utils.JwtUtil;
import com.example.vo.EmployeeVO;
import com.example.pojo.AdminUser;
import com.example.pojo.Users;
import com.example.repository.AdminUserRepository;
import com.example.service.EmployeeService;
import com.example.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminUserController {
    @Autowired
    private AdminUserRepository adminUserRepository;
    @Autowired
    private UserClients userClients;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

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

    /**
     * 登录JWT校验
     * @param employeeDTO
     * @return
     */
    @PostMapping("/login")
    public BaseResponse login(@RequestBody EmployeeDTO employeeDTO){
        log.info("员工登录: {}", employeeDTO);

        AdminUser adminUser = employeeService.login(employeeDTO);

        //登录成功，生成Jwt令牌
        Map<String,Object> claims = new HashMap<>();
        claims.put("empId", adminUser.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims
        );

        EmployeeVO employeeVO = EmployeeVO.builder()
                .id(adminUser.getId())
                .userName(adminUser.getUsername())
                .token(token)
                .build();

        return ResultUtils.success(employeeVO);
    }
}
