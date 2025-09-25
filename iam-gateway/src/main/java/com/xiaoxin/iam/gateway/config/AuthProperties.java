package com.xiaoxin.iam.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 认证相关配置属性
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {

    /**
     * 白名单路径，无需认证
     */
    private List<String> whitelist;
}
