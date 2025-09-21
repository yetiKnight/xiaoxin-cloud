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

package com.xiaoxin.iam.starter.mq.core;

import com.xiaoxin.iam.starter.mq.config.MqProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息重试服务
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class MessageRetryService {

    private final MessageSender messageSender;
    private final MqProperties properties;
    private final ConcurrentHashMap<String, RetryContext> retryMap = new ConcurrentHashMap<>();
    // 暂时不使用调度器，后续版本可以添加定时重试功能
    // private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    /**
     * 重试消息
     */
    public CompletableFuture<Boolean> retryMessage(String messageId, String topic, Object message) {
        RetryContext context = retryMap.computeIfAbsent(messageId, 
                k -> new RetryContext(messageId, topic, message));
        
        if (context.getRetryCount() >= properties.getRetry().getMaxAttempts()) {
            log.warn("消息重试次数已达上限: messageId={}, retryCount={}", 
                    messageId, context.getRetryCount());
            retryMap.remove(messageId);
            return CompletableFuture.completedFuture(false);
        }

        return executeRetry(context);
    }

    /**
     * 执行重试
     */
    private CompletableFuture<Boolean> executeRetry(RetryContext context) {
        long delay = calculateRetryDelay(context.getRetryCount());
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(delay);
                
                SendResult result = messageSender.sendSync(context.getTopic(), context.getMessage());
                if (result.isSuccess()) {
                    log.info("消息重试成功: messageId={}, retryCount={}", 
                            context.getMessageId(), context.getRetryCount());
                    retryMap.remove(context.getMessageId());
                    return true;
                } else {
                    context.incrementRetryCount();
                    log.warn("消息重试失败: messageId={}, retryCount={}", 
                            context.getMessageId(), context.getRetryCount());
                    return false;
                }
            } catch (Exception e) {
                context.incrementRetryCount();
                log.error("消息重试异常: messageId={}, retryCount={}", 
                        context.getMessageId(), context.getRetryCount(), e);
                return false;
            }
        });
    }

    /**
     * 计算重试延迟时间
     */
    private long calculateRetryDelay(int retryCount) {
        MqProperties.Retry.RetryStrategy strategy = properties.getRetry().getStrategy();
        long baseDelay = properties.getRetry().getInterval().toMillis();
        
        switch (strategy) {
            case FIXED_DELAY:
                return baseDelay;
            case EXPONENTIAL_BACKOFF:
                return (long) (baseDelay * Math.pow(properties.getRetry().getMultiplier(), retryCount));
            case CUSTOM:
                return baseDelay * (retryCount + 1);
            default:
                return baseDelay;
        }
    }

    /**
     * 清理过期的重试上下文
     */
    public void cleanupExpiredRetries() {
        LocalDateTime now = LocalDateTime.now();
        long maxAge = properties.getRetry().getMaxInterval().toMillis();
        
        retryMap.entrySet().removeIf(entry -> {
            RetryContext context = entry.getValue();
            return now.isAfter(context.getCreateTime().plusNanos(maxAge * 1_000_000));
        });
    }

    /**
     * 重试上下文
     */
    private static class RetryContext {
        private final String messageId;
        private final String topic;
        private final Object message;
        private final LocalDateTime createTime;
        private int retryCount;

        public RetryContext(String messageId, String topic, Object message) {
            this.messageId = messageId;
            this.topic = topic;
            this.message = message;
            this.createTime = LocalDateTime.now();
            this.retryCount = 0;
        }

        public String getMessageId() {
            return messageId;
        }

        public String getTopic() {
            return topic;
        }

        public Object getMessage() {
            return message;
        }

        public LocalDateTime getCreateTime() {
            return createTime;
        }

        public int getRetryCount() {
            return retryCount;
        }

        public void incrementRetryCount() {
            this.retryCount++;
        }
    }
}
