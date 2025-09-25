package com.xiaoxin.iam.gateway.controller;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStrategy;
import com.xiaoxin.iam.gateway.util.SentinelRuleManager;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.Map;

/**
 * Sentinel规则管理控制器
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/actuator/sentinel")
@RequiredArgsConstructor
@Validated
public class SentinelController {

    private final SentinelRuleManager sentinelRuleManager;

    /**
     * 获取网关限流规则
     */
    @GetMapping("/gateway-flow-rules")
    public Map<String, Object> getGatewayFlowRules() {
        List<GatewayFlowRule> rules = sentinelRuleManager.getGatewayFlowRules();
        return Map.of(
            "success", true,
            "data", rules,
            "count", rules.size()
        );
    }

    /**
     * 添加网关限流规则
     */
    @PostMapping("/gateway-flow-rules")
    public Map<String, Object> addGatewayFlowRule(@Valid @RequestBody GatewayFlowRuleRequest request) {
        sentinelRuleManager.addGatewayFlowRule(request.getResource(), request.getCount(), request.getBurst());
        return Map.of("success", true, "message", "网关限流规则添加成功");
    }

    /**
     * 删除网关限流规则
     */
    @DeleteMapping("/gateway-flow-rules/{resource}")
    public Map<String, Object> removeGatewayFlowRule(@PathVariable @NotBlank String resource) {
        sentinelRuleManager.removeGatewayFlowRule(resource);
        return Map.of("success", true, "message", "网关限流规则删除成功");
    }

    /**
     * 获取熔断降级规则
     */
    @GetMapping("/degrade-rules")
    public Map<String, Object> getDegradeRules() {
        List<DegradeRule> rules = sentinelRuleManager.getDegradeRules();
        return Map.of(
            "success", true,
            "data", rules,
            "count", rules.size()
        );
    }

    /**
     * 添加熔断降级规则
     */
    @PostMapping("/degrade-rules")
    public Map<String, Object> addDegradeRule(@Valid @RequestBody DegradeRuleRequest request) {
        CircuitBreakerStrategy strategy = request.getStrategy() == 0 ? 
            CircuitBreakerStrategy.ERROR_RATIO : CircuitBreakerStrategy.SLOW_REQUEST_RATIO;
        
        sentinelRuleManager.addDegradeRule(request.getResource(), strategy, 
            request.getThreshold(), request.getMinRequestAmount(), request.getTimeWindow());
        
        return Map.of("success", true, "message", "熔断降级规则添加成功");
    }

    /**
     * 删除熔断降级规则
     */
    @DeleteMapping("/degrade-rules/{resource}")
    public Map<String, Object> removeDegradeRule(@PathVariable @NotBlank String resource) {
        sentinelRuleManager.removeDegradeRule(resource);
        return Map.of("success", true, "message", "熔断降级规则删除成功");
    }

    /**
     * 清空所有规则
     */
    @DeleteMapping("/rules")
    public Map<String, Object> clearAllRules() {
        sentinelRuleManager.clearAllRules();
        return Map.of("success", true, "message", "所有规则清空成功");
    }

    /**
     * 网关限流规则请求对象
     */
    @Data
    public static class GatewayFlowRuleRequest {
        @NotBlank(message = "资源名称不能为空")
        private String resource;

        @NotNull(message = "限流阈值不能为空")
        @Positive(message = "限流阈值必须大于0")
        private Integer count;

        @NotNull(message = "突发容量不能为空")
        @Positive(message = "突发容量必须大于0")
        private Integer burst;
    }

    /**
     * 熔断降级规则请求对象
     */
    @Data
    public static class DegradeRuleRequest {
        @NotBlank(message = "资源名称不能为空")
        private String resource;

        @NotNull(message = "熔断策略不能为空")
        private Integer strategy; // 0: 错误率, 1: 慢调用比例

        @NotNull(message = "阈值不能为空")
        @Positive(message = "阈值必须大于0")
        private Double threshold;

        @NotNull(message = "最小请求数不能为空")
        @Positive(message = "最小请求数必须大于0")
        private Integer minRequestAmount;

        @NotNull(message = "熔断时长不能为空")
        @Positive(message = "熔断时长必须大于0")
        private Integer timeWindow;
    }
}
