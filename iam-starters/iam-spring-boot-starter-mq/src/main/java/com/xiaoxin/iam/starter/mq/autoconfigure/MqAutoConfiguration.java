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

package com.xiaoxin.iam.starter.mq.autoconfigure;

import com.xiaoxin.iam.starter.mq.config.MqProperties;
import com.xiaoxin.iam.starter.mq.core.*;
import com.xiaoxin.iam.starter.mq.event.EventPublisher;
import com.xiaoxin.iam.starter.mq.event.EventPublisherImpl;
import com.xiaoxin.iam.starter.mq.monitor.MqMetricsCollector;
import com.xiaoxin.iam.starter.mq.monitor.MqMonitorService;
import com.xiaoxin.iam.starter.mq.serialization.MessageSerializer;
import com.xiaoxin.iam.starter.mq.serialization.JsonMessageSerializer;
import com.xiaoxin.iam.starter.mq.trace.MessageTraceService;
import com.xiaoxin.iam.starter.mq.trace.MessageTraceServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 消息队列自动配置类
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Configuration
@ConditionalOnClass(RocketMQTemplate.class)
@ConditionalOnProperty(prefix = "iam.mq", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(MqProperties.class)
public class MqAutoConfiguration {

    /**
     * 消息序列化器
     */
    @Bean
    @ConditionalOnMissingBean
    public MessageSerializer messageSerializer(MqProperties properties) {
        log.info("Configuring MessageSerializer with type: {}", 
                properties.getSerialization().getType());
        return new JsonMessageSerializer(properties.getSerialization());
    }

    /**
     * 消息发送器
     */
    @Bean
    @ConditionalOnMissingBean
    public MessageSender messageSender(RocketMQTemplate rocketMQTemplate,
                                       MessageSerializer serializer,
                                       MqProperties properties) {
        log.info("Configuring MessageSender");
        return new RocketMqMessageSender(rocketMQTemplate, serializer, properties);
    }

    /**
     * 事件发布器
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "iam.mq.event", name = "enabled", havingValue = "true", matchIfMissing = true)
    public EventPublisher eventPublisher(MessageSender messageSender,
                                          MqProperties properties) {
        log.info("Configuring EventPublisher");
        return new EventPublisherImpl(messageSender, properties);
    }

    /**
     * 事件异步执行器
     */
    @Bean("eventTaskExecutor")
    @ConditionalOnProperty(prefix = "iam.mq.event", name = "enable-async", havingValue = "true", matchIfMissing = true)
    public TaskExecutor eventTaskExecutor(MqProperties properties) {
        MqProperties.Event.ThreadPool threadPool = properties.getEvent().getThreadPool();
        
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadPool.getCoreSize());
        executor.setMaxPoolSize(threadPool.getMaxSize());
        executor.setQueueCapacity(threadPool.getQueueCapacity());
        executor.setKeepAliveSeconds((int) threadPool.getKeepAlive().getSeconds());
        executor.setThreadNamePrefix(threadPool.getThreadNamePrefix());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        
        log.info("Configured event task executor with core size: {}, max size: {}", 
                threadPool.getCoreSize(), threadPool.getMaxSize());
        
        return executor;
    }

    /**
     * 死信队列处理器
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "iam.mq.dead-letter", name = "enabled", havingValue = "true", matchIfMissing = true)
    public DeadLetterProcessor deadLetterProcessor(MessageSender messageSender,
                                                   MqProperties properties) {
        log.info("Configuring DeadLetterProcessor");
        return new DeadLetterProcessor(messageSender, properties);
    }

    /**
     * 消息追踪服务
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "iam.mq.trace", name = "enabled", havingValue = "true", matchIfMissing = true)
    public MessageTraceService messageTraceService(MqProperties properties) {
        log.info("Configuring MessageTraceService");
        return new MessageTraceServiceImpl(properties);
    }

    /**
     * 消息监控服务
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "iam.mq.monitor", name = "enabled", havingValue = "true", matchIfMissing = true)
    public MqMonitorService mqMonitorService(MqProperties properties) {
        log.info("Configuring MqMonitorService");
        return new MqMonitorService(properties);
    }

    /**
     * 消息指标收集器
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "io.micrometer.core.instrument.MeterRegistry")
    @ConditionalOnProperty(prefix = "iam.mq.monitor", name = "enable-micrometer", havingValue = "true", matchIfMissing = true)
    public MqMetricsCollector mqMetricsCollector(MqProperties properties) {
        log.info("Configuring MqMetricsCollector");
        return new MqMetricsCollector(properties);
    }

    /**
     * 消息重试器
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "iam.mq.retry", name = "enabled", havingValue = "true", matchIfMissing = true)
    public MessageRetryService messageRetryService(MessageSender messageSender,
                                                    MqProperties properties) {
        log.info("Configuring MessageRetryService");
        return new MessageRetryService(messageSender, properties);
    }
}
