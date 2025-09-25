package com.xiaoxin.iam.gateway.config;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Sentinel熔断降级配置
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "sentinel.degrade")
public class SentinelCircuitBreakerConfig {

    /** 是否启用熔断器 */
    private boolean enabled = true;
    
    /** 失败率阈值（百分比） */
    private double failureRateThreshold = 50.0;
    
    /** 慢调用率阈值（百分比） */
    private double slowCallRateThreshold = 50.0;
    
    /** 慢调用时间阈值（毫秒） */
    private long slowCallDurationThreshold = 2000L;
    
    /** 最小调用次数 */
    private int minimumNumberOfCalls = 10;
    
    /** 滑动窗口大小（秒） */
    private int slidingWindowSize = 100;
    
    /** 等待时间（毫秒） */
    private long waitDurationInOpenState = 10000L;

    /**
     * 初始化熔断降级规则
     */
    @PostConstruct
    public void initDegradeRules() {
        if (!enabled) {
            log.info("Sentinel熔断器功能已禁用");
            return;
        }

        log.info("初始化Sentinel熔断降级规则");
        
        List<DegradeRule> rules = new ArrayList<>();

        // 认证服务熔断规则
        rules.add(createDegradeRule("iam-auth-service", 
            CircuitBreakerStrategy.SLOW_REQUEST_RATIO, 
            slowCallRateThreshold, slowCallDurationThreshold,
            minimumNumberOfCalls, slidingWindowSize));

        rules.add(createDegradeRule("iam-auth-service", 
            CircuitBreakerStrategy.ERROR_RATIO, 
            failureRateThreshold, 0,
            minimumNumberOfCalls, slidingWindowSize));

        // 核心服务熔断规则
        rules.add(createDegradeRule("iam-core-service", 
            CircuitBreakerStrategy.SLOW_REQUEST_RATIO, 
            slowCallRateThreshold, slowCallDurationThreshold,
            minimumNumberOfCalls, slidingWindowSize));

        rules.add(createDegradeRule("iam-core-service", 
            CircuitBreakerStrategy.ERROR_RATIO, 
            failureRateThreshold, 0,
            minimumNumberOfCalls, slidingWindowSize));

        // 系统服务熔断规则
        rules.add(createDegradeRule("iam-system-service", 
            CircuitBreakerStrategy.SLOW_REQUEST_RATIO, 
            slowCallRateThreshold, slowCallDurationThreshold,
            minimumNumberOfCalls, slidingWindowSize));

        rules.add(createDegradeRule("iam-system-service", 
            CircuitBreakerStrategy.ERROR_RATIO, 
            failureRateThreshold, 0,
            minimumNumberOfCalls, slidingWindowSize));

        // 审计服务熔断规则
        rules.add(createDegradeRule("iam-audit-service", 
            CircuitBreakerStrategy.SLOW_REQUEST_RATIO, 
            slowCallRateThreshold, slowCallDurationThreshold,
            minimumNumberOfCalls, slidingWindowSize));

        rules.add(createDegradeRule("iam-audit-service", 
            CircuitBreakerStrategy.ERROR_RATIO, 
            failureRateThreshold, 0,
            minimumNumberOfCalls, slidingWindowSize));

        DegradeRuleManager.loadRules(rules);
        log.info("已加载Sentinel熔断降级规则，数量: {}", rules.size());
    }

    /**
     * 创建熔断降级规则
     */
    private DegradeRule createDegradeRule(String resource, 
                                        CircuitBreakerStrategy strategy,
                                        double threshold,
                                        long slowCallDurationThreshold,
                                        int minRequestAmount,
                                        int statIntervalMs) {
        DegradeRule rule = new DegradeRule(resource);
        rule.setGrade(strategy.getType());
        rule.setCount(threshold);
        rule.setSlowRatioThreshold(threshold / 100.0);
        rule.setMinRequestAmount(minRequestAmount);
        rule.setStatIntervalMs(statIntervalMs * 1000); // 转换为毫秒
        rule.setTimeWindow((int) (waitDurationInOpenState / 1000)); // 转换为秒

        if (strategy == CircuitBreakerStrategy.SLOW_REQUEST_RATIO) {
            rule.setSlowRatioThreshold(threshold / 100.0);
            // Sentinel中慢调用时间阈值通过系统属性设置
            System.setProperty("csp.sentinel.statistic.max.rt", 
                String.valueOf(slowCallDurationThreshold));
        }

        return rule;
    }
    
    // Getter和Setter方法（由于使用@ConfigurationProperties需要）
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    
    public double getFailureRateThreshold() { return failureRateThreshold; }
    public void setFailureRateThreshold(double failureRateThreshold) { this.failureRateThreshold = failureRateThreshold; }
    
    public double getSlowCallRateThreshold() { return slowCallRateThreshold; }
    public void setSlowCallRateThreshold(double slowCallRateThreshold) { this.slowCallRateThreshold = slowCallRateThreshold; }
    
    public long getSlowCallDurationThreshold() { return slowCallDurationThreshold; }
    public void setSlowCallDurationThreshold(long slowCallDurationThreshold) { this.slowCallDurationThreshold = slowCallDurationThreshold; }
    
    public int getMinimumNumberOfCalls() { return minimumNumberOfCalls; }
    public void setMinimumNumberOfCalls(int minimumNumberOfCalls) { this.minimumNumberOfCalls = minimumNumberOfCalls; }
    
    public int getSlidingWindowSize() { return slidingWindowSize; }
    public void setSlidingWindowSize(int slidingWindowSize) { this.slidingWindowSize = slidingWindowSize; }
    
    public long getWaitDurationInOpenState() { return waitDurationInOpenState; }
    public void setWaitDurationInOpenState(long waitDurationInOpenState) { this.waitDurationInOpenState = waitDurationInOpenState; }
}
