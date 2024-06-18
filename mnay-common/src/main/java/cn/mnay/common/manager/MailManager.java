package cn.mnay.common.manager;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.mnay.common.constant.RedisConstants;
import cn.mnay.common.enums.error.CodeEnum;
import cn.mnay.common.exception.BusinessException;
import cn.mnay.common.model.dto.auth.SendMailDTO;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class MailManager {

    private final JavaMailSender javaMailSender;
    private final RedisTemplate<String, Object> redisTemplate;
    // 发件人邮箱
    @Value("${spring.mail.username}")
    private String from;

    /**
     * 创建用户后，将账户信息发送给指定邮箱
     */
    public void sendPasswordMail(SendMailDTO sendMailDTO) {
        // "Mnay系统提示：\n" +
        //  xxx用户 + "：\n" +
        // "您的账户已成功创建。\n" +
        // "登录邮箱：" + memberDTO.getMemberEmail() + "\n" +
        // "初始密码：" + memberDTO.getMemberPassword()
        new MailMessageInfo.MailMessageInfoBuilder()
                .from(from)
                .to(sendMailDTO.getToEmail())
                .subject(sendMailDTO.getSubject()) // "账号信息"
                .text(sendMailDTO.getText())
                .build()
                .send(javaMailSender);
    }

    /**
     * 发送重置密码的验证码给指定邮箱
     */
    public void sendResetPwdMail(SendMailDTO sendMailDTO) {
        // 生成验证码并存入redis中，验证码有效时间设为5分钟
        String captcha = RandomUtil.randomString(6);
        String key = RedisConstants.RESET_PASSWORD + sendMailDTO.getToEmail();
        Assert.isTrue(StrUtil.isBlank(Optional.ofNullable(redisTemplate.opsForValue().get(key)).map(Object::toString).orElse(null)), () -> new BusinessException(CodeEnum.VERIFICATION_CODE_NOT_EXPIRED));

        redisTemplate.opsForValue().set(key, captcha, 5, TimeUnit.MINUTES);

        // 发送验证码
        new MailMessageInfo.MailMessageInfoBuilder()
                .from(from)
                .to(sendMailDTO.getToEmail())
                .subject(sendMailDTO.getSubject())
                .text(sendMailDTO.getText() + captcha)
                .build()
                .send(javaMailSender);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MailMessageInfo {
        private String from;
        private String to;
        private String subject;
        private String text;

        public void send(JavaMailSender javaMailSender) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(this.getFrom());
            message.setTo(this.getTo()); // 收件人邮箱地址
            message.setSubject(this.getSubject());
            message.setText(this.getText());
            javaMailSender.send(message);
        }
    }

}
