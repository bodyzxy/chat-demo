package com.example.controller;


import com.example.common.BaseResponse;
import com.example.service.ChatService;
import com.example.utils.ResultUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public BaseResponse chat(@RequestParam String message){
        String result = chatService.chatByPdf(message);
        return ResultUtils.success(result);
    }
}
