package com.seguranca.sisincidentes.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entidade JPA que representa uma Vulnerabilidade de segurança.
 *
 * <p><b>RF06 — Gerenciamento de Vulnerabilidades e Incidentes</b></p>
 */
@Entity
@Table(name = "TB_VULNERABILIDADE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vulnerabilidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @Column(name = "severidade", nullable = false, length = 20)
    private String severidade; // Ex: BAIXA, MEDIA, ALTA, CRITICA

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
    }
}