package cn.mnay.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "Mnay系统",
                version = "1.0.0",
                description = "Mnay"
        ),
        security = @SecurityRequirement(name = "JWT")
)
@SecurityScheme(type = SecuritySchemeType.HTTP, name = "JWT", scheme = "bearer", in = SecuritySchemeIn.HEADER)
public class Swagger3Config {
}
