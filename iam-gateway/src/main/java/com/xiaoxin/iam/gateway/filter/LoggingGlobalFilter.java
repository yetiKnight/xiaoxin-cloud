package com.xiaoxin.iam.gateway.filter;

import com.xiaoxin.iam.gateway.config.MonitoringConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * 日志记录全局过滤器
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Component
public class LoggingGlobalFilter implements GlobalFilter, Ordered {

    /**
     * 慢请求阈值（毫秒）
     */
    private static final long SLOW_REQUEST_THRESHOLD = 3000L;

    /**
     * 请求ID头名称
     */
    private static final String REQUEST_ID_HEADER = "X-Request-ID";

    @Autowired
    private MonitoringConfig.SimpleMetricsCollector metricsCollector;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        final long startTime = System.currentTimeMillis();
        
        // 记录开始时间
        
        String requestId = request.getHeaders().getFirst(REQUEST_ID_HEADER);
        if (requestId == null) {
            requestId = generateRequestId();
        }
        final String finalRequestId = requestId;
        final String requestPath = request.getURI().getPath();
        
        // 记录请求信息
        log.info("请求开始 - ID: {}, 方法: {}, 路径: {}, 客户端IP: {}", 
            finalRequestId, 
            request.getMethod(), 
            requestPath,
            getClientIp(request)
        );
        
        // 添加请求ID到请求头
        ServerHttpRequest modifiedRequest = request.mutate()
            .header(REQUEST_ID_HEADER, finalRequestId)
            .build();
        
        return chain.filter(exchange.mutate().request(modifiedRequest).build())
            .doFinally(signalType -> {
                ServerHttpResponse response = exchange.getResponse();
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                
                // 记录监控指标
                metricsCollector.recordRequestDuration(duration);
                
                // 记录响应信息
                log.info("请求完成 - ID: {}, 状态码: {}, 耗时: {}ms", 
                    finalRequestId, 
                    response.getStatusCode(),
                    duration
                );
                
                // 记录慢请求
                if (duration > SLOW_REQUEST_THRESHOLD) {
                    log.warn("慢请求告警 - ID: {}, 路径: {}, 耗时: {}ms", 
                        finalRequestId, 
                        requestPath,
                        duration
                    );
                }
            });
    }

    /**
     * 生成请求ID
     * 使用UUID确保唯一性
     */
    private String generateRequestId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取客户端真实IP
     */
    private String getClientIp(ServerHttpRequest request) {
        String xForwardedFor = request.getHeaders().getFirst("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeaders().getFirst("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        var remoteAddress = request.getRemoteAddress();
        return remoteAddress != null ? 
            remoteAddress.getAddress().getHostAddress() : "unknown";
    }

    @Override
    public int getOrder() {
        return -200; // 日志过滤器优先级最高
    }
}
