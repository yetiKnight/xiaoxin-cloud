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

/**
 * 消息追踪服务接口
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public interface MessageTraceService {

    /**
     * 记录消息发送追踪
     */
    void traceSend(String messageId, String topic, String tag, Object message);

    /**
     * 记录消息消费追踪
     */
    void traceConsume(String messageId, String topic, String tag, String consumerGroup, Object message);

    /**
     * 记录消息异常追踪
     */
    void traceException(String messageId, String topic, String operation, Throwable exception);

    /**
     * 获取消息追踪信息
     */
    com.xiaoxin.iam.starter.mq.trace.MessageTrace getTrace(String messageId);

    /**
     * 清理过期的追踪信息
     */
    void cleanupExpiredTraces();
}
