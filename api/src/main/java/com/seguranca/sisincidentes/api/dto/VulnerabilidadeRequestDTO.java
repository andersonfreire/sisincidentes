package com.seguranca.sisincidentes.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para cadastro de Vulnerabilidades.
 *
 * <p><b>RF06 — Gerenciamento de Vulnerabilidades</b></p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "VulnerabilidadeRequest", description = "Dados para cadastro de uma nova vulnerabilidade no catálogo")
public class VulnerabilidadeRequestDTO {

    @NotBlank(message = "O campo 'título' é obrigatório.")
    @Size(min = 3, max = 150, message = "O título da vulnerabilidade deve ter entre 3 e 150 caracteres.")
    @Schema(description = "Nome descritivo da falha de segurança", example = "SQL Injection em formulário de contato", requiredMode = Schema.RequiredMode.REQUIRED)
    private String titulo;

    @NotBlank(message = "O campo 'severidade' é obrigatório.")
    @Pattern(regexp = "^(BAIXA|MEDIA|ALTA|CRITICA)$", message = "A severidade deve ser: BAIXA, MEDIA, ALTA ou CRITICA.")
    @Schema(description = "Nível de risco da vulnerabilidade", example = "CRITICA", requiredMode = Schema.RequiredMode.REQUIRED)
    private String severidade;

    @Size(max = 500, message = "A descrição técnica deve ter no máximo 500 caracteres.")
    @Schema(description = "Detalhes técnicos sobre a vulnerabilidade", example = "Falta de sanitização no parâmetro 'id' da query string.")
    private String descricao;
}