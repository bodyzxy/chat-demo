package com.example.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 员工登录
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeVO {
    private Long id;

    private String userName;


    private String token;
}
