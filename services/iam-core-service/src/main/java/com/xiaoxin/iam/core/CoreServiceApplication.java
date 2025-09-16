package com.xiaoxin.iam.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 核心业务服务启动类
 * 
 * @author xiaoxin
 * @since 2024-01-01
 */
@SpringBootApplication
@EnableDiscoveryClient
public class CoreServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreServiceApplication.class, args);
    }
}
