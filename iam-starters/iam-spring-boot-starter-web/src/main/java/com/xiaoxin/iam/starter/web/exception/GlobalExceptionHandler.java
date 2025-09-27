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

package com.xiaoxin.iam.starter.web.exception;

import com.xiaoxin.iam.common.exception.*;
import com.xiaoxin.iam.starter.web.wrapper.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局异常处理器
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    /**
     * 处理认证异常
     */
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthException(AuthException ex) {
        log.error("认证异常: {}", ex.getMessage(), ex);
        return ResponseEntity.status(401).body(ApiResponse.unauthorized(ex.getMessage()));
    }

    /**
     * 处理权限异常
     */
    @ExceptionHandler(PermissionException.class)
    public ResponseEntity<ApiResponse<Object>> handlePermissionException(PermissionException ex) {
        log.error("权限异常: {}", ex.getMessage(), ex);
        return ResponseEntity.status(403).body(ApiResponse.forbidden(ex.getMessage()));
    }

    /**
     * 处理验证异常
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(ValidationException ex) {
        log.error("验证异常: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.badRequest(ex.getMessage()));
    }

    /**
     * 处理数据异常
     */
    @ExceptionHandler(DataException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataException(DataException ex) {
        log.error("数据异常: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.badRequest(ex.getMessage()));
    }

    /**
     * 处理系统异常
     */
    @ExceptionHandler(SystemException.class)
    public ResponseEntity<ApiResponse<Object>> handleSystemException(SystemException ex) {
        log.error("系统异常: {}", ex.getMessage(), ex);
        return ResponseEntity.status(500).body(ApiResponse.internalServerError(ex.getMessage()));
    }

    /**
     * 处理通用业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(BusinessException ex) {
        log.error("业务异常: {}", ex.getMessage(), ex);
        return ResponseEntity.status(400).body(ApiResponse.error(ex.getCode(), ex.getMessage()));
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("参数验证异常: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.badRequest("参数验证失败: " + ex.getMessage()));
    }

    /**
     * 处理缺少请求参数异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.error("缺少参数异常: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.badRequest("缺少必需参数: " + ex.getParameterName()));
    }

    /**
     * 处理参数类型错误异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.error("参数类型错误: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.badRequest("参数类型错误: " + ex.getName()));
    }

    /**
     * 处理接口不存在异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        log.error("接口不存在: {}", ex.getRequestURL(), ex);
        return ResponseEntity.status(404).body(ApiResponse.notFound("接口不存在: " + ex.getRequestURL()));
    }

    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("非法参数: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(ApiResponse.badRequest("参数错误: " + ex.getMessage()));
    }

    /**
     * 处理安全异常
     */
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ApiResponse<Object>> handleSecurityException(SecurityException ex) {
        log.error("安全异常: {}", ex.getMessage(), ex);
        return ResponseEntity.status(403).body(ApiResponse.forbidden("访问被拒绝: " + ex.getMessage()));
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException ex) {
        log.error("运行时异常: {}", ex.getMessage(), ex);
        return ResponseEntity.status(500).body(ApiResponse.internalServerError("系统错误: " + ex.getMessage()));
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        log.error("未知异常: {}", ex.getMessage(), ex);
        return ResponseEntity.status(500).body(ApiResponse.internalServerError("未知错误: " + ex.getMessage()));
    }
}
