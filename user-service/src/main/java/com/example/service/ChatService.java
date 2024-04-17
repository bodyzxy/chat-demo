package com.example.service;

import com.example.common.BaseResponse;
import com.example.pojo.AiImage;

public interface ChatService {
    String chatByPdf(String message);

    BaseResponse aiImage(AiImage aiImage);
}
