package com.xiaoxin.iam.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * 系统服务启动类
 * 
 * @author xiaoxin
 * @since 2024-01-01
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.xiaoxin.iam")
public class SystemServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemServiceApplication.class, args);
    }
}
