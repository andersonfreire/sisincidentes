package com.seguranca.sisincidentes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração do Spring Security para a fase atual do desenvolvimento.
 *
 * <p>Nesta etapa (RF01), a API opera sem autenticação JWT para que a integração
 * entre o front-end React e o back-end possa ser validada. A autenticação
 * será implementada em um RF específico.</p>
 *
 * <ul>
 *   <li>CSRF desabilitado (padrão para APIs REST stateless)</li>
 *   <li>Sessões stateless (sem HttpSession no servidor)</li>
 *   <li>Headers de frame habilitados para o H2 Console funcionar no browser</li>
 *   <li>Todos os endpoints públicos nesta fase</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /** Endpoints públicos (sem autenticação) nesta fase do desenvolvimento. */
    private static final String[] PUBLIC_ENDPOINTS = {
        "/api/**",
        "/h2-console/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/v3/api-docs/**",
        "/actuator/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desabilita CSRF (APIs REST stateless não usam cookies de sessão)
            .csrf(AbstractHttpConfigurer::disable)

            // Política de sessão: STATELESS (sem HttpSession)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Autorização de rotas
            .authorizeHttpRequests(auth ->
                auth.requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                    .anyRequest().authenticated()
            )

            // Permite frames do mesmo domínio (necessário para o H2 Console)
            .headers(headers ->
                headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
            );

        return http.build();
    }
}
