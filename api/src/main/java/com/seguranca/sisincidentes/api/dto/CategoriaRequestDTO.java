package com.seguranca.sisincidentes.api.dto;

import com.seguranca.sisincidentes.domain.model.TipoCategoria;
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
@Schema(name = "CategoriaRequest", description = "Dados para cadastro ou atualização de categoria")
public class CategoriaRequestDTO {

    @NotBlank(message = "O nome da categoria é obrigatório")
    @Size(max = 100, message = "O nome não pode exceder 100 caracteres")
    @Schema(description = "Nome da categoria", example = "Malware")
    private String nome;

    @Size(max = 255, message = "A descrição não pode exceder 255 caracteres")
    @Schema(description = "Descrição detalhada da categoria", example = "Incidentes envolvendo softwares maliciosos")
    private String descricao;

    @NotNull(message = "O tipo da categoria é obrigatório")
    @Schema(description = "Tipo de aplicação da categoria", example = "INCIDENTE")
    private TipoCategoria tipo;
}
