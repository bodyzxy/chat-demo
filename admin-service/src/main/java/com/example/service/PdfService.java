package com.example.service;

import com.example.common.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PdfService {
    BaseResponse update(MultipartFile file);
}
