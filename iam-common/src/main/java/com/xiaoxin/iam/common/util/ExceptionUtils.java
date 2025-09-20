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

package com.xiaoxin.iam.common.util;

import com.xiaoxin.iam.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 异常工具类
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
public class ExceptionUtils {

    private ExceptionUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * 获取异常堆栈信息
     */
    public static String getStackTrace(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        
        try (StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        } catch (Exception e) {
            log.error("获取异常堆栈信息失败", e);
            return throwable.toString();
        }
    }

    /**
     * 获取异常根原因
     */
    public static Throwable getRootCause(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        
        Throwable rootCause = throwable;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }

    /**
     * 获取异常链
     */
    public static List<Throwable> getExceptionChain(Throwable throwable) {
        List<Throwable> chain = new ArrayList<>();
        Throwable current = throwable;
        
        while (current != null && !chain.contains(current)) {
            chain.add(current);
            current = current.getCause();
        }
        
        return chain;
    }

    /**
     * 检查异常是否为业务异常
     */
    public static boolean isBusinessException(Throwable throwable) {
        return throwable instanceof BusinessException;
    }

    /**
     * 检查异常是否为运行时异常
     */
    public static boolean isRuntimeException(Throwable throwable) {
        return throwable instanceof RuntimeException;
    }

    /**
     * 检查异常是否为检查异常
     */
    public static boolean isCheckedException(Throwable throwable) {
        return throwable instanceof Exception && !(throwable instanceof RuntimeException);
    }

    /**
     * 获取异常消息
     */
    public static String getMessage(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        
        if (throwable instanceof BusinessException) {
            return ((BusinessException) throwable).getFullMessage();
        }
        
        return throwable.getMessage();
    }

    /**
     * 获取异常简短消息
     */
    public static String getShortMessage(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        
        String message = throwable.getMessage();
        if (message == null || message.isEmpty()) {
            message = throwable.getClass().getSimpleName();
        }
        
        // 限制消息长度
        if (message.length() > 200) {
            message = message.substring(0, 200) + "...";
        }
        
        return message;
    }

    /**
     * 记录异常日志
     */
    public static void logException(Throwable throwable) {
        logException(throwable, "异常发生");
    }

    /**
     * 记录异常日志
     */
    public static void logException(Throwable throwable, String message) {
        if (throwable == null) {
            return;
        }
        
        if (throwable instanceof BusinessException) {
            log.warn("{}: {}", message, throwable.getMessage());
        } else {
            log.error("{}: {}", message, throwable.getMessage(), throwable);
        }
    }

    /**
     * 记录异常日志（带上下文）
     */
    public static void logException(Throwable throwable, String message, Object... context) {
        if (throwable == null) {
            return;
        }
        
        StringBuilder contextStr = new StringBuilder();
        for (int i = 0; i < context.length; i += 2) {
            if (i + 1 < context.length) {
                contextStr.append(context[i]).append("=").append(context[i + 1]).append(", ");
            }
        }
        
        String fullMessage = message + (contextStr.length() > 0 ? " [" + contextStr.toString().trim() + "]" : "");
        
        if (throwable instanceof BusinessException) {
            log.warn("{}: {}", fullMessage, throwable.getMessage());
        } else {
            log.error("{}: {}", fullMessage, throwable.getMessage(), throwable);
        }
    }

    /**
     * 包装异常为业务异常
     */
    public static BusinessException wrapAsBusinessException(Throwable throwable) {
        if (throwable instanceof BusinessException) {
            return (BusinessException) throwable;
        }
        
        return new BusinessException(throwable.getMessage(), throwable);
    }

    /**
     * 包装异常为业务异常
     */
    public static BusinessException wrapAsBusinessException(Throwable throwable, String message) {
        if (throwable instanceof BusinessException) {
            return (BusinessException) throwable;
        }
        
        return new BusinessException(message, throwable);
    }

    /**
     * 包装异常为业务异常
     */
    public static BusinessException wrapAsBusinessException(Throwable throwable, Integer code, String message) {
        if (throwable instanceof BusinessException) {
            return (BusinessException) throwable;
        }
        
        return new BusinessException(code, message, throwable);
    }

    /**
     * 安全执行，捕获异常并返回默认值
     */
    public static <T> T safeExecute(SafeExecutor<T> executor, T defaultValue) {
        try {
            return executor.execute();
        } catch (Exception e) {
            logException(e, "安全执行失败");
            return defaultValue;
        }
    }

    /**
     * 安全执行，捕获异常并返回默认值
     */
    public static <T> T safeExecute(SafeExecutor<T> executor, T defaultValue, String errorMessage) {
        try {
            return executor.execute();
        } catch (Exception e) {
            logException(e, errorMessage);
            return defaultValue;
        }
    }

    /**
     * 安全执行，不返回值
     */
    public static void safeExecute(SafeRunnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            logException(e, "安全执行失败");
        }
    }

    /**
     * 安全执行，不返回值
     */
    public static void safeExecute(SafeRunnable runnable, String errorMessage) {
        try {
            runnable.run();
        } catch (Exception e) {
            logException(e, errorMessage);
        }
    }

    /**
     * 安全执行器接口
     */
    @FunctionalInterface
    public interface SafeExecutor<T> {
        T execute() throws Exception;
    }

    /**
     * 安全执行器接口（无返回值）
     */
    @FunctionalInterface
    public interface SafeRunnable {
        void run() throws Exception;
    }
}
