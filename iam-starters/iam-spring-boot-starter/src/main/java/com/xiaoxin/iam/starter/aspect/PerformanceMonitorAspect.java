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

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 性能监控切面
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Aspect
@Component
@ConditionalOnProperty(prefix = "iam.core", name = "enable-performance-monitor", havingValue = "true", matchIfMissing = true)
public class PerformanceMonitorAspect {

    private static final long SLOW_EXECUTION_THRESHOLD = 1000L; // 1秒

    @Around("execution(* com.xiaoxin.iam..service..*(..))")
    public Object monitorServicePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        return monitorExecution(joinPoint, "Service");
    }

    @Around("execution(* com.xiaoxin.iam..controller..*(..))")
    public Object monitorControllerPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        return monitorExecution(joinPoint, "Controller");
    }

    private Object monitorExecution(ProceedingJoinPoint joinPoint, String layer) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String fullMethodName = className + "." + methodName;
        
        long startTime = System.currentTimeMillis();
        
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;
            
            if (executionTime > SLOW_EXECUTION_THRESHOLD) {
                log.warn("慢查询检测 - {} {}: {}ms", layer, fullMethodName, executionTime);
            } else {
                log.debug("性能监控 - {} {}: {}ms", layer, fullMethodName, executionTime);
            }
            
            return result;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("异常监控 - {} {}: {}ms, 异常: {}", layer, fullMethodName, executionTime, e.getMessage());
            throw e;
        }
    }
}
