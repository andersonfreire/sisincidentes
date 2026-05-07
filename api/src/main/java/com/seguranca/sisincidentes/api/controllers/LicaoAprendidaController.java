package com.seguranca.sisincidentes.api.controllers;

import com.seguranca.sisincidentes.api.dto.LicaoAprendidaRequestDTO;
import com.seguranca.sisincidentes.api.dto.LicaoAprendidaResponseDTO;
import com.seguranca.sisincidentes.api.exception.ApiError;
import com.seguranca.sisincidentes.service.LicaoAprendidaService;
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
 * Controller REST para o gerenciamento de Lições Aprendidas.
 *
 * <p><b>RF08 — Gerenciamento de Lições Aprendidas</b></p>
 *
 * <p>Expõe endpoints para documentação de resoluções de incidentes,
 * permitindo o registro do conhecimento técnico obtido (Post-mortem).</p>
 */
@Slf4j
@RestController
@RequestMapping("/api/licoes-aprendidas")
@RequiredArgsConstructor
@Tag(name = "Lições Aprendidas", description = "RF08 — Documentação de resoluções e lições aprendidas")
public class LicaoAprendidaController {

    private final LicaoAprendidaService service;

    // =========================================================
    //  POST /api/licoes-aprendidas — Cadastrar
    // =========================================================

    @Operation(
        summary = "Registrar Lição Aprendida",
        description = "Vincula um relatório de lição aprendida a um incidente resolvido. " +
                      "Cada incidente admite apenas uma única lição aprendida (1:1)."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Lição aprendida registrada com sucesso",
            content = @Content(schema = @Schema(implementation = LicaoAprendidaResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou incidente já possui lição",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Incidente não encontrado",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public ResponseEntity<LicaoAprendidaResponseDTO> create(
            @Valid @RequestBody LicaoAprendidaRequestDTO requestDTO) {

        log.info("POST /api/licoes-aprendidas — Registrando resolução para incidente ID: {}", requestDTO.getIncidenteId());
        LicaoAprendidaResponseDTO response = service.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =========================================================
    //  GET /api/licoes-aprendidas — Listar todas
    // =========================================================

    @Operation(
        summary = "Listar todas as Lições Aprendidas",
        description = "Retorna o histórico completo de conhecimentos documentados."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<LicaoAprendidaResponseDTO>> findAll() {
        log.debug("GET /api/licoes-aprendidas — Listando histórico.");
        return ResponseEntity.ok(service.findAll());
    }

    // =========================================================
    //  GET /api/licoes-aprendidas/incidente/{id} — Buscar por Incidente
    // =========================================================

    @Operation(
        summary = "Buscar Lição por ID do Incidente",
        description = "Localiza a documentação técnica específica vinculada a um incidente."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Documentação encontrada"),
        @ApiResponse(responseCode = "404", description = "Lição aprendida não encontrada para este incidente",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/incidente/{incidenteId}")
    public ResponseEntity<LicaoAprendidaResponseDTO> findByIncidente(
            @Parameter(description = "ID do Incidente", required = true, example = "1")
            @PathVariable Long incidenteId) {

        log.debug("GET /api/licoes-aprendidas/incidente/{} — Buscando lição vinculada.", incidenteId);
        return ResponseEntity.ok(service.findByIncidenteId(incidenteId));
    }

    // =========================================================
    //  DELETE /api/licoes-aprendidas/{id} — Excluir
    // =========================================================

    @Operation(
        summary = "Excluir Lição Aprendida",
        description = "Remove permanentemente o registro de uma lição aprendida."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Registro excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Registro não encontrado",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID da Lição Aprendida", required = true, example = "1")
            @PathVariable Long id) {

        log.info("DELETE /api/licoes-aprendidas/{} — Removendo registro.", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}