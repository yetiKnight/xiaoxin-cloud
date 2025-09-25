package com.xiaoxin.iam.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 简化监控配置类
 * 基于内存的基本计数器，为SkyWalking集成做准备
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class MonitoringConfig {

    /**
     * 基本监控指标收集器
     */
    @Bean
    public SimpleMetricsCollector simpleMetricsCollector() {
        log.info("创建简化版监控指标收集器");
        return new SimpleMetricsCollector();
    }
    
    /**
     * 简化版监控指标收集器
     * 使用内存计数器，避免第三方依赖
     */
    public static class SimpleMetricsCollector {
        
        // 基本计数器
        private final AtomicLong authSuccessCount = new AtomicLong(0);
        private final AtomicLong authFailureCount = new AtomicLong(0);
        private final AtomicLong rateLimitCount = new AtomicLong(0);
        private final AtomicLong sentinelBlockCount = new AtomicLong(0);
        private final AtomicLong totalRequestCount = new AtomicLong(0);
        
        // 时间统计
        private final AtomicLong totalRequestTime = new AtomicLong(0);
        
        /**
         * 记录认证成功
         */
        public void recordAuthSuccess() {
            authSuccessCount.incrementAndGet();
        }
        
        /**
         * 记录认证失败
         */
        public void recordAuthFailure() {
            authFailureCount.incrementAndGet();
        }
        
        /**
         * 记录限流触发
         */
        public void recordRateLimitTriggered() {
            rateLimitCount.incrementAndGet();
        }
        
        /**
         * 记录熔断触发
         */
        public void recordSentinelBlocked() {
            sentinelBlockCount.incrementAndGet();
        }
        
        /**
         * 记录请求处理时间
         */
        public void recordRequestDuration(long duration) {
            totalRequestCount.incrementAndGet();
            totalRequestTime.addAndGet(duration);
        }
        
        // Getter方法用于获取统计数据
        public long getAuthSuccessCount() { return authSuccessCount.get(); }
        public long getAuthFailureCount() { return authFailureCount.get(); }
        public long getRateLimitCount() { return rateLimitCount.get(); }
        public long getSentinelBlockCount() { return sentinelBlockCount.get(); }
        public long getTotalRequestCount() { return totalRequestCount.get(); }
        public long getTotalRequestTime() { return totalRequestTime.get(); }
        
        /**
         * 获取平均请求时间
         */
        public double getAverageRequestTime() {
            long count = totalRequestCount.get();
            return count > 0 ? (double) totalRequestTime.get() / count : 0.0;
        }
        
        /**
         * 获取认证成功率
         */
        public double getAuthSuccessRate() {
            long total = authSuccessCount.get() + authFailureCount.get();
            return total > 0 ? (double) authSuccessCount.get() / total * 100 : 0.0;
        }
        
        /**
         * 重置所有计数器
         */
        public void reset() {
            authSuccessCount.set(0);
            authFailureCount.set(0);
            rateLimitCount.set(0);
            sentinelBlockCount.set(0);
            totalRequestCount.set(0);
            totalRequestTime.set(0);
        }
    }
}