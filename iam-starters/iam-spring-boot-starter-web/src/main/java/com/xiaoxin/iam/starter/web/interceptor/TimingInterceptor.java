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
 * 性能监控拦截器
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TimingInterceptor implements HandlerInterceptor {

    private final WebProperties webProperties;
    private static final String START_TIME_ATTRIBUTE = "startTime";
    private static final String START_MEMORY_ATTRIBUTE = "startMemory";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long startTime = System.currentTimeMillis();
        long startMemory = getMemoryUsage();
        
        request.setAttribute(START_TIME_ATTRIBUTE, startTime);
        request.setAttribute(START_MEMORY_ATTRIBUTE, startMemory);
        
        log.debug("请求开始时间: {}, 内存使用: {} bytes", startTime, startMemory);
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Long startTime = (Long) request.getAttribute(START_TIME_ATTRIBUTE);
        Long startMemory = (Long) request.getAttribute(START_MEMORY_ATTRIBUTE);
        
        if (startTime != null) {
            long duration = System.currentTimeMillis() - startTime;
            long endMemory = getMemoryUsage();
            long memoryUsed = startMemory != null ? endMemory - startMemory : 0;
            
            // 记录性能指标
            if (webProperties.getRequestLog().isLogPerformanceMetrics()) {
                logPerformanceMetrics(request, response, duration, memoryUsed);
            }
            
            // 检查慢请求
            if (duration > webProperties.getRequestLog().getSlowRequestThreshold()) {
                log.warn("慢请求检测: {} {} - 耗时: {}ms, 内存使用: {} bytes", 
                        request.getMethod(), request.getRequestURI(), duration, memoryUsed);
            }
        }
    }

    /**
     * 记录性能指标
     */
    private void logPerformanceMetrics(HttpServletRequest request, HttpServletResponse response, 
                                     long duration, long memoryUsed) {
        StringBuilder metrics = new StringBuilder();
        metrics.append("性能指标 - ");
        metrics.append(request.getMethod()).append(" ");
        metrics.append(request.getRequestURI());
        metrics.append(" - 状态: ").append(response.getStatus());
        metrics.append(", 耗时: ").append(duration).append("ms");
        
        if (webProperties.getRequestLog().isIncludeMemoryUsage()) {
            metrics.append(", 内存使用: ").append(memoryUsed).append(" bytes");
        }
        
        if (webProperties.getRequestLog().isIncludeThreadInfo()) {
            metrics.append(", 线程: ").append(Thread.currentThread().getName());
        }
        
        log.info(metrics.toString());
    }

    /**
     * 获取内存使用量
     */
    private long getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
}
