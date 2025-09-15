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

package com.xiaoxin.iam.common.result;

/**
 * 统一响应结果接口
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public interface IResult {

    /**
     * 获取响应码
     * @return 响应码
     */
    Integer getCode();

    /**
     * 获取响应消息
     * @return 响应消息
     */
    String getMessage();

}
