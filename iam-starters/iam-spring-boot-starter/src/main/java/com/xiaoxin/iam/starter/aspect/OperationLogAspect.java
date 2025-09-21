/*
 * Copyright 2024 xiaoxin. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xiaoxin.iam.starter.aspect;

import com.xiaoxin.iam.common.annotation.OperationLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 操作日志切面
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Aspect
@Component
@ConditionalOnProperty(prefix = "iam.core", name = "enable-operation-log", havingValue = "true", matchIfMissing = true)
public class OperationLogAspect {

    @Around("@annotation(com.xiaoxin.iam.common.annotation.OperationLog)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        // 获取方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        OperationLog operationLog = method.getAnnotation(OperationLog.class);
        
        // 获取请求信息
        String requestInfo = getRequestInfo();
        
        try {
            // 执行方法
            Object result = joinPoint.proceed();
            
            // 记录成功日志
            long endTime = System.currentTimeMillis();
            log.info("操作日志 - 模块: {}, 操作: {}, 耗时: {}ms, 请求: {}", 
                    operationLog.module(), operationLog.operation(), 
                    endTime - startTime, requestInfo);
            
            return result;
        } catch (Exception e) {
            // 记录失败日志
            long endTime = System.currentTimeMillis();
            log.error("操作日志 - 模块: {}, 操作: {}, 耗时: {}ms, 请求: {}, 异常: {}", 
                    operationLog.module(), operationLog.operation(), 
                    endTime - startTime, requestInfo, e.getMessage());
            throw e;
        }
    }

    private String getRequestInfo() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                return String.format("%s %s", request.getMethod(), request.getRequestURI());
            }
        } catch (Exception e) {
            log.debug("获取请求信息失败: {}", e.getMessage());
        }
        return "Unknown";
    }
}
