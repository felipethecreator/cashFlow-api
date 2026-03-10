package com.cashflow.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "CashFlow API",
                version = "1.0.0",
                description = "Sistema de Controle Financeiro Pessoal com notificações inteligentes",
                contact = @Contact(
                        name = "Felipe Rodrigues Queiroz",
                        email = "felipinhodev@gmail.com",
                        url = "https://github.com/felipethecreator/cashFlow-api"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Portfólio do desenvolvedor",
                url = "https://felipethecreator.com"
        )
)
@SecurityScheme(
        name = "bearer-jwt",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}