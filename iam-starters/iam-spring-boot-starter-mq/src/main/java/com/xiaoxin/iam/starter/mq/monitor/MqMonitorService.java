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

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * 消息队列监控服务
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class MqMonitorService {

    private final MqProperties properties;
    private final AtomicLong totalSentCount = new AtomicLong(0);
    private final AtomicLong totalConsumedCount = new AtomicLong(0);
    private final AtomicLong totalErrorCount = new AtomicLong(0);
    private final ConcurrentHashMap<String, AtomicLong> topicSentCount = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicLong> topicConsumedCount = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicLong> topicErrorCount = new ConcurrentHashMap<>();

    /**
     * 记录消息发送
     */
    public void recordSent(String topic) {
        totalSentCount.incrementAndGet();
        topicSentCount.computeIfAbsent(topic, k -> new AtomicLong(0)).incrementAndGet();
        
        if (log.isDebugEnabled()) {
            log.debug("记录消息发送: topic={}, totalSent={}", topic, totalSentCount.get());
        }
    }

    /**
     * 记录消息消费
     */
    public void recordConsumed(String topic) {
        totalConsumedCount.incrementAndGet();
        topicConsumedCount.computeIfAbsent(topic, k -> new AtomicLong(0)).incrementAndGet();
        
        if (log.isDebugEnabled()) {
            log.debug("记录消息消费: topic={}, totalConsumed={}", topic, totalConsumedCount.get());
        }
    }

    /**
     * 记录消息错误
     */
    public void recordError(String topic) {
        totalErrorCount.incrementAndGet();
        topicErrorCount.computeIfAbsent(topic, k -> new AtomicLong(0)).incrementAndGet();
        
        log.warn("记录消息错误: topic={}, totalError={}", topic, totalErrorCount.get());
    }

    /**
     * 获取总发送数量
     */
    public long getTotalSentCount() {
        return totalSentCount.get();
    }

    /**
     * 获取总消费数量
     */
    public long getTotalConsumedCount() {
        return totalConsumedCount.get();
    }

    /**
     * 获取总错误数量
     */
    public long getTotalErrorCount() {
        return totalErrorCount.get();
    }

    /**
     * 获取主题发送统计
     */
    public Map<String, Long> getTopicSentStats() {
        Map<String, Long> stats = new ConcurrentHashMap<>();
        topicSentCount.forEach((topic, count) -> stats.put(topic, count.get()));
        return stats;
    }

    /**
     * 获取主题消费统计
     */
    public Map<String, Long> getTopicConsumedStats() {
        Map<String, Long> stats = new ConcurrentHashMap<>();
        topicConsumedCount.forEach((topic, count) -> stats.put(topic, count.get()));
        return stats;
    }

    /**
     * 获取主题错误统计
     */
    public Map<String, Long> getTopicErrorStats() {
        Map<String, Long> stats = new ConcurrentHashMap<>();
        topicErrorCount.forEach((topic, count) -> stats.put(topic, count.get()));
        return stats;
    }

    /**
     * 重置统计信息
     */
    public void resetStats() {
        totalSentCount.set(0);
        totalConsumedCount.set(0);
        totalErrorCount.set(0);
        topicSentCount.clear();
        topicConsumedCount.clear();
        topicErrorCount.clear();
        
        log.info("监控统计信息已重置");
    }

    /**
     * 获取监控报告
     */
    public com.xiaoxin.iam.starter.mq.monitor.MonitorReport getMonitorReport() {
        return com.xiaoxin.iam.starter.mq.monitor.MonitorReport.builder()
                .totalSent(getTotalSentCount())
                .totalConsumed(getTotalConsumedCount())
                .totalError(getTotalErrorCount())
                .topicSentStats(getTopicSentStats())
                .topicConsumedStats(getTopicConsumedStats())
                .topicErrorStats(getTopicErrorStats())
                .build();
    }
}
