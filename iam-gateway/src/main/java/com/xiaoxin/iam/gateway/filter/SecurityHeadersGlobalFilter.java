package com.xiaoxin.iam.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * 安全响应头全局过滤器
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Component
public class SecurityHeadersGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 在响应提交前添加安全响应头，避免已提交后再修改导致异常
        exchange.getResponse().beforeCommit(() -> {
            addSecurityHeaders(exchange);
            return Mono.empty();
        });
        return chain.filter(exchange);
    }
    
    /**
     * 添加安全响应头
     */
    private void addSecurityHeaders(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        // X-Content-Type-Options: 防止MIME类型嗅探攻击
        response.getHeaders().add("X-Content-Type-Options", "nosniff");
        
        // X-Frame-Options: 防止点击劫持攻击
        response.getHeaders().add("X-Frame-Options", "DENY");
        
        // X-XSS-Protection: 启用XSS过滤器
        response.getHeaders().add("X-XSS-Protection", "1; mode=block");
        
        // Strict-Transport-Security: 强制HTTPS
        response.getHeaders().add("Strict-Transport-Security", 
            "max-age=31536000; includeSubDomains; preload");
        
        // Content-Security-Policy: 内容安全策略
        response.getHeaders().add("Content-Security-Policy", 
            "default-src 'self'; " +
            "script-src 'self' 'unsafe-inline' 'unsafe-eval'; " +
            "style-src 'self' 'unsafe-inline'; " +
            "img-src 'self' data: https:; " +
            "font-src 'self' data:; " +
            "connect-src 'self'; " +
            "frame-ancestors 'none'");
        
        // Referrer-Policy: 控制Referer信息
        response.getHeaders().add("Referrer-Policy", "strict-origin-when-cross-origin");
        
        // Permissions-Policy: 权限策略
        response.getHeaders().add("Permissions-Policy", 
            "camera=(), microphone=(), geolocation=(), payment=()");
        
        // Cache-Control: 缓存控制（对于API响应）
        String path = exchange.getRequest().getURI().getPath();
        if (path != null && path.startsWith("/api/")) {
            response.getHeaders().add("Cache-Control", "no-cache, no-store, must-revalidate");
            response.getHeaders().add("Pragma", "no-cache");
            response.getHeaders().add("Expires", "0");
        }
        
        // 服务器信息隐藏
        response.getHeaders().remove("Server");
        
        // 添加自定义安全标识
        response.getHeaders().add("X-Security-Framework", "IAM-Gateway");
        
        log.debug("安全响应头已添加");
    }

    @Override
    public int getOrder() {
        return -50; // 在认证过滤器之后执行
    }
}
