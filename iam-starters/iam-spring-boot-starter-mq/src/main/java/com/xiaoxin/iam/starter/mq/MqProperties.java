package com.xiaoxin.iam.starter.mq;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
     * 是否启用消息队列
     */
    private boolean enabled = true;

    /**
     * 消息队列类型
     */
    private String type = "rocketmq";

    /**
     * 生产者配置
     */
    private Producer producer = new Producer();

    /**
     * 消费者配置
     */
    private Consumer consumer = new Consumer();

    @Data
    public static class Producer {
        /**
         * 生产者组名
         */
        private String group = "iam-producer-group";

        /**
         * 发送超时时间
         */
        private int sendTimeout = 3000;

        /**
         * 重试次数
         */
        private int retryTimes = 2;
    }

    @Data
    public static class Consumer {
        /**
         * 消费者组名
         */
        private String group = "iam-consumer-group";

        /**
         * 消费模式
         */
        private String consumeMode = "CLUSTERING";

        /**
         * 消费线程数
         */
        private int consumeThreadMin = 20;

        /**
         * 消费线程数
         */
        private int consumeThreadMax = 64;
    }
}
