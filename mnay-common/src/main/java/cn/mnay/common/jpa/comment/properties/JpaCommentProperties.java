package cn.mnay.common.jpa.comment.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(
        prefix = "jpa.comment"
)
public class JpaCommentProperties {

    private boolean enable;

}
