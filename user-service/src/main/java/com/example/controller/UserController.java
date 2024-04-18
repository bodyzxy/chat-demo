package com.example.controller;

import com.example.common.BaseResponse;
import com.example.pojo.Users;
import com.example.pojo.dto.UserDTO;
import com.example.properties.JwtProperties;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import com.example.utils.JwtUtil;
import com.example.utils.ResultUtils;
import com.example.vo.EmployeeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import cn.hutool.core.util.StrUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtProperties jwtProperties;

    @Value("${pattern.dateformat}")
    private String data;

    /**
     * 注册
     * @param userDTO
     * @return
     */
    @PostMapping("/create")
    public BaseResponse create(@RequestBody UserDTO userDTO) {
        if (StrUtil.isEmpty(userDTO.username()) && StrUtil.isEmpty(userDTO.password())){
            return ResultUtils.error(200,"姓名或密码不能为空");
        }
        return userService.create(userDTO);
    }

    /**
     * 登录
     * @param userDTO
     * @return
     */
    @PostMapping("/login")
    public BaseResponse login(@RequestBody UserDTO userDTO) {
        if (StrUtil.isEmpty(userDTO.username()) && StrUtil.isEmpty(userDTO.password())){
            return ResultUtils.error(200,"姓名或密码为空");
        }
        Users users = userService.login(userDTO);

        //登录成功生成令牌
        Map<String, Object> map = new HashMap<>();
        map.put("empId", users.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                map
        );

        EmployeeVO employeeVO = EmployeeVO.builder()
                .id(users.getId())
                .token(token)
                .userName(users.getUsername())
                .build();
        return ResultUtils.success(employeeVO);
    }

    /**
     * 查询
     * @param username
     * @return
     */
    @GetMapping("/select")
    public BaseResponse<Users> select(@RequestParam("username") String username) {
        Users user = userRepository.findByUsername(username);
        return ResultUtils.success(user);
    }

    @GetMapping("/select/{id}")
    public Users select(@PathVariable("id") Long id) {
        Users user = userRepository.getReferenceById(id);

        data = String.valueOf(new Date());
        System.out.println(data);
        return user;
    }
}
