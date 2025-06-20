package com.ecommerce.orderservice.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(
        @Value("${springdoc.version:1.0.0}") String appVersion,
        @Value("${spring.application.name:Order Management Service}") String appTitle) {
        return new OpenAPI()
            .info(new Info().title(appTitle)
                .version(appVersion)
                .description("API for managing customer orders.")
                .termsOfService("http://swagger.io/terms/")
                .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
