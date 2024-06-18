package cn.mnay.common.annotation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;


/**
 * 手机号码校验器
 */
@Slf4j
public class PhoneValidator implements ConstraintValidator<Phone, String> {

    /**
     * 手机号码正则表达式
     */
    public static final String PHONE_PATTERN = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0-8])|(18[0-9])|166|198|199|(147))\\d{8}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean result = false;
        try {
            result = value.matches(PHONE_PATTERN);
        } catch (Exception e) {
            log.error("手机号码格式不正确", e);
        }
        return result;
    }

}
