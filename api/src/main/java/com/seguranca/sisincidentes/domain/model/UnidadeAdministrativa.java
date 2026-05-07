package com.seguranca.sisincidentes.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade JPA que representa uma Unidade Administrativa.
 *
 * <p>RF01 — Gerenciamento de Unidades Administrativas</p>
 *
 * <p>Mapeada para a tabela {@code TB_UNIDADE_ADMINISTRATIVA} no banco H2.</p>
 */
@Entity
@Table(
    name = "TB_UNIDADE_ADMINISTRATIVA",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_UNIDADE_CODIGO", columnNames = "codigo")
    }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnidadeAdministrativa {

    /** Identificador único (PK, gerado automaticamente). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Código organizacional da unidade (único).
     * Exemplo: "1.25.01"
     */
    @Column(name = "codigo", nullable = false, length = 50)
    private String codigo;

    /**
     * Sigla da unidade administrativa.
     * Exemplo: "STI"
     */
    @Column(name = "sigla", nullable = false, length = 20)
    private String sigla;

    /**
     * Nome completo (título) da unidade.
     * Exemplo: "Superintendência de Tecnologia da Informação"
     */
    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    /**
     * Nome do gestor ou responsável pela unidade (opcional).
     */
    @Column(name = "responsavel", length = 150)
    private String responsavel;

    /**
     * Informações de contato: telefone, e-mail ou ramal (opcional).
     */
    @Column(name = "contato", length = 300)
    private String contato;

    /** Data/hora de criação do registro (preenchida automaticamente). */
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    /** Data/hora da última atualização do registro (preenchida automaticamente). */
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    /**
     * Callback JPA executado antes do primeiro {@code persist}.
     * Define a data de criação automaticamente.
     */
    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    /**
     * Callback JPA executado antes de cada {@code merge} (atualização).
     * Atualiza a data de modificação automaticamente.
     */
    @PreUpdate
    protected void onUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }
}
