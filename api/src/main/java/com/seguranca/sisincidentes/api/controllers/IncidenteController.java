package com.seguranca.sisincidentes.api.controllers;

import com.seguranca.sisincidentes.api.dto.IncidenteRequestDTO;
import com.seguranca.sisincidentes.api.dto.IncidenteResponseDTO;
import com.seguranca.sisincidentes.api.exception.ApiError;
import com.seguranca.sisincidentes.service.IncidenteService;
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

/**
 * Controller REST para o gerenciamento de Incidentes de Segurança.
 *
 * <p><b>RF06 — Gerenciamento de Vulnerabilidades e Incidentes</b></p>
 */
@Slf4j
@RestController
@RequestMapping("/api/incidentes")
@RequiredArgsConstructor
@Tag(name = "Incidentes", description = "RF06 — Gestão de Vulnerabilidades e Incidentes de Segurança")
public class IncidenteController {

    private final IncidenteService service;

    // =========================================================
    //  POST /api/incidentes — Cadastrar
    // =========================================================

    @Operation(
        summary = "Cadastrar Incidente",
        description = "Registra um novo incidente de segurança. O status inicial é definido como ABERTO."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Incidente registrado com sucesso",
            content = @Content(schema = @Schema(implementation = IncidenteResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public ResponseEntity<IncidenteResponseDTO> create(
            @Valid @RequestBody IncidenteRequestDTO requestDTO) {

        log.info("POST /api/incidentes — Registrando incidente: {}", requestDTO.getTitulo());
        IncidenteResponseDTO response = service.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =========================================================
    //  GET /api/incidentes — Listar todos (RF07/RF09)
    // =========================================================

    @Operation(
        summary = "Listar todos os Incidentes",
        description = "Retorna a lista completa de incidentes, ordenada pelos mais recentes."
    )
    @GetMapping
    public ResponseEntity<List<IncidenteResponseDTO>> findAll() {
        log.debug("GET /api/incidentes — Listando todos.");
        return ResponseEntity.ok(service.findAll());
    }

    // =========================================================
    //  GET /api/incidentes/{id} — Buscar por ID
    // =========================================================

    @Operation(summary = "Buscar Incidente por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Incidente encontrado"),
        @ApiResponse(responseCode = "404", description = "Incidente não encontrado",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<IncidenteResponseDTO> findById(
            @Parameter(description = "ID do Incidente", required = true, example = "1")
            @PathVariable Long id) {

        log.debug("GET /api/incidentes/{} — Buscando por ID.", id);
        return ResponseEntity.ok(service.findById(id));
    }

    // =========================================================
    //  PUT /api/incidentes/{id} — Atualizar Status
    // =========================================================

    @Operation(summary = "Atualizar Incidente", description = "Permite alterar o título, descrição ou status do incidente.")
    @PutMapping("/{id}")
    public ResponseEntity<IncidenteResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody IncidenteRequestDTO requestDTO) {

        log.info("PUT /api/incidentes/{} — Atualizando registro.", id);
        return ResponseEntity.ok(service.update(id, requestDTO));
    }

    // =========================================================
    //  DELETE /api/incidentes/{id} — Excluir
    // =========================================================

    @Operation(summary = "Excluir Incidente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/incidentes/{} — Removendo registro.", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}