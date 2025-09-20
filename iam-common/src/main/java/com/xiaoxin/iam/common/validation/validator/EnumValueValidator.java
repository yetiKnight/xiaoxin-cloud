package com.xiaoxin.iam.common.validation.validator;

import com.xiaoxin.iam.common.validation.annotation.EnumValue;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 枚举值验证器
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public class EnumValueValidator implements ConstraintValidator<EnumValue, String> {

    private Class<? extends Enum<?>> enumClass;
    private boolean ignoreCase;
    private boolean nullable;
    private Set<String> allowedValues;
    private Set<String> forbiddenValues;
    private String method;
    private Set<String> enumValues;

    @Override
    public void initialize(EnumValue enumValue) {
        this.enumClass = enumValue.enumClass();
        this.ignoreCase = enumValue.ignoreCase();
        this.nullable = enumValue.nullable();
        this.allowedValues = new HashSet<>(Arrays.asList(enumValue.allowedValues()));
        this.forbiddenValues = new HashSet<>(Arrays.asList(enumValue.forbiddenValues()));
        this.method = enumValue.method();
        this.enumValues = extractEnumValues();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 如果值为空且允许为空，则验证通过
        if (value == null || value.trim().isEmpty()) {
            return nullable;
        }

        // 获取比较值
        String compareValue = ignoreCase ? value.toLowerCase() : value;

        // 检查是否在禁止列表中
        if (!forbiddenValues.isEmpty()) {
            Set<String> forbiddenSet = ignoreCase ? 
                createLowerCaseSet(forbiddenValues) : forbiddenValues;
            if (forbiddenSet.contains(compareValue)) {
                return false;
            }
        }

        // 如果指定了允许列表，只验证允许列表
        if (!allowedValues.isEmpty()) {
            Set<String> allowedSet = ignoreCase ? 
                createLowerCaseSet(allowedValues) : allowedValues;
            return allowedSet.contains(compareValue);
        }

        // 验证是否为有效枚举值
        return enumValues.contains(compareValue);
    }

    /**
     * 提取枚举值
     *
     * @return 枚举值集合
     */
    private Set<String> extractEnumValues() {
        Set<String> values = new HashSet<>();
        
        try {
            Enum<?>[] enumConstants = enumClass.getEnumConstants();
            if (enumConstants == null) {
                return values;
            }

            Method valueMethod = enumClass.getMethod(method);
            
            for (Enum<?> enumConstant : enumConstants) {
                Object methodResult = valueMethod.invoke(enumConstant);
                String enumValue = methodResult != null ? methodResult.toString() : null;
                
                if (enumValue != null) {
                    values.add(ignoreCase ? enumValue.toLowerCase() : enumValue);
                }
            }
        } catch (Exception e) {
            // 如果反射失败，回退到使用name()方法
            Enum<?>[] enumConstants = enumClass.getEnumConstants();
            if (enumConstants != null) {
                for (Enum<?> enumConstant : enumConstants) {
                    String enumValue = enumConstant.name();
                    values.add(ignoreCase ? enumValue.toLowerCase() : enumValue);
                }
            }
        }
        
        return values;
    }

    /**
     * 创建小写字符串集合
     *
     * @param originalSet 原始集合
     * @return 小写字符串集合
     */
    private Set<String> createLowerCaseSet(Set<String> originalSet) {
        Set<String> lowerCaseSet = new HashSet<>();
        for (String value : originalSet) {
            lowerCaseSet.add(value.toLowerCase());
        }
        return lowerCaseSet;
    }
}
