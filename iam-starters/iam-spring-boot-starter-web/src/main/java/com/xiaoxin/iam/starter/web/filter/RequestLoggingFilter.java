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

package com.xiaoxin.iam.starter.web.filter;

import com.xiaoxin.iam.starter.web.config.WebProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求日志过滤器
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "iam.web.request-log", name = "enabled", havingValue = "true", matchIfMissing = true)
public class RequestLoggingFilter extends OncePerRequestFilter implements Ordered {

    private final WebProperties webProperties;

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        // 排除静态资源和健康检查
        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        long startTime = System.currentTimeMillis();
        long startMemory = getMemoryUsage();
        
        // 生成请求ID
        String requestId = generateRequestId(request);
        if (webProperties.getRequestLog().isEnableRequestId()) {
            response.setHeader(webProperties.getRequestLog().getRequestIdHeaderName(), requestId);
        }
        
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        
        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            long endMemory = getMemoryUsage();
            logRequest(requestWrapper, responseWrapper, duration, startMemory, endMemory, requestId);
            responseWrapper.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request, 
                          ContentCachingResponseWrapper response, 
                          long duration, long startMemory, long endMemory, String requestId) {
        
        WebProperties.RequestLog requestLog = webProperties.getRequestLog();
        
        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append("HTTP请求 - ");
        logBuilder.append(request.getMethod()).append(" ");
        logBuilder.append(request.getRequestURI());
        
        if (request.getQueryString() != null) {
            logBuilder.append("?").append(request.getQueryString());
        }
        
        logBuilder.append(" - 状态: ").append(response.getStatus());
        
        // 记录响应时间
        if (requestLog.isIncludeResponseTime()) {
            logBuilder.append(", 耗时: ").append(duration).append("ms");
        }
        
        // 记录客户端IP
        if (requestLog.isIncludeIpAddress()) {
            logBuilder.append(", 客户端: ").append(getClientIpAddress(request));
        }
        
        // 记录请求ID
        if (requestLog.isEnableRequestId()) {
            logBuilder.append(", 请求ID: ").append(requestId);
        }
        
        // 记录用户代理
        if (requestLog.isIncludeUserAgent()) {
            String userAgent = request.getHeader("User-Agent");
            if (userAgent != null) {
                logBuilder.append(", 用户代理: ").append(userAgent);
            }
        }
        
        // 记录引用页面
        if (requestLog.isIncludeReferer()) {
            String referer = request.getHeader("Referer");
            if (referer != null) {
                logBuilder.append(", 引用页面: ").append(referer);
            }
        }
        
        // 记录请求头
        if (requestLog.isIncludeHeaders()) {
            Map<String, String> headers = getHeaders(request);
            if (!headers.isEmpty()) {
                logBuilder.append(", 请求头: ").append(headers);
            }
        }
        
        // 记录请求体
        if (requestLog.isIncludeRequestPayload()) {
            String requestBody = getRequestBody(request, requestLog.getMaxPayloadLength());
            if (!requestBody.isEmpty()) {
                logBuilder.append(", 请求体: ").append(requestBody);
            }
        }
        
        // 记录响应体
        if (requestLog.isIncludeResponsePayload()) {
            String responseBody = getResponseBody(response, requestLog.getMaxPayloadLength());
            if (!responseBody.isEmpty()) {
                logBuilder.append(", 响应体: ").append(responseBody);
            }
        }
        
        // 记录内存使用
        if (requestLog.isIncludeMemoryUsage()) {
            long memoryUsed = endMemory - startMemory;
            logBuilder.append(", 内存使用: ").append(memoryUsed).append(" bytes");
        }
        
        // 记录线程信息
        if (requestLog.isIncludeThreadInfo()) {
            logBuilder.append(", 线程: ").append(Thread.currentThread().getName());
        }
        
        // 记录用户信息
        if (requestLog.isIncludeUserInfo()) {
            String user = request.getRemoteUser();
            if (user != null) {
                logBuilder.append(", 用户: ").append(user);
            }
        }
        
        // 记录会话信息
        if (requestLog.isIncludeSessionInfo()) {
            String sessionId = request.getSession(false) != null ? request.getSession().getId() : "无会话";
            logBuilder.append(", 会话: ").append(sessionId);
        }
        
        // 根据配置决定日志级别
        String logLevel = requestLog.getLogLevel().toUpperCase();
        boolean isError = response.getStatus() >= 400;
        boolean isSlow = duration > requestLog.getSlowRequestThreshold();
        
        if (isError && requestLog.isLogErrorRequests()) {
            log.error(logBuilder.toString());
        } else if (isSlow && requestLog.isLogSlowRequests()) {
            log.warn(logBuilder.toString());
        } else {
            switch (logLevel) {
                case "DEBUG":
                    log.debug(logBuilder.toString());
                    break;
                case "WARN":
                    log.warn(logBuilder.toString());
                    break;
                case "ERROR":
                    log.error(logBuilder.toString());
                    break;
                default:
                    log.info(logBuilder.toString());
                    break;
            }
        }
    }

    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            // 排除敏感头信息
            if (!isSensitiveHeader(headerName)) {
                headers.put(headerName, request.getHeader(headerName));
            }
        }
        return headers;
    }

    private String getRequestBody(ContentCachingRequestWrapper request, int maxLength) {
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            String body = new String(content, StandardCharsets.UTF_8);
            return body.length() > maxLength ? body.substring(0, maxLength) + "..." : body;
        }
        return "";
    }

    private String getResponseBody(ContentCachingResponseWrapper response, int maxLength) {
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            String body = new String(content, StandardCharsets.UTF_8);
            return body.length() > maxLength ? body.substring(0, maxLength) + "..." : body;
        }
        return "";
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }

    private boolean isSensitiveHeader(String headerName) {
        String lowerCaseName = headerName.toLowerCase();
        return lowerCaseName.contains("password") || 
               lowerCaseName.contains("token") || 
               lowerCaseName.contains("authorization") ||
               lowerCaseName.contains("cookie");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        WebProperties.RequestLog requestLog = webProperties.getRequestLog();
        
        // 检查排除路径
        for (String pattern : requestLog.getExcludePathPatterns()) {
            if (uri.matches(pattern.replace("**", ".*"))) {
                return true;
            }
        }
        
        // 检查包含路径
        boolean shouldInclude = false;
        for (String pattern : requestLog.getIncludePathPatterns()) {
            if (uri.matches(pattern.replace("**", ".*"))) {
                shouldInclude = true;
                break;
            }
        }
        
        return !shouldInclude;
    }

    /**
     * 生成请求ID
     */
    private String generateRequestId(HttpServletRequest request) {
        String existingId = request.getHeader(webProperties.getRequestLog().getRequestIdHeaderName());
        if (existingId != null && !existingId.isEmpty()) {
            return existingId;
        }
        return java.util.UUID.randomUUID().toString();
    }

    /**
     * 获取内存使用量
     */
    private long getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
}
