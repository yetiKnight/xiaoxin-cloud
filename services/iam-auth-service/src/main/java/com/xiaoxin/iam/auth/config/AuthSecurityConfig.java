package com.xiaoxin.iam.auth.config;

import com.xiaoxin.iam.auth.constant.AuthConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 认证服务安全配置类
 * 专门负责认证服务的安全策略配置
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class AuthSecurityConfig {

    /**
     * 认证服务安全过滤器链配置
     * 配置认证服务的安全策略，允许认证相关接口无需认证
     *
     * @param http HTTP安全配置
     * @return 安全过滤器链
     * @throws Exception 配置异常
     */
    @Bean
    public SecurityFilterChain authSecurityFilterChain(HttpSecurity http) throws Exception {
        log.info("认证服务安全过滤器链配置开始");
        
        http
            .authorizeHttpRequests(authz -> authz
                // 允许认证相关接口无需认证
                .requestMatchers(AuthConstants.LOGIN_PATH).permitAll()
                .requestMatchers(AuthConstants.LOGOUT_PATH).permitAll()
                .requestMatchers(AuthConstants.REFRESH_PATH).permitAll()
                .requestMatchers(AuthConstants.REGISTER_PATH).permitAll()
                .requestMatchers(AuthConstants.CAPTCHA_PATH).permitAll()
                .requestMatchers(AuthConstants.OAUTH2_PATH_PREFIX).permitAll()
                .requestMatchers(AuthConstants.OAUTH2_CALLBACK_PATH_PREFIX).permitAll()
                // 允许健康检查和文档接口
                .requestMatchers(AuthConstants.ACTUATOR_PATH_PREFIX).permitAll()
                .requestMatchers(AuthConstants.DOCUMENTATION_PATHS).permitAll()
                // 其他请求需要认证
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable());
            
        log.info("认证服务安全过滤器链配置完成，已允许认证接口匿名访问");
        return http.build();
    }
}
