package com.seguranca.sisincidentes.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade JPA que representa um Usuário do sistema SisIncidentes.
 *
 * <p><b>RF02 — Gerenciamento de Usuários</b></p>
 *
 * <p>Mapeada para a tabela {@code TB_USUARIO} no banco H2.
 * A senha é armazenada como hash BCrypt — nunca em texto plano.</p>
 *
 * <p>Relacionamento {@code ManyToOne} com {@link UnidadeAdministrativa}:
 * cada usuário pertence a uma unidade administrativa.</p>
 */
@Entity
@Table(
    name = "TB_USUARIO",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_USUARIO_EMAIL", columnNames = "email")
    }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    /** Identificador único (PK, gerado automaticamente). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * E-mail institucional do usuário — único no sistema.
     * Será utilizado como login no mecanismo JWT (RF futuro).
     */
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    /**
     * Senha do usuário armazenada como hash BCrypt.
     * <strong>Nunca deve ser exposta em responses REST.</strong>
     */
    @Column(name = "senha", nullable = false, length = 255)
    private String senha;

    /** Nome completo do usuário. */
    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    /** Matrícula funcional (opcional). */
    @Column(name = "matricula", length = 30)
    private String matricula;

    /** Telefone de contato (opcional). */
    @Column(name = "telefone", length = 30)
    private String telefone;

    /**
     * Função/cargo do usuário na organização.
     * Persistida como String para legibilidade no banco.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "funcao", nullable = false, length = 30)
    private FuncaoUsuario funcao;

    /**
     * Perfil de acesso do usuário no sistema.
     * Determina as permissões de acesso aos módulos.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "perfil", nullable = false, length = 20)
    @Builder.Default
    private Perfil perfil = Perfil.OPERADOR;

    /** Observações adicionais sobre o usuário (opcional). */
    @Column(name = "observacoes", length = 500)
    private String observacoes;

    /**
     * Indica se o usuário está ativo no sistema.
     * Usuários inativos não conseguem autenticar.
     * Padrão: {@code true}.
     */
    @Column(name = "ativo", nullable = false)
    @Builder.Default
    private boolean ativo = true;

    /**
     * Unidade Administrativa à qual o usuário pertence.
     * Relacionamento obrigatório — cada usuário deve estar vinculado a uma unidade.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_id", nullable = false,
                foreignKey = @ForeignKey(name = "FK_USUARIO_UNIDADE"))
    private UnidadeAdministrativa unidade;

    /** Data/hora de criação do registro (preenchida automaticamente). */
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    /** Data/hora da última atualização (preenchida automaticamente). */
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    /**
     * Callback JPA executado antes do primeiro {@code persist}.
     * Define as datas de auditoria automaticamente.
     */
    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
        if (this.perfil == null) {
            this.perfil = Perfil.OPERADOR;
        }
        this.ativo = true;
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
