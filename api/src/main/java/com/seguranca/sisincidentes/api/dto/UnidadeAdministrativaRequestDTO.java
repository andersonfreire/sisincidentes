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
 * DTO de entrada para criação e atualização de Unidades Administrativas.
 *
 * <p>Contém todas as validações Bean Validation (Hibernate Validator)
 * aplicadas antes de chegar à camada de serviço.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UnidadeAdministrativaRequest", description = "Dados para cadastro ou atualização de uma Unidade Administrativa")
public class UnidadeAdministrativaRequestDTO {

    @NotBlank(message = "O campo 'código' é obrigatório.")
    @Size(min = 1, max = 50, message = "O campo 'código' deve ter entre 1 e 50 caracteres.")
    @Pattern(
        regexp = "^[\\w.\\-/]+$",
        message = "O campo 'código' deve conter apenas letras, números, pontos, hifens ou barras."
    )
    @Schema(description = "Código organizacional único da unidade", example = "1.25.01", requiredMode = Schema.RequiredMode.REQUIRED)
    private String codigo;

    @NotBlank(message = "O campo 'sigla' é obrigatório.")
    @Size(min = 1, max = 20, message = "O campo 'sigla' deve ter entre 1 e 20 caracteres.")
    @Schema(description = "Sigla identificadora da unidade", example = "STI", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sigla;

    @NotBlank(message = "O campo 'título' é obrigatório.")
    @Size(min = 3, max = 200, message = "O campo 'título' deve ter entre 3 e 200 caracteres.")
    @Schema(description = "Nome completo da unidade administrativa", example = "Superintendência de Tecnologia da Informação", requiredMode = Schema.RequiredMode.REQUIRED)
    private String titulo;

    @Size(max = 150, message = "O campo 'responsável' deve ter no máximo 150 caracteres.")
    @Schema(description = "Nome do gestor ou responsável pela unidade", example = "João Silva")
    private String responsavel;

    @Size(max = 300, message = "O campo 'contato' deve ter no máximo 300 caracteres.")
    @Schema(description = "Informações de contato: telefone, e-mail ou ramal", example = "(95) 3212-3456 | sti@exemplo.gov.br")
    private String contato;
}
