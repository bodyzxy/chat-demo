package com.example.controller;


import com.example.common.BaseResponse;
import com.example.pojo.AiImage;
import com.example.service.ChatService;
import com.example.utils.ResultUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    /**
     * 通过pdf聊天
     * @param message
     * @return
     */
    @GetMapping("/pdf")
    public BaseResponse chat(@RequestParam("message") String message){
        String result = chatService.chatByPdf(message);
        return ResultUtils.success(result);
    }

    /**
     * 生成图片
     * @param aiImage
     * @return
     */
    @PostMapping("/image")
    public BaseResponse aiImage(@RequestBody AiImage aiImage){
        return chatService.aiImage(aiImage);
    }
}
