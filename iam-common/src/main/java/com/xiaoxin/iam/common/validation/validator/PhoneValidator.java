package com.xiaoxin.iam.common.validation.validator;

import com.xiaoxin.iam.common.validation.ValidationUtils;
import com.xiaoxin.iam.common.validation.annotation.Phone;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 手机号验证器
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {

    private Phone.PhoneType phoneType;
    private boolean nullable;

    @Override
    public void initialize(Phone phone) {
        this.phoneType = phone.type();
        this.nullable = phone.nullable();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 如果值为空且允许为空，则验证通过
        if (value == null || value.trim().isEmpty()) {
            return nullable;
        }

        // 根据手机号类型进行验证
        switch (phoneType) {
            case CN:
                return ValidationUtils.isValidPhone(value);
            case INTERNATIONAL:
                return ValidationUtils.isValidInternationalPhone(value);
            case ANY:
                return ValidationUtils.isValidPhone(value) || 
                       ValidationUtils.isValidInternationalPhone(value);
            default:
                return false;
        }
    }
}
