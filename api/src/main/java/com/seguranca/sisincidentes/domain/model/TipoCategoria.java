package com.seguranca.sisincidentes.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;

/**
 * Define o tipo de aplicação da categoria.
 */
public enum TipoCategoria {
    INCIDENTE,
    VULNERABILIDADE;

    @JsonCreator
    public static TipoCategoria fromString(String value) {
        return Arrays.stream(TipoCategoria.values())
                .filter(t -> t.name().equalsIgnoreCase(value))
                .findFirst()
                .orElse(null); // O Bean Validation (@NotNull) cuidará do erro se for inválido
    }
}
