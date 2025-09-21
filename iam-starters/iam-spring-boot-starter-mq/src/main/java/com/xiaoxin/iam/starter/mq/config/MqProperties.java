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

package com.xiaoxin.iam.starter.mq.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息队列配置属性
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "iam.mq")
public class MqProperties {

    /**
     * 是否启用MQ功能
     */
    private boolean enabled = true;

    /**
     * RocketMQ配置
     */
    private RocketMq rocketmq = new RocketMq();

    /**
     * 事件驱动配置
     */
    private Event event = new Event();

    /**
     * 消息序列化配置
     */
    private Serialization serialization = new Serialization();

    /**
     * 死信队列配置
     */
    private DeadLetter deadLetter = new DeadLetter();

    /**
     * 消息追踪配置
     */
    private Trace trace = new Trace();

    /**
     * 重试配置
     */
    private Retry retry = new Retry();

    /**
     * 监控配置
     */
    private Monitor monitor = new Monitor();

    /**
     * RocketMQ配置类
     */
    @Data
    public static class RocketMq {
        /**
         * NameServer地址
         */
        private String nameServer = "localhost:9876";

        /**
         * 生产者配置
         */
        private Producer producer = new Producer();

        /**
         * 消费者配置
         */
        private Consumer consumer = new Consumer();

        /**
         * 访问控制配置
         */
        private Acl acl = new Acl();

        /**
         * 生产者配置类
         */
        @Data
        public static class Producer {
            /**
             * 生产者组名
             */
            private String group = "iam_producer_group";

            /**
             * 发送消息超时时间
             */
            private Duration sendMsgTimeout = Duration.ofSeconds(3);

            /**
             * 压缩消息体阈值
             */
            private int compressMsgBodyOverHowMuch = 4096;

            /**
             * 同步发送失败重试次数
             */
            private int retryTimesWhenSendFailed = 2;

            /**
             * 异步发送失败重试次数
             */
            private int retryTimesWhenSendAsyncFailed = 2;

            /**
             * 消息最大大小
             */
            private int maxMessageSize = 4194304; // 4MB

            /**
             * 是否启用VIP通道
             */
            private boolean vipChannelEnabled = false;
        }

        /**
         * 消费者配置类
         */
        @Data
        public static class Consumer {
            /**
             * 消费者组名
             */
            private String group = "iam_consumer_group";

            /**
             * 消费模式
             */
            private ConsumeMode consumeMode = ConsumeMode.CONCURRENTLY;

            /**
             * 消费顺序
             */
            private ConsumeFromWhere consumeFromWhere = ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET;

            /**
             * 最小消费线程数
             */
            private int consumeThreadMin = 20;

            /**
             * 最大消费线程数
             */
            private int consumeThreadMax = 64;

            /**
             * 消费超时时间
             */
            private Duration consumeTimeout = Duration.ofMinutes(15);

            /**
             * 批量消费数量
             */
            private int consumeMessageBatchMaxSize = 1;

            /**
             * 是否启用VIP通道
             */
            private boolean vipChannelEnabled = false;

            /**
             * 消费模式枚举
             */
            public enum ConsumeMode {
                CONCURRENTLY, ORDERLY
            }

            /**
             * 消费起始位置枚举
             */
            public enum ConsumeFromWhere {
                CONSUME_FROM_LAST_OFFSET,
                CONSUME_FROM_FIRST_OFFSET,
                CONSUME_FROM_TIMESTAMP
            }
        }

        /**
         * 访问控制配置类
         */
        @Data
        public static class Acl {
            /**
             * 是否启用ACL
             */
            private boolean enabled = false;

            /**
             * 访问密钥
             */
            private String accessKey;

            /**
             * 密钥
             */
            private String secretKey;
        }
    }

    /**
     * 事件驱动配置类
     */
    @Data
    public static class Event {
        /**
         * 是否启用事件驱动
         */
        private boolean enabled = true;

        /**
         * 事件主题前缀
         */
        private String topicPrefix = "iam_event_";

        /**
         * 是否启用异步事件
         */
        private boolean enableAsync = true;

        /**
         * 异步事件线程池配置
         */
        private ThreadPool threadPool = new ThreadPool();

        /**
         * 事件发布失败处理策略
         */
        private FailureStrategy failureStrategy = FailureStrategy.LOG_AND_IGNORE;

        /**
         * 线程池配置类
         */
        @Data
        public static class ThreadPool {
            /**
             * 核心线程数
             */
            private int coreSize = 5;

            /**
             * 最大线程数
             */
            private int maxSize = 20;

            /**
             * 队列容量
             */
            private int queueCapacity = 100;

            /**
             * 线程空闲时间
             */
            private Duration keepAlive = Duration.ofSeconds(60);

            /**
             * 线程名前缀
             */
            private String threadNamePrefix = "iam-event-";
        }

        /**
         * 失败策略枚举
         */
        public enum FailureStrategy {
            LOG_AND_IGNORE, THROW_EXCEPTION, RETRY
        }
    }

    /**
     * 消息序列化配置类
     */
    @Data
    public static class Serialization {
        /**
         * 序列化类型
         */
        private SerializationType type = SerializationType.JSON;

        /**
         * 是否启用压缩
         */
        private boolean enableCompression = false;

        /**
         * 压缩阈值(字节)
         */
        private int compressionThreshold = 1024;

        /**
         * JSON序列化配置
         */
        private Json json = new Json();

        /**
         * 序列化类型枚举
         */
        public enum SerializationType {
            JSON, PROTOBUF, KRYO
        }

        /**
         * JSON序列化配置类
         */
        @Data
        public static class Json {
            /**
             * 是否包含类型信息
             */
            private boolean includeTypeInfo = true;

            /**
             * 日期格式
             */
            private String dateFormat = "yyyy-MM-dd HH:mm:ss";

            /**
             * 时区
             */
            private String timeZone = "GMT+8";
        }
    }

    /**
     * 死信队列配置类
     */
    @Data
    public static class DeadLetter {
        /**
         * 是否启用死信队列
         */
        private boolean enabled = true;

        /**
         * 死信队列主题后缀
         */
        private String topicSuffix = "_DLQ";

        /**
         * 最大重试次数
         */
        private int maxRetryTimes = 16;

        /**
         * 死信处理策略
         */
        private ProcessStrategy processStrategy = ProcessStrategy.MANUAL;

        /**
         * 自动处理间隔
         */
        private Duration autoProcessInterval = Duration.ofHours(1);

        /**
         * 死信消息保留时间
         */
        private Duration retentionTime = Duration.ofDays(7);

        /**
         * 处理策略枚举
         */
        public enum ProcessStrategy {
            MANUAL, AUTO_RETRY, AUTO_DISCARD
        }
    }

    /**
     * 消息追踪配置类
     */
    @Data
    public static class Trace {
        /**
         * 是否启用消息追踪
         */
        private boolean enabled = true;

        /**
         * 追踪主题
         */
        private String topic = "RMQ_SYS_TRACE_TOPIC";

        /**
         * 追踪组名
         */
        private String group = "iam_trace_group";

        /**
         * 最大消息大小
         */
        private int maxMsgSize = 128000;

        /**
         * 是否启用性能指标
         */
        private boolean enableMetrics = true;

        /**
         * 追踪数据保留时间
         */
        private Duration retentionPeriod = Duration.ofDays(3);

        /**
         * 追踪数据存储配置
         */
        private Storage storage = new Storage();

        /**
         * 存储配置类
         */
        @Data
        public static class Storage {
            /**
             * 存储类型
             */
            private StorageType type = StorageType.MEMORY;

            /**
             * 内存存储配置
             */
            private Memory memory = new Memory();

            /**
             * 数据库存储配置
             */
            private Database database = new Database();

            /**
             * 存储类型枚举
             */
            public enum StorageType {
                MEMORY, DATABASE, ELASTICSEARCH
            }

            /**
             * 内存存储配置类
             */
            @Data
            public static class Memory {
                /**
                 * 最大缓存数量
                 */
                private int maxSize = 10000;

                /**
                 * 过期时间
                 */
                private Duration expireAfterWrite = Duration.ofHours(1);
            }

            /**
             * 数据库存储配置类
             */
            @Data
            public static class Database {
                /**
                 * 表名前缀
                 */
                private String tablePrefix = "mq_trace_";

                /**
                 * 批量插入大小
                 */
                private int batchSize = 100;

                /**
                 * 刷新间隔
                 */
                private Duration flushInterval = Duration.ofSeconds(5);
            }
        }
    }

    /**
     * 重试配置类
     */
    @Data
    public static class Retry {
        /**
         * 是否启用重试
         */
        private boolean enabled = true;

        /**
         * 最大重试次数
         */
        private int maxAttempts = 3;

        /**
         * 重试间隔
         */
        private Duration interval = Duration.ofSeconds(1);

        /**
         * 重试倍数
         */
        private double multiplier = 2.0;

        /**
         * 最大重试间隔
         */
        private Duration maxInterval = Duration.ofMinutes(5);

        /**
         * 重试策略
         */
        private RetryStrategy strategy = RetryStrategy.EXPONENTIAL_BACKOFF;

        /**
         * 重试策略枚举
         */
        public enum RetryStrategy {
            FIXED_DELAY, EXPONENTIAL_BACKOFF, CUSTOM
        }
    }

    /**
     * 监控配置类
     */
    @Data
    public static class Monitor {
        /**
         * 是否启用监控
         */
        private boolean enabled = true;

        /**
         * 指标收集间隔
         */
        private Duration collectInterval = Duration.ofSeconds(30);

        /**
         * 是否启用JMX监控
         */
        private boolean enableJmx = true;

        /**
         * 是否启用Micrometer指标
         */
        private boolean enableMicrometer = true;

        /**
         * 自定义指标配置
         */
        private Map<String, String> customMetrics = new HashMap<>();
    }
}
