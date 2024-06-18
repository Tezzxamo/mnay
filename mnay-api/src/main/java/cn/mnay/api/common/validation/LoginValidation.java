package cn.mnay.api.common.validation;

import cn.hutool.core.lang.Assert;
import cn.mnay.api.model.request.auth.HttpLoginReq;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoginValidation implements DefaultGroupSequenceProvider<HttpLoginReq> {

    @Override
    public List<Class<?>> getValidationGroups(HttpLoginReq httpLoginReq) {
        List<Class<?>> defaultGroupSequence = new ArrayList<>();
        // 这一步不能省,否则Default分组都不会执行了，会抛错的
        defaultGroupSequence.add(HttpLoginReq.class);

        Optional.ofNullable(httpLoginReq)
                .ifPresent(req -> {
                    switch (req.getLoginType()) {
                        case PASSWORD_PHONE -> {
                            Assert.notBlank(req.getPhone(), "手机号不能为空");
                            Assert.notBlank(req.getPassword(), "密码不能为空");
                        }
                        case PASSWORD_EMAIL -> {
                            Assert.notBlank(req.getEmail(), "邮箱不能为空");
                            Assert.notBlank(req.getPassword(), "密码不能为空");
                        }
                        case CAPTCHA_PHONE -> {
                            Assert.notBlank(req.getPhone(), "手机号不能为空");
                            Assert.notBlank(req.getCaptcha(), "验证码不能为空");
                        }
                        case CAPTCHA_EMAIL -> {
                            Assert.notBlank(req.getEmail(), "邮箱不能为空");
                            Assert.notBlank(req.getCaptcha(), "验证码不能为空");
                        }
                    }
                });
        return defaultGroupSequence;
    }

}
