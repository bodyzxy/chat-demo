package com.example.service.impl;

import com.example.common.BaseResponse;
import com.example.pojo.Users;
import com.example.pojo.dto.UserDTO;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import com.example.utils.ResultUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public BaseResponse create(UserDTO userDTO) {
        Users user1 = Users.builder()
                .username(userDTO.username())
                .password(userDTO.password())
                .createTime(new Date(System.currentTimeMillis()))
                .build();
        userRepository.save(user1);
        return ResultUtils.success("成功");
    }

    @Override
    public Users login(UserDTO userDTO) {
        Users users = userRepository.findByUsername(userDTO.username());
        String password = userDTO.password();
        if (users == null){
            return null;
        }
        if(!password.equals(users.getPassword())){
            return null;
        }
        return users;
    }
}
