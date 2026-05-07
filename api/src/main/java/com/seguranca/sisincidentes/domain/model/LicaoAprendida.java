package com.seguranca.sisincidentes.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Registro de pós-incidente detalhando a resolução e conhecimentos obtidos.
 *
 * <p><b>RF08 — Gerenciamento de Lições Aprendidas</b></p>
 */
@Entity
@Table(name = "TB_LICAO_APRENDIDA")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LicaoAprendida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descricao_resolucao", nullable = false, columnDefinition = "TEXT")
    private String descricaoResolucao;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    /** Relacionamento 1:1 com Incidente (Exigência do Professor) */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incidente_id", nullable = false, unique = true,
                foreignKey = @ForeignKey(name = "FK_LICAO_INCIDENTE"))
    private Incidente incidente;

    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
    }
}