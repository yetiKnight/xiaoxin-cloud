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

/**
 * 事件发布器接口
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public interface EventPublisher {

    /**
     * 发布事件（同步）
     * 
     * @param event 事件对象
     */
    void publish(DomainEvent event);

    /**
     * 发布事件（异步）
     * 
     * @param event 事件对象
     */
    void publishAsync(DomainEvent event);

    /**
     * 发布事件到指定主题
     * 
     * @param topic 主题
     * @param event 事件对象
     */
    void publish(String topic, DomainEvent event);

    /**
     * 异步发布事件到指定主题
     * 
     * @param topic 主题
     * @param event 事件对象
     */
    void publishAsync(String topic, DomainEvent event);

    /**
     * 发布延时事件
     * 
     * @param event 事件对象
     * @param delayLevel 延时级别
     */
    void publishDelay(DomainEvent event, int delayLevel);

    /**
     * 发布顺序事件
     * 
     * @param event 事件对象
     * @param hashKey 哈希键（用于选择队列）
     */
    void publishOrderly(DomainEvent event, String hashKey);
}
