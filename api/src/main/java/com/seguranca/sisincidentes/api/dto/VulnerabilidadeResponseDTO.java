package com.seguranca.sisincidentes.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de saída para representação de Vulnerabilidades.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "VulnerabilidadeResponse", description = "Dados simplificados de uma vulnerabilidade")
public class VulnerabilidadeResponseDTO {

    @Schema(description = "ID da vulnerabilidade", example = "10")
    private Long id;

    @Schema(description = "Título da falha", example = "SQL Injection")
    private String titulo;

    @Schema(description = "Nível de severidade", example = "CRITICA")
    private String severidade;

    @Schema(description = "Descrição técnica")
    private String descricao;

    @Schema(description = "Data de cadastro no sistema")
    private LocalDateTime dataCriacao;
}