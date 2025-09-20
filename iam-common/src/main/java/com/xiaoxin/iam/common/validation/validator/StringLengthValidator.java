package com.xiaoxin.iam.common.validation.validator;

import com.xiaoxin.iam.common.validation.annotation.StringLength;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 字符串长度验证器
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public class StringLengthValidator implements ConstraintValidator<StringLength, String> {

    private int min;
    private int max;
    private boolean nullable;
    private StringLength.LengthType lengthType;
    private Charset charset;
    private boolean trim;
    private boolean allowEmpty;

    @Override
    public void initialize(StringLength stringLength) {
        this.min = stringLength.min();
        this.max = stringLength.max();
        this.nullable = stringLength.nullable();
        this.lengthType = stringLength.lengthType();
        this.trim = stringLength.trim();
        this.allowEmpty = stringLength.allowEmpty();

        // 解析字符编码
        try {
            this.charset = Charset.forName(stringLength.charset());
        } catch (Exception e) {
            this.charset = StandardCharsets.UTF_8;
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 如果值为null且允许为空，则验证通过
        if (value == null) {
            return nullable;
        }

        // 处理空字符串
        if (value.isEmpty()) {
            if (!nullable && !allowEmpty) {
                return false;
            }
            // 空字符串长度为0，检查是否满足最小长度要求
            return min <= 0;
        }

        // 处理字符串
        String processedValue = trim ? value.trim() : value;

        // 如果trim后为空字符串，按空字符串处理
        if (processedValue.isEmpty()) {
            if (!nullable && !allowEmpty) {
                return false;
            }
            return min <= 0;
        }

        // 计算长度
        int length = calculateLength(processedValue);

        // 验证长度范围
        return length >= min && length <= max;
    }

    /**
     * 计算字符串长度
     *
     * @param value 字符串值
     * @return 长度
     */
    private int calculateLength(String value) {
        switch (lengthType) {
            case CHARACTER:
                return value.length();
            case BYTE:
                return value.getBytes(charset).length;
            default:
                return value.length();
        }
    }
}
