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
import com.xiaoxin.iam.starter.mq.serialization.MessageSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

/**
 * RocketMQ消息发送器实现
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class RocketMqMessageSender implements MessageSender {

    private final RocketMQTemplate rocketMQTemplate;
    private final MessageSerializer messageSerializer;
    private final MqProperties properties;

    @Override
    public SendResult sendSync(String topic, Object message) {
        return sendSync(topic, null, null, message);
    }

    @Override
    public SendResult sendSync(String topic, String tag, Object message) {
        return sendSync(topic, tag, null, message);
    }

    @Override
    public SendResult sendSync(String topic, String tag, String key, Object message) {
        try {
            String destination = buildDestination(topic, tag);
            Message<byte[]> mqMessage = buildMessage(message, key);
            
            rocketMQTemplate.syncSend(destination, mqMessage);
            
            return SendResult.success(topic, "sync-" + System.currentTimeMillis(), key);
        } catch (Exception e) {
            log.error("Failed to send sync message to topic: {}, tag: {}, key: {}", topic, tag, key, e);
            return SendResult.failure(topic, e);
        }
    }

    @Override
    public void sendAsync(String topic, Object message, SendCallback callback) {
        sendAsync(topic, null, null, message, callback);
    }

    @Override
    public void sendAsync(String topic, String tag, Object message, SendCallback callback) {
        sendAsync(topic, tag, null, message, callback);
    }

    @Override
    public void sendAsync(String topic, String tag, String key, Object message, SendCallback callback) {
        try {
            String destination = buildDestination(topic, tag);
            Message<byte[]> mqMessage = buildMessage(message, key);
            
            rocketMQTemplate.asyncSend(destination, mqMessage, new org.apache.rocketmq.client.producer.SendCallback() {
                @Override
                public void onSuccess(org.apache.rocketmq.client.producer.SendResult sendResult) {
                    SendResult result = SendResult.success(topic, "async-" + System.currentTimeMillis(), key);
                    callback.onSuccess(result);
                }

                @Override
                public void onException(Throwable e) {
                    log.error("Failed to send async message to topic: {}, tag: {}, key: {}", topic, tag, key, e);
                    callback.onException(e);
                }
            });
        } catch (Exception e) {
            log.error("Failed to send async message to topic: {}, tag: {}, key: {}", topic, tag, key, e);
            callback.onException(e);
        }
    }

    @Override
    public void sendOneWay(String topic, Object message) {
        sendOneWay(topic, null, null, message);
    }

    @Override
    public void sendOneWay(String topic, String tag, Object message) {
        sendOneWay(topic, tag, null, message);
    }

    @Override
    public void sendOneWay(String topic, String tag, String key, Object message) {
        try {
            String destination = buildDestination(topic, tag);
            Message<byte[]> mqMessage = buildMessage(message, key);
            
            rocketMQTemplate.sendOneWay(destination, mqMessage);
            log.debug("Sent one way message to topic: {}, tag: {}, key: {}", topic, tag, key);
        } catch (Exception e) {
            log.error("Failed to send one way message to topic: {}, tag: {}, key: {}", topic, tag, key, e);
        }
    }

    @Override
    public SendResult sendOrderly(String topic, Object message, String hashKey) {
        try {
            Message<byte[]> mqMessage = buildMessage(message, null);
            
            rocketMQTemplate.syncSendOrderly(topic, mqMessage, hashKey);
            
            return SendResult.success(topic, "orderly-" + System.currentTimeMillis(), hashKey);
        } catch (Exception e) {
            log.error("Failed to send orderly message to topic: {}, hashKey: {}", topic, hashKey, e);
            return SendResult.failure(topic, e);
        }
    }

    @Override
    public SendResult sendDelayMessage(String topic, Object message, int delayLevel) {
        try {
            Message<byte[]> mqMessage = buildMessage(message, null);
            
            rocketMQTemplate.syncSend(topic, mqMessage, 
                    properties.getRocketmq().getProducer().getSendMsgTimeout().toMillis(), 
                    delayLevel);
            
            return SendResult.success(topic, "delay-" + System.currentTimeMillis(), null);
        } catch (Exception e) {
            log.error("Failed to send delay message to topic: {}, delayLevel: {}", topic, delayLevel, e);
            return SendResult.failure(topic, e);
        }
    }

    @Override
    public SendResult sendInTransaction(String topic, Object message, Object arg) {
        try {
            Message<byte[]> mqMessage = buildMessage(message, null);
            
            rocketMQTemplate.sendMessageInTransaction(topic, mqMessage, arg);
            
            return SendResult.success(topic, "txn-" + System.currentTimeMillis(), null);
        } catch (Exception e) {
            log.error("Failed to send transaction message to topic: {}", topic, e);
            return SendResult.failure(topic, e);
        }
    }

    /**
     * 构建消息目的地
     */
    private String buildDestination(String topic, String tag) {
        if (tag != null && !tag.isEmpty()) {
            return topic + ":" + tag;
        }
        return topic;
    }

    /**
     * 构建消息对象
     */
    private Message<byte[]> buildMessage(Object payload, String key) {
        byte[] body = messageSerializer.serialize(payload);
        
        MessageBuilder<byte[]> builder = MessageBuilder.withPayload(body);
        
        if (key != null && !key.isEmpty()) {
            builder.setHeader("KEYS", key);
        }
        
        return builder.build();
    }

}
