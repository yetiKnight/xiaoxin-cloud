package com.xiaoxin.iam.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * JSON工具类
 * 基于Jackson提供JSON序列化和反序列化功能
 *
 * @author xiaoxin
 * @since 1.0.0
 */
public class JsonUtils {

    /**
     * ObjectMapper实例
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // 配置ObjectMapper
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    /**
     * 私有构造函数，防止实例化
     */
    private JsonUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ==================== 对象转JSON ====================

    /**
     * 对象转JSON字符串
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("对象转JSON失败", e);
        }
    }

    /**
     * 对象转JSON字符串（美化格式）
     */
    public static String toPrettyJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("对象转JSON失败", e);
        }
    }

    /**
     * 对象转JSON字节数组
     */
    public static byte[] toJsonBytes(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("对象转JSON失败", e);
        }
    }

    // ==================== JSON转对象 ====================

    /**
     * JSON字符串转对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON转对象失败", e);
        }
    }

    /**
     * JSON字符串转对象（使用TypeReference）
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON转对象失败", e);
        }
    }

    /**
     * JSON字节数组转对象
     */
    public static <T> T fromJson(byte[] jsonBytes, Class<T> clazz) {
        if (jsonBytes == null || jsonBytes.length == 0) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(jsonBytes, clazz);
        } catch (IOException e) {
            throw new RuntimeException("JSON转对象失败", e);
        }
    }

    /**
     * JSON字节数组转对象（使用TypeReference）
     */
    public static <T> T fromJson(byte[] jsonBytes, TypeReference<T> typeReference) {
        if (jsonBytes == null || jsonBytes.length == 0) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(jsonBytes, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("JSON转对象失败", e);
        }
    }

    // ==================== JSON转Map ====================

    /**
     * JSON字符串转Map
     */
    public static Map<String, Object> toMap(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON转Map失败", e);
        }
    }

    /**
     * 对象转Map
     */
    public static Map<String, Object> toMap(Object obj) {
        if (obj == null) {
            return null;
        }
        return OBJECT_MAPPER.convertValue(obj, new TypeReference<Map<String, Object>>() {});
    }

    /**
     * Map转对象
     */
    public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
        if (map == null) {
            return null;
        }
        return OBJECT_MAPPER.convertValue(map, clazz);
    }

    // ==================== JSON转List ====================

    /**
     * JSON字符串转List
     */
    public static <T> List<T> toList(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON转List失败", e);
        }
    }

    /**
     * JSON字符串转List（使用TypeReference）
     */
    public static <T> List<T> toList(String json, TypeReference<List<T>> typeReference) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON转List失败", e);
        }
    }

    // ==================== 对象转换 ====================

    /**
     * 对象转换
     */
    public static <T> T convert(Object obj, Class<T> clazz) {
        if (obj == null) {
            return null;
        }
        return OBJECT_MAPPER.convertValue(obj, clazz);
    }

    /**
     * 对象转换（使用TypeReference）
     */
    public static <T> T convert(Object obj, TypeReference<T> typeReference) {
        if (obj == null) {
            return null;
        }
        return OBJECT_MAPPER.convertValue(obj, typeReference);
    }

    // ==================== JSON验证 ====================

    /**
     * 验证JSON字符串是否有效
     */
    public static boolean isValidJson(String json) {
        if (StringUtils.isBlank(json)) {
            return false;
        }
        try {
            OBJECT_MAPPER.readTree(json);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    /**
     * 验证JSON字符串是否为数组
     */
    public static boolean isJsonArray(String json) {
        if (StringUtils.isBlank(json)) {
            return false;
        }
        try {
            return OBJECT_MAPPER.readTree(json).isArray();
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    /**
     * 验证JSON字符串是否为对象
     */
    public static boolean isJsonObject(String json) {
        if (StringUtils.isBlank(json)) {
            return false;
        }
        try {
            return OBJECT_MAPPER.readTree(json).isObject();
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    // ==================== JSON操作 ====================

    /**
     * 获取JSON字符串中的值
     */
    public static String getString(String json, String key) {
        if (StringUtils.isBlank(json) || StringUtils.isBlank(key)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readTree(json).get(key).asText();
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * 获取JSON字符串中的整数值
     */
    public static Integer getInt(String json, String key) {
        if (StringUtils.isBlank(json) || StringUtils.isBlank(key)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readTree(json).get(key).asInt();
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * 获取JSON字符串中的长整数值
     */
    public static Long getLong(String json, String key) {
        if (StringUtils.isBlank(json) || StringUtils.isBlank(key)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readTree(json).get(key).asLong();
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * 获取JSON字符串中的布尔值
     */
    public static Boolean getBoolean(String json, String key) {
        if (StringUtils.isBlank(json) || StringUtils.isBlank(key)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readTree(json).get(key).asBoolean();
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * 获取JSON字符串中的双精度值
     */
    public static Double getDouble(String json, String key) {
        if (StringUtils.isBlank(json) || StringUtils.isBlank(key)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readTree(json).get(key).asDouble();
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    // ==================== JSON合并 ====================

    /**
     * 合并两个JSON字符串
     */
    public static String mergeJson(String json1, String json2) {
        if (StringUtils.isBlank(json1)) {
            return json2;
        }
        if (StringUtils.isBlank(json2)) {
            return json1;
        }
        try {
            Map<String, Object> map1 = toMap(json1);
            Map<String, Object> map2 = toMap(json2);
            map1.putAll(map2);
            return toJson(map1);
        } catch (Exception e) {
            throw new RuntimeException("JSON合并失败", e);
        }
    }

    /**
     * 合并两个Map
     */
    public static Map<String, Object> mergeMap(Map<String, Object> map1, Map<String, Object> map2) {
        if (map1 == null) {
            return map2;
        }
        if (map2 == null) {
            return map1;
        }
        Map<String, Object> result = new java.util.HashMap<>(map1);
        result.putAll(map2);
        return result;
    }

    // ==================== JSON格式化 ====================

    /**
     * 格式化JSON字符串
     */
    public static String formatJson(String json) {
        if (StringUtils.isBlank(json)) {
            return json;
        }
        try {
            Object obj = OBJECT_MAPPER.readValue(json, Object.class);
            return toPrettyJson(obj);
        } catch (JsonProcessingException e) {
            return json;
        }
    }

    /**
     * 压缩JSON字符串
     */
    public static String compactJson(String json) {
        if (StringUtils.isBlank(json)) {
            return json;
        }
        try {
            Object obj = OBJECT_MAPPER.readValue(json, Object.class);
            return toJson(obj);
        } catch (JsonProcessingException e) {
            return json;
        }
    }

    // ==================== 特殊类型处理 ====================

    /**
     * 处理日期格式
     */
    public static String toJsonWithDateFormat(Object obj, String dateFormat) {
        if (obj == null) {
            return null;
        }
        try {
            ObjectMapper mapper = OBJECT_MAPPER.copy();
            mapper.setDateFormat(new java.text.SimpleDateFormat(dateFormat));
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("对象转JSON失败", e);
        }
    }

    /**
     * 处理时区
     */
    public static String toJsonWithTimeZone(Object obj, String timeZone) {
        if (obj == null) {
            return null;
        }
        try {
            ObjectMapper mapper = OBJECT_MAPPER.copy();
            mapper.setTimeZone(java.util.TimeZone.getTimeZone(timeZone));
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("对象转JSON失败", e);
        }
    }

    // ==================== 异常处理 ====================

    /**
     * 安全转换JSON字符串为对象
     */
    public static <T> T safeFromJson(String json, Class<T> clazz) {
        try {
            return fromJson(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 安全转换对象为JSON字符串
     */
    public static String safeToJson(Object obj) {
        try {
            return toJson(obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 安全转换JSON字符串为Map
     */
    public static Map<String, Object> safeToMap(String json) {
        try {
            return toMap(json);
        } catch (Exception e) {
            return null;
        }
    }

    // ==================== 工具方法 ====================

    /**
     * 获取ObjectMapper实例
     */
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    /**
     * 创建新的ObjectMapper实例
     */
    public static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    /**
     * 深度复制对象
     */
    public static <T> T deepCopy(T obj, Class<T> clazz) {
        if (obj == null) {
            return null;
        }
        try {
            String json = toJson(obj);
            return fromJson(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("深度复制失败", e);
        }
    }

    /**
     * 深度复制对象（使用TypeReference）
     */
    public static <T> T deepCopy(T obj, TypeReference<T> typeReference) {
        if (obj == null) {
            return null;
        }
        try {
            String json = toJson(obj);
            return fromJson(json, typeReference);
        } catch (Exception e) {
            throw new RuntimeException("深度复制失败", e);
        }
    }
}
