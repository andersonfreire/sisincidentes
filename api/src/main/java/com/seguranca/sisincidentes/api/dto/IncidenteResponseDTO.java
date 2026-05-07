package com.seguranca.sisincidentes.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de saída para representação detalhada de Incidentes.
 *
 * <p>Utilizado para retornar dados ao Frontend, incluindo a lista de 
 * vulnerabilidades associadas e metadados de auditoria.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "IncidenteResponse", description = "Representação detalhada de um incidente para o cliente")
public class IncidenteResponseDTO {

    @Schema(description = "ID do incidente", example = "1")
    private Long id;

    @Schema(description = "Título da ocorrência", example = "Tentativa de Invasão via Brute Force")
    private String titulo;

    @Schema(description = "Descrição detalhada", example = "Relato técnico da ocorrência...")
    private String descricao;

    @Schema(description = "Status atual", example = "ABERTO")
    private String status;

    @Schema(description = "Data e hora do registro")
    private LocalDateTime dataRegistro;

    @Schema(description = "Lista de vulnerabilidades vinculadas ao incidente")
    private List<VulnerabilidadeResponseDTO> vulnerabilidades;
}