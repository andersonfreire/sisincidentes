package com.seguranca.sisincidentes.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa uma Categoria de incidentes ou vulnerabilidades.
 * 
 * <p><b>RF05 — Gerenciamento de Categorias</b></p>
 */
@Entity
@Table(name = "TB_CATEGORIA")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String nome;

    @Column(length = 255)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoCategoria tipo;
}
