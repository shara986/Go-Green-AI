package com.gogreen.ai.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "GoGreen AI REST API",
                description = "GoGreen AI Backend APIs",
                version = "1.0",
                contact = @Contact(
                        name = "GoGreen Support",
                        email = "support@gogreenai.com"
                )
        ),
        security = {
                @SecurityRequirement(name = "bearerAuth")
        },
        servers = {
                @Server(url = "http://localhost:8080", description = "Local development server")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT authentication",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER
)
public class SwaggerConfig {
}
