package com.seguranca.sisincidentes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * Configuração de CORS (Cross-Origin Resource Sharing).
 *
 * <p>Permite que o front-end React (rodando em {@code http://localhost:3000})
 * consuma os endpoints do back-end Spring Boot (rodando em {@code http://localhost:8080})
 * sem bloqueios de política de mesma origem do browser.</p>
 */
@Configuration
public class CorsConfig {

    /**
     * Configura as regras de CORS globais para toda a aplicação.
     *
     * <ul>
     *   <li>Origens permitidas: localhost:3000 (React dev) e localhost:8080 (API dev)</li>
     *   <li>Métodos permitidos: GET, POST, PUT, DELETE, OPTIONS</li>
     *   <li>Headers permitidos: qualquer (incluindo Authorization para futura autenticação JWT)</li>
     *   <li>Credenciais: permitidas (necessário para cookies/JWT em futuras versões)</li>
     *   <li>Max Age: 1 hora (evita pre-flight repetitivos)</li>
     * </ul>
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Origens permitidas
        config.setAllowedOrigins(List.of(
            "http://localhost:3000",   // React front-end (desenvolvimento)
            "http://localhost:8080"    // Spring Boot (testes locais via Swagger)
        ));

        // Métodos HTTP permitidos
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Headers permitidos (qualquer header, incluindo Authorization para JWT)
        config.setAllowedHeaders(List.of("*"));

        // Expõe o header Location (útil para POST com 201 Created)
        config.setExposedHeaders(List.of("Location", "Content-Type"));

        // Permite cookies/credenciais (necessário para JWT no futuro)
        config.setAllowCredentials(true);

        // Tempo de cache do pre-flight (1 hora = 3600 segundos)
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
