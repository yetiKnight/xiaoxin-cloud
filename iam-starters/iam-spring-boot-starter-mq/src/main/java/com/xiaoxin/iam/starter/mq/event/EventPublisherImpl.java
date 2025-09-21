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

package com.xiaoxin.iam.starter.mq.event;

import com.xiaoxin.iam.starter.mq.config.MqProperties;
import com.xiaoxin.iam.starter.mq.core.MessageSender;
import com.xiaoxin.iam.starter.mq.core.SendCallback;
import com.xiaoxin.iam.starter.mq.core.SendResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;

import javax.annotation.Resource;

/**
 * 事件发布器实现
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class EventPublisherImpl implements EventPublisher {

    private final MessageSender messageSender;
    private final MqProperties properties;

    @Resource
    @Qualifier("eventTaskExecutor")
    private TaskExecutor taskExecutor;

    @Override
    public void publish(DomainEvent event) {
        String topic = buildTopic(event.getTopic());
        SendResult result = messageSender.sendSync(topic, event.getTag(), event.getEventId(), event);
        
        if (!result.isSuccess()) {
            handleFailure(event, result.getException());
        } else {
            log.debug("Published event: {} to topic: {}", event.getEventType(), topic);
        }
    }

    @Override
    public void publishAsync(DomainEvent event) {
        if (properties.getEvent().isEnableAsync() && taskExecutor != null) {
            taskExecutor.execute(() -> doPublishAsync(event));
        } else {
            doPublishAsync(event);
        }
    }

    @Override
    public void publish(String topic, DomainEvent event) {
        String fullTopic = buildTopic(topic);
        SendResult result = messageSender.sendSync(fullTopic, event.getTag(), event.getEventId(), event);
        
        if (!result.isSuccess()) {
            handleFailure(event, result.getException());
        } else {
            log.debug("Published event: {} to topic: {}", event.getEventType(), fullTopic);
        }
    }

    @Override
    public void publishAsync(String topic, DomainEvent event) {
        if (properties.getEvent().isEnableAsync() && taskExecutor != null) {
            taskExecutor.execute(() -> doPublishAsync(topic, event));
        } else {
            doPublishAsync(topic, event);
        }
    }

    @Override
    public void publishDelay(DomainEvent event, int delayLevel) {
        String topic = buildTopic(event.getTopic());
        SendResult result = messageSender.sendDelayMessage(topic, event, delayLevel);
        
        if (!result.isSuccess()) {
            handleFailure(event, result.getException());
        } else {
            log.debug("Published delay event: {} to topic: {} with delay level: {}", 
                    event.getEventType(), topic, delayLevel);
        }
    }

    @Override
    public void publishOrderly(DomainEvent event, String hashKey) {
        String topic = buildTopic(event.getTopic());
        SendResult result = messageSender.sendOrderly(topic, event, hashKey);
        
        if (!result.isSuccess()) {
            handleFailure(event, result.getException());
        } else {
            log.debug("Published orderly event: {} to topic: {} with hash key: {}", 
                    event.getEventType(), topic, hashKey);
        }
    }

    /**
     * 异步发布事件
     */
    private void doPublishAsync(DomainEvent event) {
        String topic = buildTopic(event.getTopic());
        messageSender.sendAsync(topic, event.getTag(), event.getEventId(), event, new SendCallback() {
            @Override
            public void onSuccess(SendResult result) {
                log.debug("Published async event: {} to topic: {}", event.getEventType(), topic);
            }

            @Override
            public void onException(Throwable exception) {
                handleFailure(event, exception);
            }
        });
    }

    /**
     * 异步发布事件到指定主题
     */
    private void doPublishAsync(String topic, DomainEvent event) {
        String fullTopic = buildTopic(topic);
        messageSender.sendAsync(fullTopic, event.getTag(), event.getEventId(), event, new SendCallback() {
            @Override
            public void onSuccess(SendResult result) {
                log.debug("Published async event: {} to topic: {}", event.getEventType(), fullTopic);
            }

            @Override
            public void onException(Throwable exception) {
                handleFailure(event, exception);
            }
        });
    }

    /**
     * 构建完整主题名称
     */
    private String buildTopic(String eventTopic) {
        String prefix = properties.getEvent().getTopicPrefix();
        if (prefix != null && !prefix.isEmpty() && !eventTopic.startsWith(prefix)) {
            return prefix + eventTopic;
        }
        return eventTopic;
    }

    /**
     * 处理发布失败
     */
    private void handleFailure(DomainEvent event, Throwable exception) {
        MqProperties.Event.FailureStrategy strategy = properties.getEvent().getFailureStrategy();
        
        switch (strategy) {
            case LOG_AND_IGNORE:
                log.error("Failed to publish event: {} [{}]", event.getEventType(), event.getEventId(), exception);
                break;
            case THROW_EXCEPTION:
                log.error("Failed to publish event: {} [{}]", event.getEventType(), event.getEventId(), exception);
                throw new EventPublishException("Failed to publish event", exception);
            case RETRY:
                // TODO: 实现重试逻辑
                log.error("Failed to publish event: {} [{}], retry not implemented yet", 
                        event.getEventType(), event.getEventId(), exception);
                break;
        }
    }

    /**
     * 事件发布异常
     */
    public static class EventPublishException extends RuntimeException {
        public EventPublishException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
