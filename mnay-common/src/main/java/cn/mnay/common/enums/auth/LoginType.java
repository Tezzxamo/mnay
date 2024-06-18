package cn.mnay.common.enums.auth;

import cn.mnay.common.enums.base.BaseJsonEnum;

public enum LoginType implements BaseJsonEnum<LoginType> {

    PASSWORD_PHONE(1, "密码-手机号"),
    PASSWORD_EMAIL(2, "密码-邮箱"),
    CAPTCHA_PHONE(3, "验证码-手机号"),
    CAPTCHA_EMAIL(4, "验证码-邮箱");

    private final int code;
    private final String description;

    LoginType(final int code, final String description) {
        this.description = description;
        this.code = code;
    }

    @Override
    public String description() {
        return this.description;
    }

    @Override
    public int getCode() {
        return this.code;
    }

}
