package com.example.service;

import com.example.common.BaseResponse;
import com.example.pojo.Users;
import com.example.pojo.dto.UserDTO;

public interface UserService {
    BaseResponse create(UserDTO userDTO);

    Users login(UserDTO userDTO);
}
