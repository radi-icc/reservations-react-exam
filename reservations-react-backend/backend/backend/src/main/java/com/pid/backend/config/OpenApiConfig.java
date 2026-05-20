package com.pid.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI configuration for backend API documentation.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI reservationsProjectOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Reservations Project API")
                        .description("Backend API for theatre show catalogue, reservations, reviews, affiliates and admin features.")
                        .version("1.0.0"));
    }
}