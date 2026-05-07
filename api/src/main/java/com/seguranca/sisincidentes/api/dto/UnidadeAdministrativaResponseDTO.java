package com.seguranca.sisincidentes.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de saída para representação de uma Unidade Administrativa nas respostas da API.
 *
 * <p>Garante que apenas os campos pertinentes sejam expostos ao cliente,
 * desacoplando a entidade JPA do contrato público da API REST.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UnidadeAdministrativaResponse", description = "Dados de resposta de uma Unidade Administrativa")
public class UnidadeAdministrativaResponseDTO {

    @Schema(description = "Identificador único da unidade", example = "1")
    private Long id;

    @Schema(description = "Código organizacional único da unidade", example = "1.25.01")
    private String codigo;

    @Schema(description = "Sigla identificadora da unidade", example = "STI")
    private String sigla;

    @Schema(description = "Nome completo da unidade administrativa", example = "Superintendência de Tecnologia da Informação")
    private String titulo;

    @Schema(description = "Nome do gestor ou responsável pela unidade")
    private String responsavel;

    @Schema(description = "Informações de contato: telefone, e-mail ou ramal")
    private String contato;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Data e hora de criação do registro", example = "2025-01-15T10:30:00")
    private LocalDateTime dataCriacao;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Data e hora da última atualização do registro", example = "2025-03-20T14:45:00")
    private LocalDateTime dataAtualizacao;
}
