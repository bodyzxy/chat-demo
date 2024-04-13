package com.example.repository;

import com.example.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser,Long> {
    AdminUser findByUsername(String username);
}