package com.seguranca.sisincidentes.api.controllers;

import com.seguranca.sisincidentes.api.dto.InteracaoRequestDTO;
import com.seguranca.sisincidentes.api.dto.InteracaoResponseDTO;
import com.seguranca.sisincidentes.api.exception.ApiError;
import com.seguranca.sisincidentes.service.InteracaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/interacoes")
@RequiredArgsConstructor
@Tag(name = "Interações", description = "RF10 — Histórico de acompanhamento de incidentes")
public class InteracaoController {

    private final InteracaoService service;

    @Operation(summary = "Adicionar Interação", description = "Registra um novo comentário ou atualização de status no histórico do incidente.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Interação registrada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Incidente não encontrado", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public ResponseEntity<InteracaoResponseDTO> create(@Valid @RequestBody InteracaoRequestDTO requestDTO) {
        log.info("POST /api/interacoes — Adicionando comentário ao incidente: {}", requestDTO.getIncidenteId());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(requestDTO));
    }

    @Operation(summary = "Listar Interações por Incidente", description = "Retorna o histórico completo de acompanhamento de um incidente, ordenado do mais recente para o mais antigo.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Histórico retornado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Incidente não encontrado", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/incidente/{incidenteId}")
    public ResponseEntity<List<InteracaoResponseDTO>> findByIncidente(
            @Parameter(description = "ID do Incidente", required = true, example = "1")
            @PathVariable Long incidenteId) {
        log.debug("GET /api/interacoes/incidente/{} — Recuperando histórico.", incidenteId);
        return ResponseEntity.ok(service.findByIncidenteId(incidenteId));
    }

    @Operation(summary = "Remover Interação", description = "Exclui permanentemente um registro do histórico.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Registro excluído"),
        @ApiResponse(responseCode = "404", description = "Interação não encontrada", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID da Interação", required = true, example = "1")
            @PathVariable Long id) {
        log.info("DELETE /api/interacoes/{} — Removendo registro.", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}