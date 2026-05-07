package com.seguranca.sisincidentes.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "LicaoAprendidaRequest", description = "Dados para registro de uma lição aprendida após a resolução de um incidente")
public class LicaoAprendidaRequestDTO {

    @NotNull(message = "O ID do incidente é obrigatório.")
    @Schema(description = "Identificador do incidente resolvido", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long incidenteId;

    @NotBlank(message = "A descrição da resolução é obrigatória.")
    @Size(min = 20, message = "A descrição da resolução deve ser detalhada (mínimo 20 caracteres).")
    @Schema(description = "Detalhamento técnico da solução aplicada", example = "Bloqueio do IP de origem no firewall corporativo e atualização de patches.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String descricaoResolucao;
}