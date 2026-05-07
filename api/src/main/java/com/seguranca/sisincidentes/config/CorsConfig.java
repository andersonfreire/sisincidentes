package com.seguranca.sisincidentes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuração de CORS (Cross-Origin Resource Sharing).
 *
 * <p>Permite que o front-end React (rodando em {@code http://localhost:3000})
 * consuma os endpoints do back-end Spring Boot sem bloqueios de política
 * de mesma origem do browser.</p>
 */
@Configuration
public class CorsConfig {

    /**
     * Configura as regras de CORS globais para toda a aplicação.
     *
     * <p>Define origens, métodos e headers permitidos, além de configurar
     * o suporte a credenciais para autenticação JWT.</p>
     *
     * @return Uma instância de {@link CorsConfigurationSource} configurada.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Origens permitidas
        config.setAllowedOrigins(List.of(
            "http://localhost:3000",   // React front-end (desenvolvimento)
            "http://localhost:8080"    // Spring Boot (testes locais via Swagger)
        ));

        // Métodos HTTP permitidos
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Headers permitidos (Explicitados para evitar avisos com allowCredentials)
        config.setAllowedHeaders(List.of(
            "Authorization",
            "Cache-Control",
            "Content-Type",
            "Accept",
            "Origin",
            "X-Requested-With",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));

        // Expõe headers úteis para o front-end
        config.setExposedHeaders(List.of("Location", "Content-Type"));

        // Permite cookies/credenciais (necessário para JWT no futuro)
        config.setAllowCredentials(true);

        // Tempo de cache do pre-flight (1 hora = 3600 segundos)
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
