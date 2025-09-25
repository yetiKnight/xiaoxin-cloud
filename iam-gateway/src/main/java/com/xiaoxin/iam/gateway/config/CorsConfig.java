package com.xiaoxin.iam.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * CORS跨域配置
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class CorsConfig {

    private static final String MAX_AGE = "3600";
    
    /**
     * 配置CORS过滤器
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        
        // 允许的源（生产环境应该配置具体域名）
        corsConfiguration.addAllowedOriginPattern("*");
        
        // 允许的请求头
        corsConfiguration.addAllowedHeader("*");
        
        // 允许的HTTP方法
        corsConfiguration.addAllowedMethod("*");
        
        // 是否允许携带Cookie
        corsConfiguration.setAllowCredentials(true);
        
        // 预检请求的缓存时间（秒）
        corsConfiguration.setMaxAge(3600L);
        
        // 暴露的响应头
        corsConfiguration.addExposedHeader("X-Request-Id");
        corsConfiguration.addExposedHeader("X-Response-Time");
        corsConfiguration.addExposedHeader("X-Gateway-Version");
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        
        log.info("CORS跨域配置已启用");
        return new CorsWebFilter(source);
    }
    
    /**
     * 处理预检请求的过滤器
     */
    @Bean
    public WebFilter corsResponseHeaderFilter() {
        return new WebFilter() {
            @Override
            @NonNull
            public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
                ServerHttpRequest request = exchange.getRequest();
                ServerHttpResponse response = exchange.getResponse();
                
                // 处理预检请求
                if (CorsUtils.isPreFlightRequest(request)) {
                    log.debug("处理CORS预检请求: {} {}", request.getMethod(), request.getURI().getPath());
                    
                    response.getHeaders().add("Access-Control-Allow-Origin", 
                        request.getHeaders().getOrigin());
                    response.getHeaders().add("Access-Control-Allow-Methods", 
                        "GET, POST, PUT, DELETE, OPTIONS");
                    response.getHeaders().add("Access-Control-Allow-Headers", 
                        "Origin, X-Requested-With, Content-Type, Accept, Authorization, X-Request-Id");
                    response.getHeaders().add("Access-Control-Allow-Credentials", "true");
                    response.getHeaders().add("Access-Control-Max-Age", MAX_AGE);
                    
                    response.setStatusCode(HttpStatus.OK);
                    return response.setComplete();
                }
                
                return chain.filter(exchange);
            }
        };
    }
}
