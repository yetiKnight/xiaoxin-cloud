package com.xiaoxin.iam.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * OAuth2重定向处理过滤器
 * 
 * 处理OAuth2授权码模式的重定向请求，确保认证服务能够正确识别网关地址
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Component
@Slf4j
public class OAuth2RedirectFilter implements GlobalFilter, Ordered {
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        
        // 处理OAuth2相关请求
        if (isOAuth2Request(path)) {
            return handleOAuth2Request(exchange, chain);
        }
        
        return chain.filter(exchange);
    }
    
    /**
     * 判断是否为OAuth2请求
     */
    private boolean isOAuth2Request(String path) {
        return path.startsWith("/oauth2/") || 
               path.startsWith("/.well-known/") || 
               path.equals("/userinfo") ||
               path.startsWith("/connect/");
    }
    
    /**
     * 处理OAuth2请求
     */
    private Mono<Void> handleOAuth2Request(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        
        // 添加必要的请求头，确保认证服务知道真实的网关地址
        ServerHttpRequest modifiedRequest = request.mutate()
            .header("X-Forwarded-Host", getGatewayHost(request))
            .header("X-Forwarded-Proto", getGatewayProto(request))
            .header("X-Forwarded-Port", getGatewayPort(request))
            .header("X-Forwarded-For", getClientIp(request))
            .header("X-Original-URI", request.getURI().toString())
            .build();
        
        ServerWebExchange modifiedExchange = exchange.mutate()
            .request(modifiedRequest)
            .build();
        
        log.debug("处理OAuth2请求: {} - 添加转发头信息", path);
        return chain.filter(modifiedExchange);
    }
    
    /**
     * 获取网关主机地址
     */
    private String getGatewayHost(ServerHttpRequest request) {
        // 优先使用Host头，如果没有则使用默认值
        String host = request.getHeaders().getFirst("Host");
        if (host != null && !host.isEmpty()) {
            return host;
        }
        return "localhost:8080";
    }
    
    /**
     * 获取网关协议
     */
    private String getGatewayProto(ServerHttpRequest request) {
        // 检查是否通过HTTPS访问
        String proto = request.getHeaders().getFirst("X-Forwarded-Proto");
        if (proto != null && !proto.isEmpty()) {
            return proto;
        }
        
        // 根据请求URI判断协议
        String scheme = request.getURI().getScheme();
        return scheme != null ? scheme : "http";
    }
    
    /**
     * 获取网关端口
     */
    private String getGatewayPort(ServerHttpRequest request) {
        String host = request.getHeaders().getFirst("Host");
        if (host != null && host.contains(":")) {
            return host.split(":")[1];
        }
        
        // 根据协议返回默认端口
        String scheme = getGatewayProto(request);
        return "https".equals(scheme) ? "443" : "8080";
    }
    
    /**
     * 获取客户端IP地址
     */
    private String getClientIp(ServerHttpRequest request) {
        // 尝试从各种头中获取真实IP
        String[] headers = {
            "X-Forwarded-For",
            "X-Real-IP", 
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
        };
        
        for (String header : headers) {
            String ip = request.getHeaders().getFirst(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // X-Forwarded-For可能包含多个IP，取第一个
                if (ip.contains(",")) {
                    ip = ip.split(",")[0].trim();
                }
                return ip;
            }
        }
        
        // 如果都没有，返回远程地址
        if (request.getRemoteAddress() != null && request.getRemoteAddress().getAddress() != null) {
            return request.getRemoteAddress().getAddress().getHostAddress();
        }
        return "unknown";
    }
    
    @Override
    public int getOrder() {
        // 在认证过滤器之前执行，但在路由过滤器之后
        return -50;
    }
}
