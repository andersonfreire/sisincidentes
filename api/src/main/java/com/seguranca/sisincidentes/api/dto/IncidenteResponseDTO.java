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

    @Schema(description = "Número do chamado relacionado")
    private String numeroChamado;

    @Schema(description = "Tarefa relacionada")
    private String tarefaRelacionada;

    @Schema(description = "Tipo da ocorrência")
    private String tipo;

    @Schema(description = "Prioridade")
    private String prioridade;

    @Schema(description = "IP de origem")
    private String ipOrigem;

    @Schema(description = "IP de destino")
    private String ipDestino;

    @Schema(description = "Host afetado")
    private String host;

    @Schema(description = "Tempo estimado")
    private String tempoEstimado;

    @Schema(description = "Emails em cópia (CC)")
    private String cc;

    @Schema(description = "Notas adicionais")
    private String notas;

    @Schema(description = "ID da categoria")
    private Long categoriaId;

    @Schema(description = "Nome da categoria")
    private String categoriaNome;

    @Schema(description = "ID da unidade")
    private Long unidadeId;

    @Schema(description = "Sigla da unidade")
    private String unidadeSigla;

    @Schema(description = "ID do autor")
    private Long autorId;

    @Schema(description = "Nome do autor")
    private String autorNome;

    @Schema(description = "ID do usuário atribuído")
    private Long atribuidoId;

    @Schema(description = "Nome do usuário atribuído")
    private String atribuidoNome;

    @Schema(description = "Data e hora do registro")
    private LocalDateTime dataRegistro;

    @Schema(description = "Lista de vulnerabilidades vinculadas ao incidente")
    private List<VulnerabilidadeResponseDTO> vulnerabilidades;
}