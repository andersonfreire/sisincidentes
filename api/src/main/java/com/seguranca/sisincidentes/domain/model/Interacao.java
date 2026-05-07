package com.seguranca.sisincidentes.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade JPA que representa uma interação ou atualização no histórico de um incidente.
 *
 * <p><b>RF10 — Histórico de Interações e Acompanhamento</b></p>
 */
@Entity
@Table(name = "TB_INTERACAO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Interacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "texto", nullable = false, columnDefinition = "TEXT")
    private String texto;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    /**
     * Relacionamento N:1 com Incidente.
     * Múltiplas interações pertencem a um único incidente.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incidente_id", nullable = false,
                foreignKey = @ForeignKey(name = "FK_INTERACAO_INCIDENTE"))
    private Incidente incidente;

    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
    }
}