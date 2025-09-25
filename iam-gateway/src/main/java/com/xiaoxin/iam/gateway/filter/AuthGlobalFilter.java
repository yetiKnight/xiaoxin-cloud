package com.xiaoxin.iam.gateway.filter;

import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import com.xiaoxin.iam.common.constant.CommonConstants;
import com.xiaoxin.iam.common.result.Result;
import com.xiaoxin.iam.common.result.ResultCode;
import com.xiaoxin.iam.common.utils.JsonUtils;
import com.xiaoxin.iam.gateway.config.AuthProperties;
import com.xiaoxin.iam.gateway.config.SecurityProperties;
import com.xiaoxin.iam.gateway.config.MonitoringConfig;
import com.xiaoxin.iam.gateway.util.JwtUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * 认证全局过滤器
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final AuthProperties authProperties;
    private final SecurityProperties securityProperties;
    private final JwtUtils jwtUtils;
    private final MonitoringConfig.SimpleMetricsCollector metricsCollector;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String path = request.getURI().getPath();
        String method = request.getMethod().name();

        log.debug("网关请求处理: {} {}", method, path);

        // 检查是否启用认证
        if (!securityProperties.getAuth().getEnabled()) {
            log.debug("认证已禁用，直接放行: {}", path);
            return chain.filter(exchange);
        }

        // 检查是否在白名单中
        if (isWhitelistPath(path)) {
            log.debug("白名单路径，直接放行: {}", path);
            return chain.filter(exchange);
        }

        // 获取认证Token
        String token = getAuthToken(request);
        if (!StringUtils.hasText(token)) {
            log.warn("请求缺少认证Token: {} {}", method, path);
            return unauthorizedResponse(response, "缺少认证Token");
        }

        // 验证JWT Token
        JwtUtils.JwtValidationResult validationResult = jwtUtils.validateToken(token);
        if (!validationResult.isValid()) {
            log.warn("JWT Token验证失败: {} - {}", validationResult.getMessage(), path);
            // 记录认证失败指标
            metricsCollector.recordAuthFailure();
            if (validationResult.isExpired()) {
                return unauthorizedResponse(response, "Token已过期");
            }
            return unauthorizedResponse(response, "Token无效: " + validationResult.getMessage());
        }

        // 记录认证成功指标
        metricsCollector.recordAuthSuccess();

        // 检查管理员权限
        if (isAdminPath(path) && !hasAdminRole(validationResult.getRoles())) {
            log.warn("用户 {} 尝试访问管理员路径: {}", validationResult.getUsername(), path);
            return forbiddenResponse(response);
        }

        // 添加请求头信息
        ServerHttpRequest modifiedRequest = request.mutate()
                .header("X-Request-Path", path)
                .header("X-Request-Method", method)
                .header("X-Gateway-Time", String.valueOf(System.currentTimeMillis()))
                .header("X-User-Id", validationResult.getUserId())
                .header("X-Username", validationResult.getUsername())
                .header("X-User-Roles", validationResult.getRoles())
                .header("X-User-Permissions", validationResult.getPermissions())
                .build();

        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    /**
     * 检查路径是否在白名单中
     */
    private boolean isWhitelistPath(String path) {
        // 优先使用SecurityProperties中的白名单
        List<String> whitelist = securityProperties.getAuth().getWhitelist();
        if (CollectionUtils.isEmpty(whitelist)) {
            // 回退到AuthProperties中的白名单
            whitelist = authProperties.getWhitelist();
        }
        
        if (CollectionUtils.isEmpty(whitelist)) {
            log.warn("白名单配置为空，所有请求都需要认证");
            return false;
        }
        
        return whitelist.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    /**
     * 检查路径是否需要管理员权限
     */
    private boolean isAdminPath(String path) {
        List<String> adminPaths = securityProperties.getAuth().getAdminPaths();
        if (CollectionUtils.isEmpty(adminPaths)) {
            return false;
        }
        
        return adminPaths.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    /**
     * 检查是否具有管理员角色
     */
    private boolean hasAdminRole(String roles) {
        if (!StringUtils.hasText(roles)) {
            return false;
        }
        
        // 检查是否包含管理员角色（admin或system）
        return roles.contains("admin") || roles.contains("system") || roles.contains("ADMIN") || roles.contains("SYSTEM");
    }

    /**
     * 获取认证Token
     */
    private String getAuthToken(ServerHttpRequest request) {
        // 优先从Header中获取
        String tokenHeader = securityProperties.getAuth().getTokenHeader();
        String authorization = request.getHeaders().getFirst(tokenHeader);
        if (authorization != null && StringUtils.hasText(authorization)) {
            String tokenPrefix = securityProperties.getJwt().getTokenPrefix();
            if (authorization.startsWith(tokenPrefix)) {
                return authorization.substring(tokenPrefix.length());
            }
        }

        // 从查询参数中获取
        String tokenParam = securityProperties.getAuth().getTokenParam();
        String tokenFromParam = request.getQueryParams().getFirst(tokenParam);
        if (StringUtils.hasText(tokenFromParam)) {
            return tokenFromParam;
        }

        return null;
    }

    /**
     * 返回未认证响应
     */
    private Mono<Void> unauthorizedResponse(ServerHttpResponse response, String message) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(CommonConstants.HEADER_CONTENT_TYPE, "application/json;charset=UTF-8");

        Result<Void> result = Result.failed(ResultCode.UNAUTHORIZED.getCode(), message);
        String body = JsonUtils.toJson(result);

        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }

    /**
     * 返回禁止访问响应
     */
    private Mono<Void> forbiddenResponse(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.getHeaders().add(CommonConstants.HEADER_CONTENT_TYPE, "application/json;charset=UTF-8");

        Result<Void> result = Result.failed(ResultCode.FORBIDDEN);
        String body = JsonUtils.toJson(result);

        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }

    @Override
    public int getOrder() {
        return -100; // 认证过滤器优先级较高
    }
}
