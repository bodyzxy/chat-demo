package com.example.common;

import com.example.utils.ResultUtils;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * 自定义的错误处理器
 */
public class CustomResponseErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return false;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        // 处理HTTP响应的错误
        System.out.println("Handling error with status code: " + response.getStatusCode());
        ResultUtils.error(400,response.getStatusText());
    }
}
