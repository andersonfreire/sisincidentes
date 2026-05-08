package com.seguranca.sisincidentes.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seguranca.sisincidentes.api.dto.VulnerabilidadeRequestDTO;
import com.seguranca.sisincidentes.api.dto.VulnerabilidadeResponseDTO;
import com.seguranca.sisincidentes.domain.model.Vulnerabilidade;
import com.seguranca.sisincidentes.domain.repository.VulnerabilidadeRepository;
import com.seguranca.sisincidentes.service.VulnerabilidadeService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VulnerabilidadeServiceImpl implements VulnerabilidadeService {

    private final VulnerabilidadeRepository repository;

    @Override
    @Transactional
    public VulnerabilidadeResponseDTO create(VulnerabilidadeRequestDTO dto) {
        Vulnerabilidade entity = Vulnerabilidade.builder()
                .titulo(dto.getTitulo())
                .descricao(dto.getDescricao())
                .severidade(dto.getSeveridade())
                .build();
        Vulnerabilidade saved = saveAndValidate(entity);
        return toResponseDTO(saved);
    }

    @Override
    @Transactional
    public VulnerabilidadeResponseDTO update(Long id, VulnerabilidadeRequestDTO dto) {
        java.util.Objects.requireNonNull(id, "ID não pode ser nulo");
        Vulnerabilidade existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vulnerabilidade não encontrada"));

        existing.setTitulo(dto.getTitulo());
        existing.setDescricao(dto.getDescricao());
        existing.setSeveridade(dto.getSeveridade());

        Vulnerabilidade saved = saveAndValidate(existing);
        return toResponseDTO(saved);
    }

    @Override
    public VulnerabilidadeResponseDTO findById(Long id) {
        java.util.Objects.requireNonNull(id, "ID não pode ser nulo");
        return repository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Vulnerabilidade não encontrada"));
    }

    @Override
    public List<VulnerabilidadeResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Objects.requireNonNull(id, "ID não pode ser nulo");
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Vulnerabilidade não encontrada");
        }
        repository.deleteById(id);
    }

    /**
     * Salva uma entidade e valida que o retorno não seja nulo.
     * Encapsula a validação de nulidade para satisfazer a análise estática da IDE.
     */
    @NonNull
    @SuppressWarnings("null")
    private Vulnerabilidade saveAndValidate(Vulnerabilidade entity) {
        return Objects.requireNonNull(repository.save(entity));
    }

    private VulnerabilidadeResponseDTO toResponseDTO(Vulnerabilidade entity) {
        return VulnerabilidadeResponseDTO.builder()
                .id(entity.getId())
                .titulo(entity.getTitulo())
                .descricao(entity.getDescricao())
                .severidade(entity.getSeveridade())
                .dataCriacao(entity.getDataCriacao())
                .build();
    }
}