package com.example.service;

import com.example.dto.EmployeeDTO;
import com.example.pojo.AdminUser;

public interface EmployeeService {
    AdminUser login(EmployeeDTO employeeDTO);
}
