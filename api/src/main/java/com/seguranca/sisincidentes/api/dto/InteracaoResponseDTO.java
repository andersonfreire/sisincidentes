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
@Schema(name = "InteracaoResponse", description = "Dados retornados de uma interação do histórico")
public class InteracaoResponseDTO {
    private Long id;
    private Long incidenteId;
    private String texto;
    private LocalDateTime dataCriacao;
}