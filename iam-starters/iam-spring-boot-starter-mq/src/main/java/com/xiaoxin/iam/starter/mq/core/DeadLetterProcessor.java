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

/**
 * 死信队列处理器
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class DeadLetterProcessor {

    private final MessageSender messageSender;
    private final MqProperties properties;

    /**
     * 处理死信消息
     */
    public void processDeadLetter(String originalTopic, Object message, String reason) {
        try {
            String deadLetterTopic = buildDeadLetterTopic(originalTopic);
            log.warn("处理死信消息: originalTopic={}, deadLetterTopic={}, reason={}", 
                    originalTopic, deadLetterTopic, reason);
            
            // 发送到死信队列
            messageSender.sendSync(deadLetterTopic, "dead-letter", message);
            
            log.info("死信消息处理完成: topic={}", deadLetterTopic);
        } catch (Exception e) {
            log.error("死信消息处理失败: originalTopic={}, reason={}", originalTopic, reason, e);
        }
    }

    /**
     * 重试死信消息
     */
    public void retryDeadLetter(String deadLetterTopic, Object message) {
        try {
            String originalTopic = extractOriginalTopic(deadLetterTopic);
            log.info("重试死信消息: deadLetterTopic={}, originalTopic={}", deadLetterTopic, originalTopic);
            
            // 重新发送到原始主题
            messageSender.sendSync(originalTopic, message);
            
            log.info("死信消息重试成功: topic={}", originalTopic);
        } catch (Exception e) {
            log.error("死信消息重试失败: deadLetterTopic={}", deadLetterTopic, e);
        }
    }

    /**
     * 构建死信队列主题
     */
    private String buildDeadLetterTopic(String originalTopic) {
        return originalTopic + properties.getDeadLetter().getTopicSuffix();
    }

    /**
     * 从死信队列主题提取原始主题
     */
    private String extractOriginalTopic(String deadLetterTopic) {
        String suffix = properties.getDeadLetter().getTopicSuffix();
        if (deadLetterTopic.endsWith(suffix)) {
            return deadLetterTopic.substring(0, deadLetterTopic.length() - suffix.length());
        }
        return deadLetterTopic;
    }
}
