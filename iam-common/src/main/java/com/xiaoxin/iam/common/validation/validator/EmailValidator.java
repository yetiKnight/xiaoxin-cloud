package com.xiaoxin.iam.common.validation.validator;

import com.xiaoxin.iam.common.validation.ValidationUtils;
import com.xiaoxin.iam.common.validation.annotation.Email;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 邮箱验证器
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public class EmailValidator implements ConstraintValidator<Email, String> {

    private boolean strict;
    private boolean nullable;
    private Set<String> allowedDomains;
    private Set<String> blockedDomains;

    @Override
    public void initialize(Email email) {
        this.strict = email.strict();
        this.nullable = email.nullable();
        this.allowedDomains = new HashSet<>(Arrays.asList(email.allowedDomains()));
        this.blockedDomains = new HashSet<>(Arrays.asList(email.blockedDomains()));
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 如果值为空且允许为空，则验证通过
        if (value == null || value.trim().isEmpty()) {
            return nullable;
        }

        // 基本格式验证
        boolean isValidFormat = strict ? 
            ValidationUtils.isValidEmailStrict(value) : 
            ValidationUtils.isValidEmail(value);

        if (!isValidFormat) {
            return false;
        }

        // 域名限制验证
        String domain = extractDomain(value);
        if (domain == null) {
            return false;
        }

        // 检查是否在禁止域名列表中
        if (!blockedDomains.isEmpty() && blockedDomains.contains(domain.toLowerCase())) {
            return false;
        }

        // 检查是否在允许域名列表中（如果设置了允许列表）
        if (!allowedDomains.isEmpty() && !allowedDomains.contains(domain.toLowerCase())) {
            return false;
        }

        return true;
    }

    /**
     * 提取邮箱域名
     *
     * @param email 邮箱地址
     * @return 域名
     */
    private String extractDomain(String email) {
        if (email == null || !email.contains("@")) {
            return null;
        }
        String[] parts = email.split("@");
        if (parts.length != 2) {
            return null;
        }
        return parts[1];
    }
}
