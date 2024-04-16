package com.example.service.impl;

import com.example.dto.EmployeeDTO;
import com.example.pojo.AdminUser;
import com.example.repository.AdminUserRepository;
import com.example.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.nio.channels.AcceptPendingException;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    @Autowired
    private AdminUserRepository adminUserRepository;

    @Override
    public AdminUser login(EmployeeDTO employeeDTO) {
        String username = employeeDTO.getUsername();
        String password = employeeDTO.getPassword();

        AdminUser adminUser = adminUserRepository.findByUsername(username);
        if (adminUser == null) {
            log.info("账号不存在");
            return null;
        }
        if (!password.equals(adminUser.getPassword())) {
            System.out.println("密码错误");
            return null;
        }

        return adminUser;
    }
}
