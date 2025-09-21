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

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息追踪信息
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@Builder
public class MessageTrace {

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 主题
     */
    private String topic;

    /**
     * 标签
     */
    private String tag;

    /**
     * 消息内容
     */
    private Object message;

    /**
     * 追踪事件列表
     */
    private List<TraceEvent> events;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 追踪事件
     */
    @Data
    @Builder
    public static class TraceEvent {

        /**
         * 事件类型
         */
        private EventType type;

        /**
         * 事件时间
         */
        private LocalDateTime timestamp;

        /**
         * 消费者组（仅消费事件）
         */
        private String consumerGroup;

        /**
         * 操作描述
         */
        private String operation;

        /**
         * 异常信息（仅异常事件）
         */
        private String errorMessage;

        /**
         * 执行耗时（毫秒）
         */
        private Long duration;
    }

    /**
     * 事件类型枚举
     */
    public enum EventType {
        /**
         * 发送
         */
        SEND,

        /**
         * 消费
         */
        CONSUME,

        /**
         * 异常
         */
        EXCEPTION
    }
}
