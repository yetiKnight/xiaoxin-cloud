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

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.xiaoxin.iam.starter.mq.config.MqProperties;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * JSON消息序列化器实现
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
public class JsonMessageSerializer implements MessageSerializer {

    private final ObjectMapper objectMapper;
    private final MqProperties.Serialization serializationConfig;

    public JsonMessageSerializer(MqProperties.Serialization serializationConfig) {
        this.serializationConfig = serializationConfig;
        this.objectMapper = createObjectMapper();
    }

    @Override
    public byte[] serialize(Object object) {
        try {
            byte[] data = objectMapper.writeValueAsBytes(object);
            
            // 根据配置决定是否压缩
            if (serializationConfig.isEnableCompression() && 
                data.length > serializationConfig.getCompressionThreshold()) {
                data = compress(data);
                log.debug("Message compressed from {} to {} bytes", 
                        data.length, data.length);
            }
            
            return data;
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize object: {}", object.getClass().getName(), e);
            throw new SerializationException("Failed to serialize object", e);
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        try {
            // 尝试解压缩
            byte[] actualData = tryDecompress(data);
            
            return objectMapper.readValue(actualData, clazz);
        } catch (IOException e) {
            log.error("Failed to deserialize data to class: {}", clazz.getName(), e);
            throw new SerializationException("Failed to deserialize data", e);
        }
    }

    @Override
    public Object deserialize(byte[] data) {
        try {
            // 尝试解压缩
            byte[] actualData = tryDecompress(data);
            
            // 如果包含类型信息，直接反序列化
            if (serializationConfig.getJson().isIncludeTypeInfo()) {
                return objectMapper.readValue(actualData, Object.class);
            } else {
                // 否则返回JsonNode
                return objectMapper.readTree(actualData);
            }
        } catch (IOException e) {
            log.error("Failed to deserialize data", e);
            throw new SerializationException("Failed to deserialize data", e);
        }
    }

    /**
     * 创建ObjectMapper
     */
    private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        // 注册Java 8时间模块
        mapper.registerModule(new JavaTimeModule());
        
        // 配置日期格式
        MqProperties.Serialization.Json jsonConfig = serializationConfig.getJson();
        mapper.setDateFormat(new SimpleDateFormat(jsonConfig.getDateFormat()));
        mapper.setTimeZone(TimeZone.getTimeZone(jsonConfig.getTimeZone()));
        
        // 配置类型信息
        if (jsonConfig.isIncludeTypeInfo()) {
            mapper.activateDefaultTyping(
                    mapper.getPolymorphicTypeValidator(),
                    ObjectMapper.DefaultTyping.NON_FINAL,
                    JsonTypeInfo.As.PROPERTY
            );
        }
        
        return mapper;
    }

    /**
     * 压缩数据
     */
    private byte[] compress(byte[] data) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             GZIPOutputStream gzipOut = new GZIPOutputStream(baos)) {
            
            gzipOut.write(data);
            gzipOut.finish();
            return baos.toByteArray();
        } catch (IOException e) {
            log.warn("Failed to compress data, using original data", e);
            return data;
        }
    }

    /**
     * 尝试解压缩数据
     */
    private byte[] tryDecompress(byte[] data) {
        // 简单检测是否为GZIP格式
        if (data.length >= 2 && data[0] == (byte) 0x1f && data[1] == (byte) 0x8b) {
            try {
                return decompress(data);
            } catch (IOException e) {
                log.warn("Failed to decompress data, using original data", e);
                return data;
            }
        }
        return data;
    }

    /**
     * 解压缩数据
     */
    private byte[] decompress(byte[] data) throws IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             GZIPInputStream gzipIn = new GZIPInputStream(bais);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipIn.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            
            return baos.toByteArray();
        }
    }

    /**
     * 序列化异常
     */
    public static class SerializationException extends RuntimeException {
        public SerializationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
