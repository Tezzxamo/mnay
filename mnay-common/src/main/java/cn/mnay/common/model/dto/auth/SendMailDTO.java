package cn.mnay.common.model.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "发送邮件DTO")
public class SendMailDTO {

    /**
     * 发件人
     */
    private String fromEmail;
    /**
     * 收件人
     */
    private String toEmail;
    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 邮件内容
     */
    private String text;


}
