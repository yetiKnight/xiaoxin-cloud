package com.xiaoxin.iam.auth.config;

import com.xiaoxin.iam.auth.service.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 认证管理器配置
 * 配置认证管理器和认证提供者
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AuthenticationManagerConfig {

    private final AuthServiceImpl authService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 用户详情服务
     * 从核心服务获取用户信息
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return authService;
    }

    /**
     * 认证提供者
     * 配置认证提供者使用自定义的用户详情服务和密码编码器
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    /**
     * 认证管理器
     * 从认证配置中获取认证管理器
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}