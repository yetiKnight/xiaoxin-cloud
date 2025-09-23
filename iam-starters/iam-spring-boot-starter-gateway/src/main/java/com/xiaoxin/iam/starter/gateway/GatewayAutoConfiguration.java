package com.xiaoxin.iam.starter.gateway;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * 网关相关自动配置类
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class GatewayAutoConfiguration {

    /**
     * IAM网关配置
     */
    @Bean("iamGatewayProperties")
    @ConditionalOnMissingBean
    public GatewayProperties iamGatewayProperties() {
        log.info("IAM平台网关配置已启用");
        return new GatewayProperties();
    }

    /**
     * 限流Key解析器 - 基于IP地址
     */
    @Bean
    @ConditionalOnMissingBean
    public KeyResolver ipKeyResolver() {
        log.info("IAM平台网关IP限流Key解析器已启用");
        return exchange -> Mono.just(
            exchange.getRequest()
                .getRemoteAddress()
                .getAddress()
                .getHostAddress()
        );
    }

    /**
     * Caffeine缓存管理器 - 用于LoadBalancer缓存
     */
    @Bean
    @ConditionalOnMissingBean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
            .initialCapacity(100)
            .maximumSize(1000)
            .expireAfterAccess(Duration.ofMinutes(10))
            .recordStats());
        log.info("IAM平台网关Caffeine缓存管理器已启用");
        return cacheManager;
    }
}
