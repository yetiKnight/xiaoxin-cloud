package com.xiaoxin.iam.audit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 审计服务启动类
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackages = "com.xiaoxin.iam")
public class AuditServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuditServiceApplication.class, args);
    }
}
