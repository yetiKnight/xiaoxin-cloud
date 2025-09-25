package com.xiaoxin.iam.gateway.controller;

import com.xiaoxin.iam.common.result.Result;
import com.xiaoxin.iam.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务降级处理控制器
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    /**
     * 认证服务降级处理
     */
    @RequestMapping("/auth")
    public Result<Void> authFallback() {
        log.warn("认证服务不可用，执行降级处理");
        return Result.failed(ResultCode.SERVICE_UNAVAILABLE.getCode(), 
                           "认证服务暂时不可用，请稍后重试");
    }

    /**
     * 核心业务服务降级处理
     */
    @RequestMapping("/core")
    public Result<Void> coreFallback() {
        log.warn("核心业务服务不可用，执行降级处理");
        return Result.failed(ResultCode.SERVICE_UNAVAILABLE.getCode(), 
                           "核心业务服务暂时不可用，请稍后重试");
    }

    /**
     * 系统服务降级处理
     */
    @RequestMapping("/system")
    public Result<Void> systemFallback() {
        log.warn("系统服务不可用，执行降级处理");
        return Result.failed(ResultCode.SERVICE_UNAVAILABLE.getCode(), 
                           "系统服务暂时不可用，请稍后重试");
    }

    /**
     * 审计服务降级处理
     */
    @RequestMapping("/audit")
    public Result<Void> auditFallback() {
        log.warn("审计服务不可用，执行降级处理");
        return Result.failed(ResultCode.SERVICE_UNAVAILABLE.getCode(), 
                           "审计服务暂时不可用，请稍后重试");
    }
}
