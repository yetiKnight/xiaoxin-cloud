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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 任务执行器配置
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(IamCoreProperties.class)
@ConditionalOnProperty(prefix = "iam.core.task", name = "enable-async", havingValue = "true", matchIfMissing = true)
@EnableAsync
@EnableScheduling
public class TaskExecutorConfig {

    private final IamCoreProperties iamCoreProperties;

    /**
     * 异步任务执行器
     */
    @Bean("taskExecutor")
    @ConditionalOnMissingBean(name = "taskExecutor")
    public Executor taskExecutor() {
        IamCoreProperties.ThreadPool threadPool = iamCoreProperties.getThreadPool();
        
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadPool.getCoreSize());
        executor.setMaxPoolSize(threadPool.getMaxSize());
        executor.setQueueCapacity(threadPool.getQueueCapacity());
        executor.setKeepAliveSeconds((int) threadPool.getKeepAliveTime().getSeconds());
        executor.setThreadNamePrefix(threadPool.getThreadNamePrefix());
        executor.setWaitForTasksToCompleteOnShutdown(threadPool.isWaitForTasksToCompleteOnShutdown());
        executor.setAwaitTerminationSeconds((int) threadPool.getAwaitTerminationSeconds().getSeconds());
        
        // 拒绝策略：调用者运行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        
        executor.initialize();
        
        log.info("IAM异步任务执行器配置完成: coreSize={}, maxSize={}, queueCapacity={}", 
                threadPool.getCoreSize(), threadPool.getMaxSize(), threadPool.getQueueCapacity());
        
        return executor;
    }
}
