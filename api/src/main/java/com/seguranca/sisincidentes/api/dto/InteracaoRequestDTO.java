package com.seguranca.sisincidentes.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "InteracaoRequest", description = "Dados para inserção de um novo comentário/atualização no incidente")
public class InteracaoRequestDTO {

    @NotNull(message = "O ID do incidente é obrigatório.")
    @Schema(description = "Identificador do incidente", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long incidenteId;

    @NotBlank(message = "O texto da interação não pode ser vazio.")
    @Schema(description = "Conteúdo da atualização ou comentário", example = "Análise preliminar iniciada pela equipe de redes.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String texto;
}