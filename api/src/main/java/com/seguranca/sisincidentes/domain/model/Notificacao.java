package com.seguranca.sisincidentes.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Representa uma notificação enviada a um usuário do sistema.
 * Completa o requisito de 8 entidades mapeadas.
 */
@Entity
@Table(name = "TB_NOTIFICACAO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensagem;

    @Builder.Default
    @Column(name = "lida", nullable = false)
    private boolean lida = false;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, foreignKey = @ForeignKey(name = "FK_NOTIFICACAO_USUARIO"))
    private Usuario destinatario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incidente_id", foreignKey = @ForeignKey(name = "FK_NOTIFICACAO_INCIDENTE"))
    private Incidente incidente;

    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
    }
}
