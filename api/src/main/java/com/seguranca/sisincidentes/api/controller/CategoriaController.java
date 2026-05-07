package com.seguranca.sisincidentes.api.controller;

import com.seguranca.sisincidentes.api.dto.CategoriaRequestDTO;
import com.seguranca.sisincidentes.api.dto.CategoriaResponseDTO;
import com.seguranca.sisincidentes.domain.model.TipoCategoria;
import com.seguranca.sisincidentes.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorias", description = "Gerenciamento de categorias de incidentes e vulnerabilidades (RF05)")
public class CategoriaController {

    private final CategoriaService service;

    @PostMapping
    @Operation(summary = "Criar nova categoria", description = "Permite o cadastro de uma nova categoria para classificação")
    public ResponseEntity<CategoriaResponseDTO> create(@Valid @RequestBody CategoriaRequestDTO requestDTO) {
        Objects.requireNonNull(requestDTO, "Os dados da categoria não podem ser nulos.");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(requestDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar categoria", description = "Atualiza os dados de uma categoria existente")
    public ResponseEntity<CategoriaResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CategoriaRequestDTO requestDTO) {
        Objects.requireNonNull(id, "O ID da categoria não pode ser nulo.");
        Objects.requireNonNull(requestDTO, "Os dados para atualização não podem ser nulos.");
        return ResponseEntity.ok(service.update(id, requestDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Retorna os detalhes de uma categoria específica")
    public ResponseEntity<CategoriaResponseDTO> findById(@PathVariable Long id) {
        Objects.requireNonNull(id, "O ID da categoria não pode ser nulo.");
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    @Operation(summary = "Listar todas", description = "Retorna todas as categorias cadastradas")
    public ResponseEntity<List<CategoriaResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Filtrar por tipo", description = "Retorna categorias filtradas por tipo (INCIDENTE ou VULNERABILIDADE)")
    public ResponseEntity<List<CategoriaResponseDTO>> findByTipo(@PathVariable TipoCategoria tipo) {
        Objects.requireNonNull(tipo, "O tipo da categoria não pode ser nulo.");
        return ResponseEntity.ok(service.findByTipo(tipo));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir categoria", description = "Remove uma categoria do sistema (Bloqueado para OPERADOR)")
    public void delete(@PathVariable Long id) {
        Objects.requireNonNull(id, "O ID da categoria não pode ser nulo para exclusão.");
        service.delete(id);
    }
}
