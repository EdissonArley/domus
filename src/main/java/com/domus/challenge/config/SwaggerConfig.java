package com.domus.challenge.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${swagger.api.base-url}")
    private String apiBaseUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Movies & Directors API")
                        .version("1.0")
                        .description("API for managing movies and directors"))
                .servers(List.of(new Server().url(apiBaseUrl)));
    }
}
