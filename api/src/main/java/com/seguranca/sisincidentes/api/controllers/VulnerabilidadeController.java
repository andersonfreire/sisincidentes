package com.seguranca.sisincidentes.api.controllers;

import com.seguranca.sisincidentes.api.dto.VulnerabilidadeRequestDTO;
import com.seguranca.sisincidentes.api.dto.VulnerabilidadeResponseDTO;
import com.seguranca.sisincidentes.service.VulnerabilidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vulnerabilidades")
@RequiredArgsConstructor
@Tag(name = "Vulnerabilidades", description = "Gerenciamento de vulnerabilidades (CRUD completo - 6º entidade)")
public class VulnerabilidadeController {

    private final VulnerabilidadeService service;

    @PostMapping
    @Operation(summary = "Criar vulnerabilidade")
    public ResponseEntity<VulnerabilidadeResponseDTO> create(@Valid @RequestBody VulnerabilidadeRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping
    @Operation(summary = "Listar todas")
    public ResponseEntity<List<VulnerabilidadeResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID")
    public ResponseEntity<VulnerabilidadeResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar vulnerabilidade")
    public ResponseEntity<VulnerabilidadeResponseDTO> update(@PathVariable Long id, @Valid @RequestBody VulnerabilidadeRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir vulnerabilidade")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
