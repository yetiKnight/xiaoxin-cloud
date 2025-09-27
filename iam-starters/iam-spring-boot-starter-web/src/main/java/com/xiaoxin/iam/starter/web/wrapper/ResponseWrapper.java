/*
 * Copyright 2024 xiaoxin. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xiaoxin.iam.starter.web.wrapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * 响应包装器
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ResponseWrapper {

    private final ObjectMapper objectMapper;

    /**
     * 包装成功响应
     */
    public void writeSuccessResponse(HttpServletResponse response, Object data) throws IOException {
        ApiResponse<Object> apiResponse = ApiResponse.success(data);
        writeResponse(response, apiResponse);
    }

    /**
     * 包装错误响应
     */
    public void writeErrorResponse(HttpServletResponse response, int code, String message) throws IOException {
        ApiResponse<Object> apiResponse = ApiResponse.error(code, message);
        writeResponse(response, apiResponse);
    }

    /**
     * 包装错误响应
     */
    public void writeErrorResponse(HttpServletResponse response, String message) throws IOException {
        ApiResponse<Object> apiResponse = ApiResponse.error(message);
        writeResponse(response, apiResponse);
    }

    /**
     * 写入响应
     */
    public void writeResponse(HttpServletResponse response, ApiResponse<Object> apiResponse) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(apiResponse.getCode());
        
        String jsonResponse = objectMapper.writeValueAsString(apiResponse);
        
        try (PrintWriter writer = response.getWriter()) {
            writer.write(jsonResponse);
            writer.flush();
        }
        
        log.debug("响应已写入: {}", jsonResponse);
    }
}
