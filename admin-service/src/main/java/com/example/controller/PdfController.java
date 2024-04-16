package com.example.controller;

import com.example.common.BaseResponse;
import com.example.service.PdfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("/pdf")
public class PdfController {

    @Autowired
    private PdfService pdfService;

    /**
     * 上传pdf
     * @param file
     */
    @PostMapping("/update")
    public void updatePdf(@RequestParam MultipartFile file) {
        pdfService.update(file);
    }
}
