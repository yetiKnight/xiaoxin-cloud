package com.xiaoxin.iam.auth.config;

import com.xiaoxin.iam.auth.constant.AuthConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码配置类
 * 负责密码编码器的配置
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class PasswordConfig {

    /**
     * 密码编码器
     * 使用BCrypt算法进行密码加密，每次加密自动生成随机盐值，能有效防止彩虹表攻击
     * 强度设置为12，提供良好的安全性和性能平衡
     *
     * @return BCrypt密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("认证服务密码编码器已启用，使用BCrypt算法，强度为{}", AuthConstants.BCRYPT_STRENGTH);
        return new BCryptPasswordEncoder(AuthConstants.BCRYPT_STRENGTH);
    }
}