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

package com.xiaoxin.iam.common.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 * 用于标记需要记录操作日志的方法
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    /**
     * 操作模块
     * @return 模块名称
     */
    String module() default "";

    /**
     * 操作名称
     * @return 操作描述
     */
    String operation() default "";

    /**
     * 操作类型
     * @return 操作类型
     */
    OperationType type() default OperationType.OTHER;

    /**
     * 是否保存请求参数
     * @return 是否保存参数
     */
    boolean saveRequestData() default false;

    /**
     * 是否保存响应参数
     * @return 是否保存响应
     */
    boolean saveResponseData() default false;

    /**
     * 操作类型枚举
     */
    enum OperationType {
        /** 查询 */
        SELECT,
        /** 新增 */
        INSERT,
        /** 修改 */
        UPDATE,
        /** 删除 */
        DELETE,
        /** 授权 */
        GRANT,
        /** 导出 */
        EXPORT,
        /** 导入 */
        IMPORT,
        /** 强退 */
        FORCE,
        /** 清空数据 */
        CLEAN,
        /** 其它 */
        OTHER
    }
}
