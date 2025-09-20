package com.xiaoxin.iam.common.validation.validator;

import com.xiaoxin.iam.common.validation.ValidationUtils;
import com.xiaoxin.iam.common.validation.annotation.IdCard;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 身份证号验证器
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public class IdCardValidator implements ConstraintValidator<IdCard, String> {

    private IdCard.IdCardType idCardType;
    private boolean nullable;
    private boolean checkCode;

    @Override
    public void initialize(IdCard idCard) {
        this.idCardType = idCard.type();
        this.nullable = idCard.nullable();
        this.checkCode = idCard.checkCode();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 如果值为空且允许为空，则验证通过
        if (value == null || value.trim().isEmpty()) {
            return nullable;
        }

        // 根据身份证类型进行验证
        switch (idCardType) {
            case ID_15:
                return ValidationUtils.isValidIdCard15(value);
            case ID_18:
                return validateId18(value);
            case BOTH:
                return ValidationUtils.isValidIdCard15(value) || validateId18(value);
            default:
                return false;
        }
    }

    /**
     * 验证18位身份证
     *
     * @param idCard 身份证号
     * @return 是否有效
     */
    private boolean validateId18(String idCard) {
        if (!ValidationUtils.isValidIdCard(idCard)) {
            return false;
        }

        // 如果不需要验证校验码，直接返回格式验证结果
        if (!checkCode) {
            return true;
        }

        // 验证18位身份证校验码
        return validateCheckCode(idCard);
    }

    /**
     * 验证18位身份证校验码
     *
     * @param idCard 身份证号
     * @return 校验码是否正确
     */
    private boolean validateCheckCode(String idCard) {
        if (idCard == null || idCard.length() != 18) {
            return false;
        }

        try {
            // 权重因子
            int[] weights = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
            // 校验码对应值
            char[] checkCodes = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

            int sum = 0;
            for (int i = 0; i < 17; i++) {
                char c = idCard.charAt(i);
                if (!Character.isDigit(c)) {
                    return false;
                }
                sum += (c - '0') * weights[i];
            }

            int mod = sum % 11;
            char expectedCheckCode = checkCodes[mod];
            char actualCheckCode = idCard.charAt(17);

            return expectedCheckCode == actualCheckCode || 
                   (expectedCheckCode == 'X' && (actualCheckCode == 'x' || actualCheckCode == 'X'));
        } catch (Exception e) {
            return false;
        }
    }
}
