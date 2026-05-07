package com.seguranca.sisincidentes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Configuração do SpringDoc OpenAPI (Swagger).
 * Define as informações da API e o esquema de segurança JWT Bearer.
 */
@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                final String securitySchemeName = "bearerAuth";

                return new OpenAPI()
                                .info(new Info()
                                                .title("API SisIncidentes")
                                                .version("v1.0.0")
                                                .description("## Sistema de Gerenciamento de Incidentes de Segurança\n\n"
                                                                +
                                                                "API REST construída com **Spring Boot 3.4** + **Java 21** + **H2 Database**.\n\n"
                                                                +
                                                                "### Recursos implementados:\n" +
                                                                "- **RF01** — Gerenciamento de Unidades Administrativas ✅\n"
                                                                +
                                                                "- **RF02** — Gerenciamento de Usuários ✅\n" +
                                                                "- **RF03** — Autenticação e Autorização (JWT) ✅\n\n" +
                                                                "- **RF04** — Restrição de Alteração de Dados Sensíveis ✅\n\n"
                                                                +
                                                                "- **RF05** — Gerenciamento de Categorias ✅\n\n"
                                                                +
                                                                "### Recursos em desenvolvimento:\n" +
                                                                "- RF06 — Gerenciamento de Incidentes e Vulnerabilidades\n"
                                                                +
                                                                "- RF07 — Emissão de Relatórios\n" +
                                                                "- RF08 — Gerenciamento de Lições Aprendidas\n" +
                                                                "- RF09 — Consulta de Estatísticas Externas\n" +
                                                                "- RF10 — Controle de Perfis e Permissões")
                                                .contact(new Contact()
                                                                .name("Suporte TI")
                                                                .email("suporte@sisincidentes.gov.br"))
                                                .license(new License()
                                                                .name("Apache 2.0")
                                                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                                .components(new Components()
                                                .addSecuritySchemes(securitySchemeName,
                                                                new SecurityScheme()
                                                                                .name(securitySchemeName)
                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer")
                                                                                .bearerFormat("JWT")));
        }
}
