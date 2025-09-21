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

package com.xiaoxin.iam.starter.mq.serialization;

/**
 * 消息序列化器接口
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public interface MessageSerializer {

    /**
     * 序列化对象为字节数组
     * 
     * @param object 待序列化的对象
     * @return 序列化后的字节数组
     */
    byte[] serialize(Object object);

    /**
     * 反序列化字节数组为对象
     * 
     * @param data 字节数组
     * @param clazz 目标类型
     * @param <T> 泛型类型
     * @return 反序列化后的对象
     */
    <T> T deserialize(byte[] data, Class<T> clazz);

    /**
     * 反序列化字节数组为对象（带类型信息）
     * 
     * @param data 字节数组
     * @return 反序列化后的对象
     */
    Object deserialize(byte[] data);
}
