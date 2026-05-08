package com.seguranca.sisincidentes.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VulnerabilidadeRequestDTO {

    @NotBlank(message = "O título é obrigatório")
    @Size(max = 150)
    @Schema(example = "SQL Injection")
    private String titulo;

    @Size(max = 500)
    private String descricao;

    @NotBlank(message = "A severidade é obrigatória")
    @Schema(example = "CRITICA")
    private String severidade;
}