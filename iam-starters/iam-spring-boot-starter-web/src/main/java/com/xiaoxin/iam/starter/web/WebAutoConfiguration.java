package com.xiaoxin.iam.starter.web;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.xiaoxin.iam.starter.web.config.ApiDocConfig;
import com.xiaoxin.iam.starter.web.config.HttpClientConfig;
import com.xiaoxin.iam.starter.web.config.JacksonConfig;
import com.xiaoxin.iam.starter.web.config.WebProperties;
import com.xiaoxin.iam.starter.web.exception.GlobalExceptionHandler;
import com.xiaoxin.iam.starter.web.filter.RequestLoggingFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Web相关自动配置类
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(WebProperties.class)
@EnableKnife4j
@EnableFeignClients(basePackages = "com.xiaoxin.iam")
@Import({JacksonConfig.class, ApiDocConfig.class, HttpClientConfig.class, com.xiaoxin.iam.starter.web.config.WebMvcConfig.class, com.xiaoxin.iam.starter.web.config.FileUploadConfig.class})
public class WebAutoConfiguration {

    private final WebProperties webProperties;

    /**
     * 全局异常处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    /**
     * CORS配置
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "iam.web.cors", name = "enabled", havingValue = "true", matchIfMissing = false)
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("IAM平台CORS配置已启用");
        
        WebProperties.Cors cors = webProperties.getCors();
        
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 配置允许的源
        if (!cors.getAllowedOrigins().isEmpty()) {
            cors.getAllowedOrigins().forEach(configuration::addAllowedOrigin);
        }
        if (!cors.getAllowedOriginPatterns().isEmpty()) {
            cors.getAllowedOriginPatterns().forEach(configuration::addAllowedOriginPattern);
        }
        
        // 配置允许的方法
        cors.getAllowedMethods().forEach(configuration::addAllowedMethod);
        
        // 配置允许的请求头
        cors.getAllowedHeaders().forEach(configuration::addAllowedHeader);
        
        // 配置暴露的响应头
        cors.getExposedHeaders().forEach(configuration::addExposedHeader);
        
        // 其他配置
        configuration.setAllowCredentials(cors.isAllowCredentials());
        configuration.setMaxAge(cors.getMaxAge().getSeconds());
        
        // 私有网络支持
        if (cors.isAllowPrivateNetwork()) {
            configuration.addAllowedHeader("Access-Control-Request-Private-Network");
        }
        
        // 启用CORS日志
        if (cors.isEnableLogging()) {
            log.debug("CORS配置详情: {}", configuration);
        }
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }

    /**
     * CORS过滤器
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "iam.web.cors", name = "enabled", havingValue = "true", matchIfMissing = false)
    public CorsFilter corsFilter(CorsConfigurationSource corsConfigurationSource) {
        log.info("IAM平台CORS过滤器已启用");
        return new CorsFilter(corsConfigurationSource);
    }

    /**
     * 请求日志过滤器
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "iam.web.request-log", name = "enabled", havingValue = "true", matchIfMissing = false)
    public RequestLoggingFilter requestLoggingFilter() {
        log.info("IAM平台请求日志过滤器已启用");
        return new RequestLoggingFilter(webProperties);
    }
}
