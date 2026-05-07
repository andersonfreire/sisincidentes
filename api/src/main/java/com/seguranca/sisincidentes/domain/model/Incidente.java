package com.seguranca.sisincidentes.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidade JPA que representa um Incidente de segurança registrado.
 *
 * <p><b>RF06 — Gerenciamento de Vulnerabilidades e Incidentes</b></p>
 */
@Entity
@Table(name = "TB_INCIDENTE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Incidente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private String status = "ABERTO";

    @Column(name = "data_registro", nullable = false, updatable = false)
    private LocalDateTime dataRegistro;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    /** Relacionamento M:N com Vulnerabilidade (Exigência do Professor) */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "TB_INCIDENTE_VULNERABILIDADE",
        joinColumns = @JoinColumn(name = "incidente_id", foreignKey = @ForeignKey(name = "FK_INCIDENTE_VULN_INC")),
        inverseJoinColumns = @JoinColumn(name = "vulnerabilidade_id", foreignKey = @ForeignKey(name = "FK_INCIDENTE_VULN_VULN"))
    )
    private List<Vulnerabilidade> vulnerabilidades;

    @PrePersist
    protected void onCreate() {
        this.dataRegistro = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }
}