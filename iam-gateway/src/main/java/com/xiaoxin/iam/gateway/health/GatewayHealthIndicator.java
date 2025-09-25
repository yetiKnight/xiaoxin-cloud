package com.xiaoxin.iam.gateway.health;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 网关健康检查指标
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Component("gateway")
public class GatewayHealthIndicator implements HealthIndicator {

    private final RouteLocator routeLocator;
    private final AtomicLong lastCheckTime = new AtomicLong(0);
    private volatile Health cachedHealth;

    public GatewayHealthIndicator(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    @Override
    public Health health() {
        long currentTime = System.currentTimeMillis();
        
        // 缓存健康检查结果，避免频繁检查
        if (cachedHealth != null && currentTime - lastCheckTime.get() < 10000) { // 10秒缓存
            return cachedHealth;
        }

        try {
            Health.Builder builder = Health.up();
            
            // 检查路由配置
            long routeCount = routeLocator.getRoutes()
                    .timeout(Duration.ofSeconds(5))
                    .count()
                    .block();
            
            builder.withDetail("routes.count", routeCount);
            
            if (routeCount == 0) {
                builder.down().withDetail("reason", "No routes configured");
            }
            
            // 检查JVM内存使用情况
            Runtime runtime = Runtime.getRuntime();
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long usedMemory = totalMemory - freeMemory;
            double memoryUsagePercent = (double) usedMemory / totalMemory * 100;
            
            builder.withDetail("memory.total", totalMemory)
                   .withDetail("memory.used", usedMemory)
                   .withDetail("memory.free", freeMemory)
                   .withDetail("memory.usage.percent", String.format("%.2f%%", memoryUsagePercent));
            
            if (memoryUsagePercent > 90) {
                builder.down().withDetail("reason", "High memory usage: " + String.format("%.2f%%", memoryUsagePercent));
            } else if (memoryUsagePercent > 80) {
                builder.unknown().withDetail("reason", "Medium memory usage: " + String.format("%.2f%%", memoryUsagePercent));
            }
            
            // 检查线程数
            int threadCount = Thread.activeCount();
            builder.withDetail("threads.active", threadCount);
            
            if (threadCount > 500) {
                builder.down().withDetail("reason", "Too many active threads: " + threadCount);
            }
            
            cachedHealth = builder.build();
            lastCheckTime.set(currentTime);
            
            return cachedHealth;
            
        } catch (Exception e) {
            log.error("Gateway health check failed", e);
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .withException(e)
                    .build();
        }
    }
}
