package com.seguranca.sisincidentes.api.dto;

import com.seguranca.sisincidentes.domain.model.TipoCategoria;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CategoriaResponse", description = "Dados de retorno da categoria")
public class CategoriaResponseDTO {

    @Schema(description = "Identificador único da categoria", example = "1")
    private Long id;

    @Schema(description = "Nome da categoria", example = "Malware")
    private String nome;

    @Schema(description = "Descrição da categoria", example = "Incidentes envolvendo softwares maliciosos")
    private String descricao;

    @Schema(description = "Tipo da categoria", example = "INCIDENTE")
    private TipoCategoria tipo;
}
