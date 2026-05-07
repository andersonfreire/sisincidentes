package com.seguranca.sisincidentes.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "LicaoAprendidaResponse", description = "Dados da lição aprendida registrada")
public class LicaoAprendidaResponseDTO {

    private Long id;
    private Long incidenteId;
    private String incidenteTitulo;
    private String descricaoResolucao;
    private LocalDateTime dataCriacao;
}