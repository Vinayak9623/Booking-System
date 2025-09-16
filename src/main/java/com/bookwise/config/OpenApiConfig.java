package com.bookwise.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(    name = "bearerAuth", // This name must match the one used in @SecurityRequirement
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer")
public class OpenApiConfig {

    @Bean
    public OpenAPI customeOpenApi(){
        return new OpenAPI().info(new Info().title("My Api").version("1.0").description("Api Documentation with jwt support"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
