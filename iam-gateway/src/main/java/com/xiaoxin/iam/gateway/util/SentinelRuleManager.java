package com.xiaoxin.iam.gateway.util;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Sentinel规则管理工具类
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Component
public class SentinelRuleManager {

    /**
     * 动态添加网关限流规则
     */
    public void addGatewayFlowRule(String resource, int count, int burst) {
        Set<GatewayFlowRule> rules = new HashSet<>(GatewayRuleManager.getRules());
        
        GatewayFlowRule rule = new GatewayFlowRule(resource);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setCount(count);
        rule.setBurst(burst);
        rule.setIntervalSec(1);
        
        rules.add(rule);
        GatewayRuleManager.loadRules(rules);
        
        log.info("添加网关限流规则: resource={}, count={}, burst={}", resource, count, burst);
    }

    /**
     * 动态移除网关限流规则
     */
    public void removeGatewayFlowRule(String resource) {
        Set<GatewayFlowRule> rules = new HashSet<>(GatewayRuleManager.getRules());
        rules.removeIf(rule -> rule.getResource().equals(resource));
        GatewayRuleManager.loadRules(rules);
        
        log.info("移除网关限流规则: resource={}", resource);
    }

    /**
     * 动态添加熔断降级规则
     */
    public void addDegradeRule(String resource, CircuitBreakerStrategy strategy, 
                              double threshold, int minRequestAmount, int timeWindow) {
        List<DegradeRule> rules = new ArrayList<>(DegradeRuleManager.getRules());
        
        DegradeRule rule = new DegradeRule(resource);
        rule.setGrade(strategy.getType());
        rule.setCount(threshold);
        rule.setMinRequestAmount(minRequestAmount);
        rule.setTimeWindow(timeWindow);
        rule.setStatIntervalMs(100000); // 100秒统计窗口
        
        if (strategy == CircuitBreakerStrategy.SLOW_REQUEST_RATIO) {
            rule.setSlowRatioThreshold(threshold / 100.0);
        }
        
        rules.add(rule);
        DegradeRuleManager.loadRules(rules);
        
        log.info("添加熔断降级规则: resource={}, strategy={}, threshold={}", 
                resource, strategy, threshold);
    }

    /**
     * 动态移除熔断降级规则
     */
    public void removeDegradeRule(String resource) {
        List<DegradeRule> rules = new ArrayList<>(DegradeRuleManager.getRules());
        rules.removeIf(rule -> rule.getResource().equals(resource));
        DegradeRuleManager.loadRules(rules);
        
        log.info("移除熔断降级规则: resource={}", resource);
    }

    /**
     * 获取当前网关限流规则
     */
    public List<GatewayFlowRule> getGatewayFlowRules() {
        return new ArrayList<>(GatewayRuleManager.getRules());
    }

    /**
     * 获取当前熔断降级规则
     */
    public List<DegradeRule> getDegradeRules() {
        return new ArrayList<>(DegradeRuleManager.getRules());
    }

    /**
     * 清空所有规则
     */
    public void clearAllRules() {
        GatewayRuleManager.loadRules(new HashSet<>());
        DegradeRuleManager.loadRules(new ArrayList<>());
        log.info("已清空所有Sentinel规则");
    }
}
