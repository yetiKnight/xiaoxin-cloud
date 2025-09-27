package com.xiaoxin.iam.starter.security;

import com.xiaoxin.iam.starter.security.config.OAuth2Properties;
import com.xiaoxin.iam.starter.security.oauth2.OAuth2ClientAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * IAM平台安全相关自动配置类
 * 
 * <p>提供以下功能的自动配置：</p>
 * <ul>
 *     <li>Spring Security基础配置</li>
 *     <li>密码编码器</li>
 *     <li>OAuth2客户端功能</li>
 *     <li>默认安全过滤器链</li>
 * </ul>
 * 
 * @author xiaoxin
 * @date 2024-09-26
 */
@Slf4j
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(OAuth2Properties.class)
@EnableWebSecurity
@Import(OAuth2ClientAutoConfiguration.class)
public class SecurityAutoConfiguration {

    /**
     * BCrypt密码编码器
     * 
     * <p>使用BCrypt算法进行密码加密，安全强度高</p>
     */
    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        log.info("IAM平台BCrypt密码编码器已启用");
        return new BCryptPasswordEncoder(12); // 使用强度12
    }

    /**
     * 默认安全过滤器链
     * 
     * <p>提供基础的安全配置，各服务可以覆盖此配置</p>
     */
    @Bean
    @ConditionalOnMissingBean(name = "securityFilterChain")
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        log.info("IAM平台默认安全过滤器链已启用");
        
        http
            // 配置请求授权
            .authorizeHttpRequests(authz -> authz
                // 允许健康检查和API文档
                .requestMatchers("/actuator/**", "/doc.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                // 其他请求需要认证
                .anyRequest().authenticated()
            )
            // 禁用CSRF（API服务通常不需要）
            .csrf(csrf -> csrf.disable())
            // 禁用CORS（由网关统一处理）
            .cors(cors -> cors.disable())
            // 配置会话为无状态
            .sessionManagement(session -> session
                .sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS)
            );
            
        return http.build();
    }
}
