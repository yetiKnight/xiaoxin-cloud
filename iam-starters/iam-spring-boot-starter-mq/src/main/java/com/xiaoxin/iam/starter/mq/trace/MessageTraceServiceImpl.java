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

package com.xiaoxin.iam.starter.mq.trace;

import com.xiaoxin.iam.starter.mq.config.MqProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息追踪服务实现
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class MessageTraceServiceImpl implements MessageTraceService {

    private final MqProperties properties;
    private final ConcurrentHashMap<String, MessageTrace> traceMap = new ConcurrentHashMap<>();

    @Override
    public void traceSend(String messageId, String topic, String tag, Object message) {
        try {
            MessageTrace trace = traceMap.computeIfAbsent(messageId, k -> 
                    MessageTrace.builder()
                            .messageId(messageId)
                            .topic(topic)
                            .tag(tag)
                            .message(message)
                            .events(new ArrayList<>())
                            .createTime(LocalDateTime.now())
                            .build());

            MessageTrace.TraceEvent event = MessageTrace.TraceEvent.builder()
                    .type(MessageTrace.EventType.SEND)
                    .timestamp(LocalDateTime.now())
                    .operation("消息发送")
                    .build();

            trace.getEvents().add(event);
            
            log.debug("记录消息发送追踪: messageId={}, topic={}", messageId, topic);
        } catch (Exception e) {
            log.error("记录消息发送追踪失败: messageId={}, topic={}", messageId, topic, e);
        }
    }

    @Override
    public void traceConsume(String messageId, String topic, String tag, String consumerGroup, Object message) {
        try {
            MessageTrace trace = traceMap.computeIfAbsent(messageId, k -> 
                    MessageTrace.builder()
                            .messageId(messageId)
                            .topic(topic)
                            .tag(tag)
                            .message(message)
                            .events(new ArrayList<>())
                            .createTime(LocalDateTime.now())
                            .build());

            MessageTrace.TraceEvent event = MessageTrace.TraceEvent.builder()
                    .type(MessageTrace.EventType.CONSUME)
                    .timestamp(LocalDateTime.now())
                    .consumerGroup(consumerGroup)
                    .operation("消息消费")
                    .build();

            trace.getEvents().add(event);
            
            log.debug("记录消息消费追踪: messageId={}, topic={}, consumerGroup={}", 
                    messageId, topic, consumerGroup);
        } catch (Exception e) {
            log.error("记录消息消费追踪失败: messageId={}, topic={}, consumerGroup={}", 
                    messageId, topic, consumerGroup, e);
        }
    }

    @Override
    public void traceException(String messageId, String topic, String operation, Throwable exception) {
        try {
            MessageTrace trace = traceMap.get(messageId);
            if (trace == null) {
                trace = MessageTrace.builder()
                        .messageId(messageId)
                        .topic(topic)
                        .events(new ArrayList<>())
                        .createTime(LocalDateTime.now())
                        .build();
                traceMap.put(messageId, trace);
            }

            MessageTrace.TraceEvent event = MessageTrace.TraceEvent.builder()
                    .type(MessageTrace.EventType.EXCEPTION)
                    .timestamp(LocalDateTime.now())
                    .operation(operation)
                    .errorMessage(exception.getMessage())
                    .build();

            trace.getEvents().add(event);
            
            log.debug("记录消息异常追踪: messageId={}, topic={}, operation={}", 
                    messageId, topic, operation);
        } catch (Exception e) {
            log.error("记录消息异常追踪失败: messageId={}, topic={}, operation={}", 
                    messageId, topic, operation, e);
        }
    }

    @Override
    public MessageTrace getTrace(String messageId) {
        return traceMap.get(messageId);
    }

    @Override
    public void cleanupExpiredTraces() {
        try {
            LocalDateTime expireTime = LocalDateTime.now()
                    .minus(properties.getTrace().getRetentionPeriod());
            
            traceMap.entrySet().removeIf(entry -> 
                    entry.getValue().getCreateTime().isBefore(expireTime));
            
            log.debug("清理过期追踪信息完成");
        } catch (Exception e) {
            log.error("清理过期追踪信息失败", e);
        }
    }
}
