package com.example.controller;

import com.example.common.BaseResponse;
import com.example.pojo.Users;
import com.example.pojo.dto.UserDTO;
import com.example.repository.UserRepository;
import com.example.utils.ResultUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@Slf4j
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @Value("${pattern.dateformat}")
    private String data;

    @PostMapping("/create")
    public String create(@RequestBody UserDTO userDTO) {
        Users user1 = Users.builder()
                .username(userDTO.username())
                .password(userDTO.password())
                .createTime(new Date(System.currentTimeMillis()))
                .build();
        userRepository.save(user1);
        return user1.toString();
    }

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
