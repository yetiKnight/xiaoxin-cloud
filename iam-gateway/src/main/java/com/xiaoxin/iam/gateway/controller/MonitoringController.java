package com.xiaoxin.iam.gateway.controller;

import com.xiaoxin.iam.common.result.Result;
import com.xiaoxin.iam.gateway.config.MonitoringConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 监控数据查看控制器
 * 提供简单的监控数据查看接口
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/actuator/gateway")
@RequiredArgsConstructor
public class MonitoringController {

    private final MonitoringConfig.SimpleMetricsCollector metricsCollector;

    /**
     * 获取网关监控指标
     */
    @GetMapping("/metrics")
    public Result<Map<String, Object>> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        // 认证相关指标
        metrics.put("authSuccessCount", metricsCollector.getAuthSuccessCount());
        metrics.put("authFailureCount", metricsCollector.getAuthFailureCount());
        metrics.put("authSuccessRate", String.format("%.2f%%", metricsCollector.getAuthSuccessRate()));
        
        // 限流熔断指标
        metrics.put("rateLimitCount", metricsCollector.getRateLimitCount());
        metrics.put("sentinelBlockCount", metricsCollector.getSentinelBlockCount());
        
        // 请求相关指标
        metrics.put("totalRequestCount", metricsCollector.getTotalRequestCount());
        metrics.put("totalRequestTime", metricsCollector.getTotalRequestTime());
        metrics.put("averageRequestTime", String.format("%.2fms", metricsCollector.getAverageRequestTime()));
        
        // 系统指标
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        double memoryUsage = (double) usedMemory / totalMemory * 100;
        
        metrics.put("jvmMemoryUsed", usedMemory);
        metrics.put("jvmMemoryTotal", totalMemory);
        metrics.put("jvmMemoryUsage", String.format("%.2f%%", memoryUsage));
        metrics.put("availableProcessors", runtime.availableProcessors());
        
        // 时间戳
        metrics.put("timestamp", System.currentTimeMillis());
        
        log.debug("获取网关监控指标: {}", metrics);
        return Result.success(metrics);
    }
    
    /**
     * 重置监控指标
     */
    @GetMapping("/metrics/reset")
    public Result<String> resetMetrics() {
        metricsCollector.reset();
        log.info("监控指标已重置");
        return Result.success("监控指标已重置");
    }
    
    /**
     * 获取网关状态摘要
     */
    @GetMapping("/status")
    public Result<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        
        // 基本状态
        status.put("status", "UP");
        status.put("gatewayVersion", "1.0.0");
        status.put("springCloudGatewayVersion", "4.1.0");
        
        // 请求统计
        long totalRequests = metricsCollector.getTotalRequestCount();
        long authSuccess = metricsCollector.getAuthSuccessCount();
        long authFailure = metricsCollector.getAuthFailureCount();
        
        status.put("totalRequests", totalRequests);
        status.put("authenticationStatus", Map.of(
            "success", authSuccess,
            "failure", authFailure,
            "rate", String.format("%.2f%%", metricsCollector.getAuthSuccessRate())
        ));
        
        // 性能指标
        status.put("performance", Map.of(
            "averageResponseTime", String.format("%.2fms", metricsCollector.getAverageRequestTime()),
            "rateLimitTriggers", metricsCollector.getRateLimitCount(),
            "sentinelBlocks", metricsCollector.getSentinelBlockCount()
        ));
        
        // 运行时间
        status.put("uptime", System.currentTimeMillis());
        
        return Result.success(status);
    }
}
