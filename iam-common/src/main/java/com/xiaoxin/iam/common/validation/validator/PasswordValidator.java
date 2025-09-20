package com.xiaoxin.iam.common.validation.validator;

import com.xiaoxin.iam.common.validation.ValidationUtils;
import com.xiaoxin.iam.common.validation.annotation.Password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 密码强度验证器
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public class PasswordValidator implements ConstraintValidator<Password, String> {

    private Password.PasswordStrength strength;
    private int min;
    private int max;
    private boolean nullable;
    private boolean requireUppercase;
    private boolean requireLowercase;
    private boolean requireDigit;
    private boolean requireSpecialChar;
    private String allowedSpecialChars;
    private boolean forbidConsecutiveSame;
    private int maxConsecutiveSame;
    private boolean forbidCommonPasswords;

    // 常见弱密码列表
    private static final Set<String> COMMON_PASSWORDS = new HashSet<>(Arrays.asList(
        "123456", "password", "123456789", "12345678", "12345", "1234567", "1234567890",
        "qwerty", "abc123", "111111", "password123", "admin", "123123", "welcome",
        "login", "master", "hello", "guest", "qwerty123", "654321", "root", "test",
        "user", "letmein", "pass", "monkey", "dragon", "1234", "trustno1"
    ));

    @Override
    public void initialize(Password password) {
        this.strength = password.strength();
        this.min = password.min();
        this.max = password.max();
        this.nullable = password.nullable();
        this.requireUppercase = password.requireUppercase();
        this.requireLowercase = password.requireLowercase();
        this.requireDigit = password.requireDigit();
        this.requireSpecialChar = password.requireSpecialChar();
        this.allowedSpecialChars = password.allowedSpecialChars();
        this.forbidConsecutiveSame = password.forbidConsecutiveSame();
        this.maxConsecutiveSame = password.maxConsecutiveSame();
        this.forbidCommonPasswords = password.forbidCommonPasswords();

        // 根据强度等级自动设置要求
        applyStrengthRequirements();
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

        // 常见弱密码验证
        if (forbidCommonPasswords && COMMON_PASSWORDS.contains(value.toLowerCase())) {
            return false;
        }

        // 连续相同字符验证
        if (forbidConsecutiveSame && hasConsecutiveSameChars(value)) {
            return false;
        }

        // 字符类型要求验证
        if (!validateCharacterRequirements(value)) {
            return false;
        }

        return true;
    }

    /**
     * 根据强度等级应用要求
     */
    private void applyStrengthRequirements() {
        switch (strength) {
            case WEAK:
                // 弱密码只需要长度要求，其他都不强制
                break;
            case MEDIUM:
                // 中等密码需要字母和数字
                this.requireLowercase = true;
                this.requireDigit = true;
                break;
            case STRONG:
                // 强密码需要大小写字母、数字和特殊字符
                this.requireUppercase = true;
                this.requireLowercase = true;
                this.requireDigit = true;
                this.requireSpecialChar = true;
                this.forbidConsecutiveSame = true;
                break;
            case CUSTOM:
                // 自定义强度使用注解中指定的参数
                break;
            default:
                break;
        }
    }

    /**
     * 验证字符类型要求
     *
     * @param password 密码
     * @return 是否满足要求
     */
    private boolean validateCharacterRequirements(String password) {
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (allowedSpecialChars.indexOf(c) >= 0) {
                hasSpecialChar = true;
            }
        }

        // 检查各项要求
        if (requireUppercase && !hasUppercase) {
            return false;
        }
        if (requireLowercase && !hasLowercase) {
            return false;
        }
        if (requireDigit && !hasDigit) {
            return false;
        }
        if (requireSpecialChar && !hasSpecialChar) {
            return false;
        }

        return true;
    }

    /**
     * 检查是否有连续相同字符
     *
     * @param password 密码
     * @return 是否有连续相同字符
     */
    private boolean hasConsecutiveSameChars(String password) {
        if (password.length() < maxConsecutiveSame) {
            return false;
        }

        int count = 1;
        char prevChar = password.charAt(0);

        for (int i = 1; i < password.length(); i++) {
            char currentChar = password.charAt(i);
            if (currentChar == prevChar) {
                count++;
                if (count >= maxConsecutiveSame) {
                    return true;
                }
            } else {
                count = 1;
                prevChar = currentChar;
            }
        }

        return false;
    }
}
