package com.xiaoxin.iam.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 重试配置属性
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "retry")
public class RetryConfig {

    /**
     * 是否启用重试
     */
    private boolean enabled = true;

    /**
     * 最大重试次数
     */
    private int maxAttempts = 3;

    /**
     * 退避策略配置
     */
    private Backoff backoff = new Backoff();

    @Data
    public static class Backoff {
        /**
         * 初始间隔（毫秒）
         */
        private long initialInterval = 1000L;

        /**
         * 最大间隔（毫秒）
         */
        private long maxInterval = 10000L;

        /**
         * 倍数
         */
        private double multiplier = 2.0;
    }
}
