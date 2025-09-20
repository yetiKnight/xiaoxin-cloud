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

package com.xiaoxin.iam.common.exception;

import com.xiaoxin.iam.common.config.ExceptionConfig;
import com.xiaoxin.iam.common.util.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 异常处理建议器
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionHandlerAdvice {

    private final ExceptionConfig exceptionConfig;

    /**
     * 异常统计缓存
     */
    private final ConcurrentHashMap<String, AtomicLong> exceptionStatistics = new ConcurrentHashMap<>();

    /**
     * 异常监控缓存
     */
    private final ConcurrentHashMap<String, AtomicLong> exceptionMonitoring = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        if (exceptionConfig.isEnabled()) {
            log.info("异常处理模块已启用");
            log.info("异常处理配置: {}", exceptionConfig);
        }
    }

    /**
     * 处理异常
     */
    public void handleException(Throwable throwable, String context) {
        if (!exceptionConfig.isEnabled()) {
            return;
        }

        String exceptionKey = getExceptionKey(throwable);
        
        // 记录异常统计
        if (exceptionConfig.isEnableStatistics()) {
            recordExceptionStatistics(exceptionKey);
        }

        // 记录异常监控
        if (exceptionConfig.isEnableMonitoring()) {
            recordExceptionMonitoring(exceptionKey);
        }

        // 记录异常日志
        recordExceptionLog(throwable, context);

        // 检查异常告警
        if (exceptionConfig.isEnableAlert()) {
            checkExceptionAlert(exceptionKey);
        }
    }

    /**
     * 获取异常键
     */
    private String getExceptionKey(Throwable throwable) {
        if (throwable == null) {
            return "null";
        }
        
        String className = throwable.getClass().getSimpleName();
        String message = ExceptionUtils.getShortMessage(throwable);
        
        return className + ":" + message;
    }

    /**
     * 记录异常统计
     */
    private void recordExceptionStatistics(String exceptionKey) {
        exceptionStatistics.computeIfAbsent(exceptionKey, k -> new AtomicLong(0)).incrementAndGet();
    }

    /**
     * 记录异常监控
     */
    private void recordExceptionMonitoring(String exceptionKey) {
        exceptionMonitoring.computeIfAbsent(exceptionKey, k -> new AtomicLong(0)).incrementAndGet();
    }

    /**
     * 记录异常日志
     */
    private void recordExceptionLog(Throwable throwable, String context) {
        if (throwable instanceof BusinessException) {
            if (exceptionConfig.isLogDetail()) {
                log.warn("业务异常 [{}]: {}", context, throwable.getMessage());
            }
        } else {
            if (exceptionConfig.isLogStackTrace()) {
                log.error("系统异常 [{}]: {}", context, throwable.getMessage(), throwable);
            } else {
                log.error("系统异常 [{}]: {}", context, throwable.getMessage());
            }
        }
    }

    /**
     * 检查异常告警
     */
    private void checkExceptionAlert(String exceptionKey) {
        AtomicLong count = exceptionMonitoring.get(exceptionKey);
        if (count != null && count.get() >= exceptionConfig.getAlertThreshold()) {
            log.warn("异常告警: {} 异常次数已达到阈值 {}", exceptionKey, count.get());
            // 这里可以发送告警通知
            sendAlert(exceptionKey, count.get());
        }
    }

    /**
     * 发送告警通知
     */
    private void sendAlert(String exceptionKey, long count) {
        // 这里可以实现具体的告警逻辑
        log.warn("发送异常告警: {} 异常次数: {}", exceptionKey, count);
    }

    /**
     * 获取异常统计信息
     */
    public ConcurrentHashMap<String, AtomicLong> getExceptionStatistics() {
        return exceptionStatistics;
    }

    /**
     * 获取异常监控信息
     */
    public ConcurrentHashMap<String, AtomicLong> getExceptionMonitoring() {
        return exceptionMonitoring;
    }

    /**
     * 清空异常统计
     */
    public void clearExceptionStatistics() {
        exceptionStatistics.clear();
    }

    /**
     * 清空异常监控
     */
    public void clearExceptionMonitoring() {
        exceptionMonitoring.clear();
    }

    /**
     * 获取异常统计总数
     */
    public long getTotalExceptionCount() {
        return exceptionStatistics.values().stream()
                .mapToLong(AtomicLong::get)
                .sum();
    }

    /**
     * 获取异常监控总数
     */
    public long getTotalMonitoringCount() {
        return exceptionMonitoring.values().stream()
                .mapToLong(AtomicLong::get)
                .sum();
    }
}
