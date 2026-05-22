package com.example.identity.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI identityServiceOpenApi() {
        return new OpenAPI()
            .info(new Info()
                .title("Identity Service API")
                .description("REST API for user registration and password-based authentication.")
                .version("0.0.1")
                .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")))
            .servers(List.of(new Server().url("/").description("Relative to the deployment host")));
    }
}
