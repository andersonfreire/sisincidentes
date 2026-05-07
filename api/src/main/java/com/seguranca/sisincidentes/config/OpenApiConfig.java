package com.seguranca.sisincidentes.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração do OpenAPI 3 (Swagger UI).
 *
 * <p>Documenta todos os recursos REST da API SisIncidentes,
 * tornando-os acessíveis via interface gráfica do Swagger em
 * {@code http://localhost:8080/swagger-ui/index.html}.</p>
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI sisIncidentesOpenAPI() {
        return new OpenAPI()
            .info(buildApiInfo())
            .servers(buildServers());
    }

    private Info buildApiInfo() {
        return new Info()
            .title("SisIncidentes API")
            .version("1.0.0")
            .description(
                "## Sistema de Gerenciamento de Incidentes de Segurança\n\n" +
                "API REST construída com **Spring Boot 3.4** + **Java 21** + **H2 Database**.\n\n" +
                "### Recursos implementados:\n" +
                "- **RF01** — Gerenciamento de Unidades Administrativas ✅\n\n" +
                "### Recursos em desenvolvimento:\n" +
                "- RF02 — Autenticação e Autorização (JWT)\n" +
                "- RF03 — Gerenciamento de Usuários\n" +
                "- RF04 — Gerenciamento de Categorias\n" +
                "- RF05 — Gerenciamento de Incidentes\n" +
                "- RF06 — Relatórios e Estatísticas"
            )
            .contact(new Contact()
                .name("Equipe SisIncidentes")
                .email("contato@sisincidentes.gov.br")
            )
            .license(new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0")
            );
    }

    private List<Server> buildServers() {
        return List.of(
            new Server()
                .url("http://localhost:8080")
                .description("Servidor de Desenvolvimento Local"),
            new Server()
                .url("https://api.sisincidentes.gov.br")
                .description("Servidor de Produção")
        );
    }
}
