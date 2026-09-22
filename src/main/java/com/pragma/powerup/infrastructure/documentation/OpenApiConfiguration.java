package com.pragma.powerup.infrastructure.documentation;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenApi(@Value("${appdescription}") String appDescription,
                                 @Value("${appversion}") String appVersion) {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Microservicio de Usuarios - PowerUp Plazoleta")
                        .version(appVersion)
                        .description("API REST para la administración y autenticación de usuarios de la plataforma de Plazoleta de Comidas. Gestiona los roles de Administrador, Propietario, Empleado y Cliente bajo Arquitectura Hexagonal.")
                        .contact(new Contact()
                                .name("Valentina Pinto")
                                .email("valentina.pinto@pragma.com.co"))
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                );
    }
}