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

package com.xiaoxin.iam.starter.mq.monitor;

import com.xiaoxin.iam.starter.mq.config.MqProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息队列指标收集器
 * 集成Micrometer进行指标收集
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class MqMetricsCollector {

    private final MqProperties properties;

    /**
     * 记录消息发送指标
     */
    public void recordSendMetrics(String topic, String tag, long duration, boolean success) {
        try {
            // 这里可以集成Micrometer来记录指标
            // 由于Micrometer是可选依赖，所以这里先做简单的日志记录
            if (log.isDebugEnabled()) {
                log.debug("记录发送指标: topic={}, tag={}, duration={}ms, success={}", 
                        topic, tag, duration, success);
            }
        } catch (Exception e) {
            log.error("记录发送指标失败: topic={}, tag={}", topic, tag, e);
        }
    }

    /**
     * 记录消息消费指标
     */
    public void recordConsumeMetrics(String topic, String tag, String consumerGroup, 
                                     long duration, boolean success) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("记录消费指标: topic={}, tag={}, consumerGroup={}, duration={}ms, success={}", 
                        topic, tag, consumerGroup, duration, success);
            }
        } catch (Exception e) {
            log.error("记录消费指标失败: topic={}, tag={}, consumerGroup={}", 
                    topic, tag, consumerGroup, e);
        }
    }

    /**
     * 记录队列深度指标
     */
    public void recordQueueDepthMetrics(String topic, long depth) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("记录队列深度指标: topic={}, depth={}", topic, depth);
            }
        } catch (Exception e) {
            log.error("记录队列深度指标失败: topic={}", topic, e);
        }
    }

    /**
     * 记录错误指标
     */
    public void recordErrorMetrics(String topic, String errorType, String errorMessage) {
        try {
            log.warn("记录错误指标: topic={}, errorType={}, errorMessage={}", 
                    topic, errorType, errorMessage);
        } catch (Exception e) {
            log.error("记录错误指标失败: topic={}, errorType={}", topic, errorType, e);
        }
    }
}
