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

package com.xiaoxin.iam.starter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * IAM平台核心配置属性
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "iam.core")
public class IamCoreProperties {

    /**
     * 是否启用操作日志
     */
    private boolean enableOperationLog = true;

    /**
     * 是否启用性能监控
     */
    private boolean enablePerformanceMonitor = true;

    /**
     * 是否启用请求追踪
     */
    private boolean enableRequestTrace = true;

    /**
     * 线程池配置
     */
    private ThreadPool threadPool = new ThreadPool();

    /**
     * 任务配置
     */
    private Task task = new Task();

    /**
     * 线程池配置类
     */
    @Data
    public static class ThreadPool {
        /**
         * 核心线程数
         */
        private int coreSize = 8;

        /**
         * 最大线程数
         */
        private int maxSize = 20;

        /**
         * 队列容量
         */
        private int queueCapacity = 1000;

        /**
         * 线程存活时间
         */
        private Duration keepAliveTime = Duration.ofSeconds(60);

        /**
         * 线程名称前缀
         */
        private String threadNamePrefix = "iam-task-";

        /**
         * 是否等待任务完成
         */
        private boolean waitForTasksToCompleteOnShutdown = true;

        /**
         * 等待终止时间
         */
        private Duration awaitTerminationSeconds = Duration.ofSeconds(60);
    }

    /**
     * 任务配置类
     */
    @Data
    public static class Task {
        /**
         * 是否启用异步任务
         */
        private boolean enableAsync = true;

        /**
         * 是否启用定时任务
         */
        private boolean enableScheduling = true;
    }
}
