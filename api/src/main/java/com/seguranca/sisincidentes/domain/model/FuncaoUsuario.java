package com.seguranca.sisincidentes.domain.model;

/**
 * Enum que representa a função/cargo de um Usuário na organização.
 *
 * <p>Os valores são persistidos como {@code STRING} no banco de dados
 * via {@code @Enumerated(EnumType.STRING)}, tornando o schema legível
 * sem necessidade de tabela de apoio.</p>
 *
 * <p>Utilizado no {@link Usuario} e exposto nas APIs REST como string.</p>
 */
public enum FuncaoUsuario {

    /** Analista de Tecnologia da Informação. */
    ANALISTA_TI("Analista de TI"),

    /** Coordenador de Segurança da Informação. */
    COORDENADOR_SI("Coordenador de SI"),

    /** Gestor de Tecnologia da Informação. */
    GESTOR_TI("Gestor de TI"),

    /** Superintendente de Tecnologia da Informação. */
    SUPERINTENDENTE_TI("Superintendente de TI"),

    /** Técnico de Tecnologia da Informação. */
    TECNICO_TI("Técnico de TI");

    /** Rótulo legível em português para exibição na interface. */
    private final String descricao;

    FuncaoUsuario(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Retorna a descrição amigável da função.
     *
     * @return descrição em português
     */
    public String getDescricao() {
        return descricao;
    }
}
