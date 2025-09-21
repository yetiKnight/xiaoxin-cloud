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

package com.xiaoxin.iam.starter.autoconfigure;

import com.xiaoxin.iam.starter.aspect.OperationLogAspect;
import com.xiaoxin.iam.starter.aspect.PerformanceMonitorAspect;
import com.xiaoxin.iam.starter.config.IamCoreProperties;
import com.xiaoxin.iam.starter.config.IamProperties;
import com.xiaoxin.iam.starter.config.TaskExecutorConfig;
import com.xiaoxin.iam.starter.web.GlobalExceptionHandler;
import com.xiaoxin.iam.starter.web.IamResponseBodyAdvice;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * IAM平台自动配置
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(IamProperties.class)
@EnableConfigurationProperties({IamProperties.class, IamCoreProperties.class})
@Import(TaskExecutorConfig.class)
public class IamAutoConfiguration {

    /**
     * 全局异常处理器
     */
    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    /**
     * 统一响应体处理器
     */
    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnMissingBean
    public IamResponseBodyAdvice responseBodyAdvice() {
        return new IamResponseBodyAdvice();
    }

    /**
     * 操作日志切面
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "iam.core", name = "enable-operation-log", havingValue = "true", matchIfMissing = true)
    public OperationLogAspect operationLogAspect() {
        return new OperationLogAspect();
    }

    /**
     * 性能监控切面
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "iam.core", name = "enable-performance-monitor", havingValue = "true", matchIfMissing = true)
    public PerformanceMonitorAspect performanceMonitorAspect() {
        return new PerformanceMonitorAspect();
    }

}
