package com.xiaoxin.iam.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 限流配置属性
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "rate-limit")
public class RateLimitConfig {

    /**
     * 是否启用限流
     */
    private boolean enabled = true;

    /**
     * 默认令牌桶每秒补充速率
     */
    private int defaultReplenishRate = 10;

    /**
     * 默认令牌桶突发容量
     */
    private int defaultBurstCapacity = 20;

    /**
     * 默认请求的令牌数量
     */
    private int defaultRequestedTokens = 1;
}
