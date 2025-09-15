package com.xiaoxin.iam.starter;

import com.xiaoxin.iam.starter.web.GlobalExceptionHandler;
import com.xiaoxin.iam.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * IAM平台自动配置类
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class IamAutoConfiguration {

    /**
     * 全局异常处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler() {
        log.info("IAM平台全局异常处理器已启用");
        return new GlobalExceptionHandler();
    }

    /**
     * 统一响应结果配置
     */
    @Bean
    @ConditionalOnMissingBean
    public Result result() {
        log.info("IAM平台统一响应结果已启用");
        return new Result();
    }
}
