package com.example.controller;

import com.example.common.BaseResponse;
import com.example.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 添加文件
 */
@RestController
@RequestMapping("/admin/file")
@Slf4j
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/update")
    public BaseResponse updateFile(@RequestParam("file") MultipartFile file) {
        return fileService.updateFile(file);
    }
}
