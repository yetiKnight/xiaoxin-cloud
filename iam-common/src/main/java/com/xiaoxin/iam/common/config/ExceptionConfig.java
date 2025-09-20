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

package com.xiaoxin.iam.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 异常处理配置
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "iam.exception")
public class ExceptionConfig {

    /**
     * 是否启用异常处理
     */
    private boolean enabled = true;

    /**
     * 是否记录异常堆栈信息
     */
    private boolean logStackTrace = true;

    /**
     * 是否记录异常详情
     */
    private boolean logDetail = true;

    /**
     * 异常日志级别
     */
    private String logLevel = "WARN";

    /**
     * 是否启用异常监控
     */
    private boolean enableMonitoring = true;

    /**
     * 异常监控阈值
     */
    private int monitoringThreshold = 100;

    /**
     * 是否启用异常告警
     */
    private boolean enableAlert = false;

    /**
     * 异常告警阈值
     */
    private int alertThreshold = 1000;

    /**
     * 告警通知邮箱
     */
    private String alertEmail;

    /**
     * 告警通知手机号
     */
    private String alertPhone;

    /**
     * 是否启用异常统计
     */
    private boolean enableStatistics = true;

    /**
     * 异常统计时间窗口（分钟）
     */
    private int statisticsWindow = 60;

    /**
     * 是否启用异常缓存
     */
    private boolean enableCache = true;

    /**
     * 异常缓存过期时间（秒）
     */
    private int cacheExpireTime = 300;

    /**
     * 是否启用异常重试
     */
    private boolean enableRetry = false;

    /**
     * 异常重试次数
     */
    private int retryCount = 3;

    /**
     * 异常重试间隔（毫秒）
     */
    private long retryInterval = 1000;

    /**
     * 是否启用异常降级
     */
    private boolean enableFallback = false;

    /**
     * 异常降级策略
     */
    private String fallbackStrategy = "default";

    /**
     * 是否启用异常熔断
     */
    private boolean enableCircuitBreaker = false;

    /**
     * 熔断器失败阈值
     */
    private int circuitBreakerFailureThreshold = 10;

    /**
     * 熔断器恢复时间（秒）
     */
    private int circuitBreakerRecoveryTime = 60;

    /**
     * 是否启用异常限流
     */
    private boolean enableRateLimit = false;

    /**
     * 限流阈值（每秒）
     */
    private int rateLimitThreshold = 100;

    /**
     * 限流时间窗口（秒）
     */
    private int rateLimitWindow = 60;
}
