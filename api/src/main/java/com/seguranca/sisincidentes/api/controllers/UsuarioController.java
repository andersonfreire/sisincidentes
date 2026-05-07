package com.seguranca.sisincidentes.api.controllers;

import com.seguranca.sisincidentes.api.dto.UsuarioRequestDTO;
import com.seguranca.sisincidentes.api.dto.UsuarioResponseDTO;
import com.seguranca.sisincidentes.api.exception.ApiError;
import com.seguranca.sisincidentes.service.UsuarioService;
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
 * Controller REST para o gerenciamento de Usuários.
 *
 * <p><b>RF02 — Gerenciamento de Usuários</b></p>
 *
 * <p>Expõe os endpoints públicos em {@code /api/usuarios} para operações
 * de CRUD e toggle de status sobre os usuários do sistema.</p>
 */
@Slf4j
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "RF02 — Gerenciamento de Usuários do Sistema")
public class UsuarioController {

    private final UsuarioService service;

    // =========================================================
    //  POST /api/usuarios — Cadastrar
    // =========================================================

    @Operation(
        summary = "Cadastrar Usuário",
        description = "Cria um novo usuário. A senha informada é hasheada automaticamente e o e-mail deve ser único."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
            content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Unidade Administrativa não encontrada",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "422", description = "E-mail já em uso",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> create(
            @Valid @RequestBody UsuarioRequestDTO requestDTO) {

        log.info("POST /api/usuarios — Criando usuário: {}", requestDTO.getEmail());
        UsuarioResponseDTO response = service.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =========================================================
    //  GET /api/usuarios — Listar todos
    // =========================================================

    @Operation(
        summary = "Listar todos os Usuários",
        description = "Retorna a lista completa de usuários, ordenada por nome."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> findAll() {
        log.debug("GET /api/usuarios — Listando todos.");
        return ResponseEntity.ok(service.findAll());
    }

    // =========================================================
    //  GET /api/usuarios/{id} — Buscar por ID
    // =========================================================

    @Operation(
        summary = "Buscar Usuário por ID",
        description = "Retorna os dados de um usuário específico pelo seu identificador."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário encontrado",
            content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> findById(
            @Parameter(description = "ID do Usuário", required = true, example = "1")
            @PathVariable Long id) {

        log.debug("GET /api/usuarios/{} — Buscando por ID.", id);
        return ResponseEntity.ok(service.findById(id));
    }

    // =========================================================
    //  GET /api/usuarios/buscar — Buscar por nome
    // =========================================================

    @Operation(
        summary = "Pesquisar Usuários por Nome",
        description = "Filtra usuários cujo nome contenha o termo informado (busca parcial)."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Resultado da pesquisa")
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<UsuarioResponseDTO>> searchByNome(
            @Parameter(description = "Termo para busca no nome", required = true, example = "Silva")
            @RequestParam String nome) {

        log.debug("GET /api/usuarios/buscar?nome={}", nome);
        return ResponseEntity.ok(service.findByNome(nome));
    }

    // =========================================================
    //  GET /api/usuarios/unidade/{unidadeId} — Filtrar por unidade
    // =========================================================

    @Operation(
        summary = "Filtrar Usuários por Unidade Administrativa",
        description = "Retorna todos os usuários vinculados à unidade administrativa informada."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Resultado da pesquisa")
    })
    @GetMapping("/unidade/{unidadeId}")
    public ResponseEntity<List<UsuarioResponseDTO>> findByUnidade(
            @Parameter(description = "ID da Unidade Administrativa", required = true, example = "1")
            @PathVariable Long unidadeId) {

        log.debug("GET /api/usuarios/unidade/{} — Filtrando por unidade.", unidadeId);
        return ResponseEntity.ok(service.findByUnidade(unidadeId));
    }

    // =========================================================
    //  PUT /api/usuarios/{id} — Atualizar
    // =========================================================

    @Operation(
        summary = "Atualizar Usuário",
        description = "Atualiza os dados de um usuário existente. Se o campo senha for omitido, a senha atual é mantida."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Usuário ou Unidade não encontrados",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "422", description = "E-mail já em uso por outro usuário",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> update(
            @Parameter(description = "ID do Usuário", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequestDTO requestDTO) {

        log.info("PUT /api/usuarios/{} — Atualizando usuário.", id);
        return ResponseEntity.ok(service.update(id, requestDTO));
    }

    // =========================================================
    //  PATCH /api/usuarios/{id}/toggle-ativo — Ativar/Desativar
    // =========================================================

    @Operation(
        summary = "Ativar ou Desativar Usuário",
        description = "Inverte o status atual de ativo/inativo do usuário. Usuários inativos não poderão autenticar."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status alterado com sucesso",
            content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PatchMapping("/{id}/toggle-ativo")
    public ResponseEntity<UsuarioResponseDTO> toggleAtivo(
            @Parameter(description = "ID do Usuário", required = true, example = "1")
            @PathVariable Long id) {

        log.info("PATCH /api/usuarios/{}/toggle-ativo — Alternando status.", id);
        return ResponseEntity.ok(service.toggleAtivo(id));
    }

    // =========================================================
    //  DELETE /api/usuarios/{id} — Excluir
    // =========================================================

    @Operation(
        summary = "Excluir Usuário",
        description = "Remove permanentemente um usuário. Se o usuário já possuir vínculos (ex: incidentes registrados), prefira desativá-lo via PATCH."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso (sem conteúdo)"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do Usuário", required = true, example = "1")
            @PathVariable Long id) {

        log.info("DELETE /api/usuarios/{} — Excluindo usuário.", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
