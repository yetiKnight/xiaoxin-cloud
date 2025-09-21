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

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 领域事件基类
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class DomainEvent {

    /**
     * 事件ID
     */
    private String eventId;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 事件版本
     */
    private String version;

    /**
     * 发生时间
     */
    private LocalDateTime occurredOn;

    /**
     * 聚合根ID
     */
    private String aggregateId;

    /**
     * 聚合根类型
     */
    private String aggregateType;

    /**
     * 事件来源
     */
    private String source;

    /**
     * 事件主题
     */
    private String topic;

    /**
     * 事件标签
     */
    private String tag;

    /**
     * 构造函数
     */
    protected DomainEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.eventType = this.getClass().getSimpleName();
        this.version = "1.0";
        this.occurredOn = LocalDateTime.now();
    }

    /**
     * 构造函数（带聚合根信息）
     * 
     * @param aggregateId 聚合根ID
     * @param aggregateType 聚合根类型
     */
    protected DomainEvent(String aggregateId, String aggregateType) {
        this();
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
    }

    /**
     * 获取事件主题（如果未设置则使用默认规则）
     */
    public String getTopic() {
        if (topic != null && !topic.isEmpty()) {
            return topic;
        }
        // 默认主题规则：事件类型转换为下划线格式
        return convertToUnderScore(eventType);
    }

    /**
     * 获取事件标签（如果未设置则使用聚合根类型）
     */
    public String getTag() {
        if (tag != null && !tag.isEmpty()) {
            return tag;
        }
        return aggregateType;
    }

    /**
     * 转换为下划线格式
     */
    private String convertToUnderScore(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }
        
        StringBuilder result = new StringBuilder();
        result.append(Character.toLowerCase(camelCase.charAt(0)));
        
        for (int i = 1; i < camelCase.length(); i++) {
            char ch = camelCase.charAt(i);
            if (Character.isUpperCase(ch)) {
                result.append('_').append(Character.toLowerCase(ch));
            } else {
                result.append(ch);
            }
        }
        
        return result.toString();
    }

    /**
     * 创建事件副本
     */
    public abstract DomainEvent copy();
}
