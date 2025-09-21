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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 消息发送结果
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendResult {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 消息键
     */
    private String messageKey;

    /**
     * 主题
     */
    private String topic;

    /**
     * 标签
     */
    private String tag;

    /**
     * 队列ID
     */
    private int queueId;

    /**
     * 队列偏移量
     */
    private long queueOffset;

    /**
     * 发送状态
     */
    private SendStatus sendStatus;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 错误消息
     */
    private String errorMessage;

    /**
     * 异常信息
     */
    private Throwable exception;

    /**
     * 发送状态枚举
     */
    public enum SendStatus {
        /**
         * 发送成功
         */
        SEND_OK,
        
        /**
         * 刷盘超时
         */
        FLUSH_DISK_TIMEOUT,
        
        /**
         * 刷从机超时
         */
        FLUSH_SLAVE_TIMEOUT,
        
        /**
         * 从机不可用
         */
        SLAVE_NOT_AVAILABLE,
        
        /**
         * 发送失败
         */
        SEND_FAILED
    }

    /**
     * 创建成功结果
     * 
     * @param topic 主题
     * @param messageId 消息ID
     * @param messageKey 消息键
     * @return 发送结果
     */
    public static SendResult success(String topic, String messageId, String messageKey) {
        return SendResult.builder()
                .success(true)
                .messageId(messageId)
                .messageKey(messageKey)
                .topic(topic)
                .sendStatus(SendStatus.SEND_OK)
                .sendTime(LocalDateTime.now())
                .build();
    }

    /**
     * 创建成功结果
     * 
     * @param messageId 消息ID
     * @param topic 主题
     * @return 发送结果
     */
    public static SendResult success(String messageId, String topic) {
        return success(topic, messageId, null);
    }

    /**
     * 创建失败结果
     * 
     * @param topic 主题
     * @param errorMessage 错误消息
     * @return 发送结果
     */
    public static SendResult failure(String topic, String errorMessage) {
        return SendResult.builder()
                .success(false)
                .topic(topic)
                .sendStatus(SendStatus.SEND_FAILED)
                .errorMessage(errorMessage)
                .sendTime(LocalDateTime.now())
                .build();
    }

    /**
     * 创建失败结果（带异常）
     * 
     * @param topic 主题
     * @param exception 异常
     * @return 发送结果
     */
    public static SendResult failure(String topic, Throwable exception) {
        return SendResult.builder()
                .success(false)
                .topic(topic)
                .sendStatus(SendStatus.SEND_FAILED)
                .errorMessage(exception.getMessage())
                .exception(exception)
                .sendTime(LocalDateTime.now())
                .build();
    }
}
