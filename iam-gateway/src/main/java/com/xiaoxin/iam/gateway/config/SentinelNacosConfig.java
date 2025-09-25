package com.xiaoxin.iam.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * Sentinel Nacos数据源配置
 * 
 * 注意：Sentinel与Nacos的数据源集成主要通过配置文件实现
 * 详见 application.yml 中的 spring.cloud.sentinel.datasource 配置
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class SentinelNacosConfig {

    @Value("${spring.cloud.nacos.discovery.server-addr}")
    private String nacosServerAddr;

    @Value("${spring.cloud.nacos.discovery.namespace:}")
    private String namespace;

    @Value("${spring.cloud.nacos.discovery.group:DEFAULT_GROUP}")
    private String groupId;

    /**
     * 初始化Nacos数据源配置
     */
    @PostConstruct
    public void initNacosDataSource() {
        log.info("Sentinel Nacos数据源配置已启用");
        log.info("Nacos服务地址: {}", nacosServerAddr);
        log.info("命名空间: {}", namespace.isEmpty() ? "默认" : namespace);
        log.info("分组: {}", groupId);
        
        log.info("规则配置说明:");
        log.info("- 网关限流规则: iam-gateway-flow-rules");
        log.info("- 普通限流规则: iam-gateway-sentinel-flow-rules");
        log.info("- 熔断降级规则: iam-gateway-sentinel-degrade-rules");
        
        log.info("请在Nacos配置中心创建相应的配置文件以启用动态规则更新");
    }
}
