package com.social.gatherly.configuration;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT";

        SecurityRequirement securityRequirement =  new SecurityRequirement().addList(jwt);

        Components components = new Components()
                .addSecuritySchemes(
                        jwt,
                        new SecurityScheme()
                                .name(jwt)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                );
        return new OpenAPI()
                .info(apiInfo())
                .components(components)
                .addSecurityItem(securityRequirement);

    }
    private Info apiInfo() {
        return new Info()
                .title("Gatherly REST API")
                .description("Backend API for Gatherly Social Event Planner")
                .version("1.0");

    }
}
