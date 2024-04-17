package com.example.service;

import com.example.common.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    BaseResponse updateFile(MultipartFile file);
}
