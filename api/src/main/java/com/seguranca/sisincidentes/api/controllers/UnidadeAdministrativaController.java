package com.seguranca.sisincidentes.api.controllers;

import com.seguranca.sisincidentes.api.dto.UnidadeAdministrativaRequestDTO;
import com.seguranca.sisincidentes.api.dto.UnidadeAdministrativaResponseDTO;
import com.seguranca.sisincidentes.api.exception.ApiError;
import com.seguranca.sisincidentes.service.UnidadeAdministrativaService;
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
import java.util.Objects;

/**
 * Controller REST para o gerenciamento de Unidades Administrativas.
 *
 * <p><b>RF01 — Gerenciamento de Unidades Administrativas</b></p>
 *
 * <p>Expõe os endpoints públicos em {@code /api/unidades} para operações
 * de CRUD completo sobre as unidades administrativas do sistema.</p>
 */
@Slf4j
@RestController
@RequestMapping("/api/unidades")
@RequiredArgsConstructor
@Tag(name = "Unidades Administrativas", description = "RF01 — Gerenciamento de Unidades Administrativas")
public class UnidadeAdministrativaController {

    private final UnidadeAdministrativaService service;

    // =========================================================
    //  POST /api/unidades — Cadastrar
    // =========================================================

    @Operation(
        summary = "Cadastrar Unidade Administrativa",
        description = "Cria uma nova Unidade Administrativa. O código deve ser único no sistema."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Unidade criada com sucesso",
            content = @Content(schema = @Schema(implementation = UnidadeAdministrativaResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "409", description = "Código já em uso",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "422", description = "Regra de negócio violada",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public ResponseEntity<UnidadeAdministrativaResponseDTO> create(
            @Valid @RequestBody UnidadeAdministrativaRequestDTO requestDTO) {

        log.info("POST /api/unidades — Criando unidade: {}", requestDTO.getCodigo());
        UnidadeAdministrativaResponseDTO response = service.create(Objects.requireNonNull(requestDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =========================================================
    //  GET /api/unidades — Listar todas
    // =========================================================

    @Operation(
        summary = "Listar todas as Unidades Administrativas",
        description = "Retorna a lista completa de unidades, ordenada por título de forma crescente."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<UnidadeAdministrativaResponseDTO>> findAll() {
        log.debug("GET /api/unidades — Listando todas.");
        return ResponseEntity.ok(service.findAll());
    }

    // =========================================================
    //  GET /api/unidades/{id} — Buscar por ID
    // =========================================================

    @Operation(
        summary = "Buscar Unidade Administrativa por ID",
        description = "Retorna os dados de uma unidade específica pelo seu identificador."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Unidade encontrada",
            content = @Content(schema = @Schema(implementation = UnidadeAdministrativaResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Unidade não encontrada",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<UnidadeAdministrativaResponseDTO> findById(
            @Parameter(description = "ID da Unidade Administrativa", required = true, example = "1")
            @PathVariable Long id) {

        log.debug("GET /api/unidades/{} — Buscando por ID.", id);
        return ResponseEntity.ok(service.findById(Objects.requireNonNull(id)));
    }

    // =========================================================
    //  GET /api/unidades/buscar — Buscar por sigla ou título
    // =========================================================

    @Operation(
        summary = "Pesquisar Unidades Administrativas",
        description = "Filtra unidades por sigla ou título (busca parcial, case-insensitive). " +
                      "Informe apenas um dos parâmetros por vez."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Resultado da pesquisa"),
        @ApiResponse(responseCode = "400", description = "Nenhum parâmetro de busca informado",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<UnidadeAdministrativaResponseDTO>> search(
            @Parameter(description = "Filtrar por sigla (parcial)", example = "STI")
            @RequestParam(required = false) String sigla,
            @Parameter(description = "Filtrar por título (parcial)", example = "Superintendência")
            @RequestParam(required = false) String titulo) {

        if (sigla != null && !sigla.isBlank()) {
            log.debug("GET /api/unidades/buscar?sigla={}", sigla);
            return ResponseEntity.ok(service.findBySigla(sigla));
        }

        if (titulo != null && !titulo.isBlank()) {
            log.debug("GET /api/unidades/buscar?titulo={}", titulo);
            return ResponseEntity.ok(service.findByTitulo(titulo));
        }

        throw new IllegalArgumentException("Informe ao menos um parâmetro de busca: 'sigla' ou 'titulo'.");
    }

    // =========================================================
    //  PUT /api/unidades/{id} — Atualizar
    // =========================================================

    @Operation(
        summary = "Atualizar Unidade Administrativa",
        description = "Atualiza os dados de uma Unidade Administrativa existente."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Unidade atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = UnidadeAdministrativaResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Unidade não encontrada",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "422", description = "Código já em uso por outra unidade",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<UnidadeAdministrativaResponseDTO> update(
            @Parameter(description = "ID da Unidade Administrativa", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UnidadeAdministrativaRequestDTO requestDTO) {

        log.info("PUT /api/unidades/{} — Atualizando unidade.", id);
        return ResponseEntity.ok(service.update(Objects.requireNonNull(id), Objects.requireNonNull(requestDTO)));
    }

    // =========================================================
    //  DELETE /api/unidades/{id} — Excluir
    // =========================================================

    @Operation(
        summary = "Excluir Unidade Administrativa",
        description = "Remove permanentemente uma Unidade Administrativa pelo seu ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Unidade excluída com sucesso (sem conteúdo)"),
        @ApiResponse(responseCode = "404", description = "Unidade não encontrada",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID da Unidade Administrativa", required = true, example = "1")
            @PathVariable Long id) {

        log.info("DELETE /api/unidades/{} — Excluindo unidade.", id);
        service.delete(Objects.requireNonNull(id));
        return ResponseEntity.noContent().build();
    }
}
