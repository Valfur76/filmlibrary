package com.bagrov.springpmhw.videorent.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI videoRentProject() {
        return new OpenAPI().info(new Info()
                .title("Видеопрокат")
                .description("Сервис позволяющий взять фильм")
                .version("0.1")
                .license(new License().name("Apache 2.0").url("currently.no.site"))
                .contact(new Contact().name("Kirill Bagrov").email("ki@ri.ll"))
        );
    }
}
