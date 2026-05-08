package com.seguranca.sisincidentes.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO de entrada para registro e atualização de Incidentes de Segurança.
 *
 * <p><b>RF06 — Gerenciamento de Vulnerabilidades e Incidentes</b></p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "IncidenteRequest", description = "Dados para abertura ou edição de um incidente de segurança")
public class IncidenteRequestDTO {

    @NotBlank(message = "O campo 'título' é obrigatório.")
    @Size(min = 5, max = 150, message = "O campo 'título' deve ter entre 5 e 150 caracteres.")
    @Schema(description = "Título resumido da ocorrência", example = "Tentativa de Invasão via Brute Force", requiredMode = Schema.RequiredMode.REQUIRED)
    private String titulo;

    @NotBlank(message = "O campo 'descrição' é obrigatório.")
    @Size(min = 10, message = "A descrição deve fornecer detalhes suficientes (mínimo 10 caracteres).")
    @Schema(description = "Relato detalhado do incidente ocorrido", example = "Detectadas múltiplas tentativas de login malsucedidas originadas do IP 192.168.1.50.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String descricao;

    @Schema(description = "Status atual do incidente", example = "ABERTO", allowableValues = {"ABERTO", "EM_ANALISE", "RESOLVIDO", "FECHADO"})
    private String status;

    @Schema(description = "Número do chamado relacionado")
    private String numeroChamado;

    @Schema(description = "Tarefa relacionada no sistema de gestão")
    private String tarefaRelacionada;

    @Schema(description = "Tipo da ocorrência", example = "Incidente")
    private String tipo;

    @Schema(description = "Prioridade do incidente", example = "Alta")
    private String prioridade;

    @Schema(description = "IP de origem da ocorrência")
    private String ipOrigem;

    @Schema(description = "IP de destino da ocorrência")
    private String ipDestino;

    @Schema(description = "Host afetado")
    private String host;

    @Schema(description = "Tempo estimado para resolução")
    private String tempoEstimado;

    @Schema(description = "Emails em cópia (CC)")
    private String cc;

    @Schema(description = "Notas adicionais")
    private String notas;

    @Schema(description = "ID da categoria associada")
    private Long categoriaId;

    @Schema(description = "ID da unidade administrativa associada")
    private Long unidadeId;

    @Schema(description = "ID do autor do registro")
    private Long autorId;

    @Schema(description = "ID do usuário atribuído")
    private Long atribuidoId;

    @Schema(description = "Lista de IDs das vulnerabilidades associadas ao incidente", example = "[1, 2]", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<Long> vulnerabilidadesIds;
}