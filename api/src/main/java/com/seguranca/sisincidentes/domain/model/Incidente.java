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

    @Column(name = "numero_chamado", length = 50)
    private String numeroChamado;

    @Column(name = "tarefa_relacionada", length = 100)
    private String tarefaRelacionada;

    @Column(name = "tipo", length = 20)
    private String tipo;

    @Column(name = "prioridade", length = 20)
    private String prioridade;

    @Column(name = "ip_origem", length = 50)
    private String ipOrigem;

    @Column(name = "ip_destino", length = 50)
    private String ipDestino;

    @Column(name = "host", length = 100)
    private String host;

    @Column(name = "tempo_estimado", length = 50)
    private String tempoEstimado;

    @Column(name = "cc", length = 255)
    private String cc;

    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", foreignKey = @ForeignKey(name = "FK_INCIDENTE_CATEGORIA"))
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_id", foreignKey = @ForeignKey(name = "FK_INCIDENTE_UNIDADE"))
    private UnidadeAdministrativa unidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", foreignKey = @ForeignKey(name = "FK_INCIDENTE_AUTOR"))
    private Usuario autor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atribuido_id", foreignKey = @ForeignKey(name = "FK_INCIDENTE_ATRIBUIDO"))
    private Usuario atribuido;

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