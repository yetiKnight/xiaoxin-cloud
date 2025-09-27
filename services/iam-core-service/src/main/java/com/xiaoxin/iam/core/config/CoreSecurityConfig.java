package com.xiaoxin.iam.core.config;

import com.xiaoxin.iam.core.constant.CoreConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 核心服务安全配置类
 * 负责核心服务的安全策略配置
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class CoreSecurityConfig {

    /**
     * 核心服务安全过滤器链
     * 配置核心服务的安全策略
     *
     * @param http HttpSecurity配置
     * @return 安全过滤器链
     * @throws Exception 配置异常
     */
    @Bean
    public SecurityFilterChain coreSecurityFilterChain(HttpSecurity http) throws Exception {
        log.info("核心服务安全配置已启用");
        
        http
            // 禁用CSRF，因为这是API服务
            .csrf(AbstractHttpConfigurer::disable)
            // 禁用CORS，由网关统一处理
            .cors(AbstractHttpConfigurer::disable)
            // 配置请求授权
            .authorizeHttpRequests(authz -> authz
                // 内部服务调用接口需要OAuth2认证和相应权限
                .requestMatchers(CoreConstants.INTERNAL_USERS_PATH + "/**")
                    .hasAnyAuthority("SCOPE_internal.read", "SCOPE_internal.write", "SCOPE_user.read", "SCOPE_user.write")
                .requestMatchers(CoreConstants.INTERNAL_ROLES_PATH + "/**")
                    .hasAnyAuthority("SCOPE_internal.read", "SCOPE_internal.write")
                .requestMatchers(CoreConstants.INTERNAL_PERMISSIONS_PATH + "/**")
                    .hasAnyAuthority("SCOPE_internal.read", "SCOPE_internal.write")
                .requestMatchers(CoreConstants.INTERNAL_DEPTS_PATH + "/**")
                    .hasAnyAuthority("SCOPE_internal.read", "SCOPE_internal.write")
                .requestMatchers(CoreConstants.INTERNAL_MENUS_PATH + "/**")
                    .hasAnyAuthority("SCOPE_internal.read", "SCOPE_internal.write")
                
                // 允许健康检查接口
                .requestMatchers(CoreConstants.ACTUATOR_PATH).permitAll()
                
                // 允许API文档接口
                .requestMatchers(CoreConstants.DOC_PATH).permitAll()
                .requestMatchers(CoreConstants.SWAGGER_UI_PATH).permitAll()
                .requestMatchers(CoreConstants.API_DOCS_PATH).permitAll()
                
                // 其他请求需要认证
                .anyRequest().authenticated()
            )
            // 配置OAuth2资源服务器 - JWT验证
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    // JWK URI现在通过配置文件指定，解决循环依赖问题
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
            )
            // 配置会话管理为无状态
            .sessionManagement(session -> session
                .sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS)
            );

        log.info("核心服务安全配置完成，已启用OAuth2资源服务器，内部API路径: {}", CoreConstants.INTERNAL_API_PREFIX);
        return http.build();
    }

    /**
     * JWT认证转换器
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("SCOPE_");
        authoritiesConverter.setAuthoritiesClaimName("scope");

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return jwtConverter;
    }
}
