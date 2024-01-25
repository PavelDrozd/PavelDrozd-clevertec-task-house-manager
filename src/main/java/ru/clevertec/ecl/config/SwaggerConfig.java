package ru.clevertec.ecl.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(
        description = "OpenApi documentation",
        title = "OpenApi specification - House manager",
        termsOfService = "Term of service"
),
        servers = @Server(
                description = "Local ENV",
                url = "http://localhost:8080"
        ))
@Configuration
public class SwaggerConfig {
}
