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

package com.xiaoxin.iam.starter.web;

import com.xiaoxin.iam.common.result.Result;
import com.xiaoxin.iam.starter.config.IamProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 统一响应体处理器
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@RestControllerAdvice
public class IamResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    private IamProperties iamProperties;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 如果已经返回Result类型，则不需要再次包装
        return !Result.class.isAssignableFrom(returnType.getParameterType()) 
                && iamProperties.isEnableUnifiedResponse();
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        
        // 如果返回null，则返回成功结果
        if (body == null) {
            return Result.success();
        }
        
        // 如果已经是Result类型，直接返回
        if (body instanceof Result) {
            return body;
        }
        
        // 包装为统一响应格式
        return Result.success(body);
    }

}
