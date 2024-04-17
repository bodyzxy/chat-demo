package com.example.service;

import com.example.common.BaseResponse;
import com.example.pojo.AiImage;
import org.springframework.web.multipart.MultipartFile;

public interface ChatService {
    String chatByPdf(String message);

    BaseResponse aiImage(AiImage aiImage);

    BaseResponse chatVoid(MultipartFile file);
}
