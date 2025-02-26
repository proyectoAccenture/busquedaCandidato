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
                        .title("API de Busqueda de Candidatos")
                        .version("1.0")
                        .description("Documentación de la API de usuarios usando OpenAPI y Swagger"));
    }
}
