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

package com.xiaoxin.iam.starter.web.interceptor;

import com.xiaoxin.iam.starter.web.config.WebProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 安全拦截器
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityInterceptor implements HandlerInterceptor {

    private final WebProperties webProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 添加安全头
        addSecurityHeaders(request, response);
        
        // 检查请求来源
        validateRequestOrigin(request);
        
        return true;
    }

    /**
     * 添加安全头
     */
    private void addSecurityHeaders(HttpServletRequest request, HttpServletResponse response) {
        // 防止XSS攻击
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-Frame-Options", "DENY");
        response.setHeader("X-XSS-Protection", "1; mode=block");
        
        // 内容安全策略
        response.setHeader("Content-Security-Policy", "default-src 'self'");
        
        // 严格传输安全
        if (request.isSecure()) {
            response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        }
        
        // 引用策略
        response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        
        log.debug("安全头已添加到响应中");
    }

    /**
     * 验证请求来源
     */
    private void validateRequestOrigin(HttpServletRequest request) {
        String origin = request.getHeader("Origin");
        String referer = request.getHeader("Referer");
        
        // 记录可疑请求
        if (origin != null && !isValidOrigin(origin)) {
            log.warn("可疑的请求来源: {}", origin);
        }
        
        if (referer != null && !isValidReferer(referer)) {
            log.warn("可疑的引用页面: {}", referer);
        }
    }

    /**
     * 验证Origin头
     */
    private boolean isValidOrigin(String origin) {
        // 这里可以添加更复杂的验证逻辑
        return !origin.contains("malicious-site.com");
    }

    /**
     * 验证Referer头
     */
    private boolean isValidReferer(String referer) {
        // 这里可以添加更复杂的验证逻辑
        return !referer.contains("malicious-site.com");
    }
}
