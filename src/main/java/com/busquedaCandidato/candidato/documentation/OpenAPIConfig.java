package com.busquedaCandidato.candidato.documentation;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Candidate Search API")
                        .version("1.0")
                        .description("Candidate Search API Documentation Using OpenAPI and Swagger"));
    }
}
