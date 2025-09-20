package com.xiaoxin.iam.common.validation.validator;

import com.xiaoxin.iam.common.validation.ValidationUtils;
import com.xiaoxin.iam.common.validation.annotation.Username;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 用户名验证器
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public class UsernameValidator implements ConstraintValidator<Username, String> {

    private Username.UsernameType type;
    private int min;
    private int max;
    private boolean nullable;
    private boolean allowUnderscore;
    private boolean allowDigit;
    private boolean mustStartWithLetter;
    private Set<String> blacklist;

    @Override
    public void initialize(Username username) {
        this.type = username.type();
        this.min = username.min();
        this.max = username.max();
        this.nullable = username.nullable();
        this.allowUnderscore = username.allowUnderscore();
        this.allowDigit = username.allowDigit();
        this.mustStartWithLetter = username.mustStartWithLetter();
        this.blacklist = new HashSet<>(Arrays.asList(username.blacklist()));
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 如果值为空且允许为空，则验证通过
        if (value == null || value.trim().isEmpty()) {
            return nullable;
        }

        // 长度验证
        if (!ValidationUtils.isLengthValid(value, min, max)) {
            return false;
        }

        // 黑名单验证
        if (!blacklist.isEmpty() && blacklist.contains(value.toLowerCase())) {
            return false;
        }

        // 格式验证
        if (!validateFormat(value)) {
            return false;
        }

        // 必须以字母开头验证
        if (mustStartWithLetter && !Character.isLetter(value.charAt(0))) {
            return false;
        }

        return true;
    }

    /**
     * 验证用户名格式
     *
     * @param username 用户名
     * @return 是否有效
     */
    private boolean validateFormat(String username) {
        switch (type) {
            case ENGLISH:
                return validateEnglishUsername(username);
            case CHINESE:
                return validateChineseUsername(username);
            case CUSTOM:
                return true; // 自定义类型不进行格式验证
            default:
                return false;
        }
    }

    /**
     * 验证英文用户名
     *
     * @param username 用户名
     * @return 是否有效
     */
    private boolean validateEnglishUsername(String username) {
        // 构建正则表达式
        StringBuilder pattern = new StringBuilder("^[a-zA-Z");
        
        if (allowDigit) {
            pattern.append("0-9");
        }
        
        if (allowUnderscore) {
            pattern.append("_");
        }
        
        pattern.append("]+$");

        return Pattern.matches(pattern.toString(), username);
    }

    /**
     * 验证中文用户名
     *
     * @param username 用户名
     * @return 是否有效
     */
    private boolean validateChineseUsername(String username) {
        // 构建正则表达式
        StringBuilder pattern = new StringBuilder("^[\\u4e00-\\u9fa5a-zA-Z");
        
        if (allowDigit) {
            pattern.append("0-9");
        }
        
        if (allowUnderscore) {
            pattern.append("_");
        }
        
        pattern.append("]+$");

        return Pattern.matches(pattern.toString(), username);
    }
}
