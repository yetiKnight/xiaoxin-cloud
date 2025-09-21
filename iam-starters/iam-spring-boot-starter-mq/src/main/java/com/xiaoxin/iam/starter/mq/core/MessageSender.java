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

/**
 * 消息发送器接口
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public interface MessageSender {

    /**
     * 同步发送消息
     * 
     * @param topic 主题
     * @param message 消息内容
     * @return 发送结果
     */
    SendResult sendSync(String topic, Object message);

    /**
     * 同步发送消息（带标签）
     * 
     * @param topic 主题
     * @param tag 标签
     * @param message 消息内容
     * @return 发送结果
     */
    SendResult sendSync(String topic, String tag, Object message);

    /**
     * 同步发送消息（带键值）
     * 
     * @param topic 主题
     * @param tag 标签
     * @param key 消息键
     * @param message 消息内容
     * @return 发送结果
     */
    SendResult sendSync(String topic, String tag, String key, Object message);

    /**
     * 异步发送消息
     * 
     * @param topic 主题
     * @param message 消息内容
     * @param callback 回调函数
     */
    void sendAsync(String topic, Object message, SendCallback callback);

    /**
     * 异步发送消息（带标签）
     * 
     * @param topic 主题
     * @param tag 标签
     * @param message 消息内容
     * @param callback 回调函数
     */
    void sendAsync(String topic, String tag, Object message, SendCallback callback);

    /**
     * 异步发送消息（带键值）
     * 
     * @param topic 主题
     * @param tag 标签
     * @param key 消息键
     * @param message 消息内容
     * @param callback 回调函数
     */
    void sendAsync(String topic, String tag, String key, Object message, SendCallback callback);

    /**
     * 单向发送消息（不关心结果）
     * 
     * @param topic 主题
     * @param message 消息内容
     */
    void sendOneWay(String topic, Object message);

    /**
     * 单向发送消息（带标签）
     * 
     * @param topic 主题
     * @param tag 标签
     * @param message 消息内容
     */
    void sendOneWay(String topic, String tag, Object message);

    /**
     * 单向发送消息（带键值）
     * 
     * @param topic 主题
     * @param tag 标签
     * @param key 消息键
     * @param message 消息内容
     */
    void sendOneWay(String topic, String tag, String key, Object message);

    /**
     * 发送顺序消息
     * 
     * @param topic 主题
     * @param message 消息内容
     * @param hashKey 哈希键（用于选择队列）
     * @return 发送结果
     */
    SendResult sendOrderly(String topic, Object message, String hashKey);

    /**
     * 发送延时消息
     * 
     * @param topic 主题
     * @param message 消息内容
     * @param delayLevel 延时级别
     * @return 发送结果
     */
    SendResult sendDelayMessage(String topic, Object message, int delayLevel);

    /**
     * 发送事务消息
     * 
     * @param topic 主题
     * @param message 消息内容
     * @param arg 事务参数
     * @return 发送结果
     */
    SendResult sendInTransaction(String topic, Object message, Object arg);
}
