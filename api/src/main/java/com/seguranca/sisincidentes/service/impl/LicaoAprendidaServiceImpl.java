package com.seguranca.sisincidentes.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seguranca.sisincidentes.api.dto.LicaoAprendidaRequestDTO;
import com.seguranca.sisincidentes.api.dto.LicaoAprendidaResponseDTO;
import com.seguranca.sisincidentes.api.exception.ResourceNotFoundException;
import com.seguranca.sisincidentes.domain.model.Incidente;
import com.seguranca.sisincidentes.domain.model.LicaoAprendida;
import com.seguranca.sisincidentes.domain.repository.IncidenteRepository;
import com.seguranca.sisincidentes.domain.repository.LicaoAprendidaRepository;
import com.seguranca.sisincidentes.service.LicaoAprendidaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementação concreta do serviço de Lições Aprendidas.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LicaoAprendidaServiceImpl implements LicaoAprendidaService {

    private static final String RESOURCE_NAME = "Lição Aprendida";

    private final LicaoAprendidaRepository licaoRepository;
    private final IncidenteRepository incidenteRepository;

    @Override
    @Transactional
    public LicaoAprendidaResponseDTO create(LicaoAprendidaRequestDTO requestDTO) {
        Objects.requireNonNull(requestDTO, "O DTO de requisição não pode ser nulo");
        Long incidenteId = Objects.requireNonNull(requestDTO.getIncidenteId(), "O ID do incidente não pode ser nulo");

        log.info("Iniciando registro de lição aprendida para o incidente ID: {}", incidenteId);

        Incidente incidente = incidenteRepository.findById(incidenteId)
                .orElseThrow(() -> new ResourceNotFoundException("Incidente", "id", incidenteId));

        licaoRepository.findByIncidenteId(incidenteId).ifPresent(l -> {
            throw new IllegalArgumentException(
                    String.format("O incidente ID %d já possui uma lição aprendida registrada.", incidenteId));
        });

        LicaoAprendida entity = toEntity(requestDTO, incidente);
        LicaoAprendida saved = Objects.requireNonNull(saveInternal(entity), "Erro ao persistir a lição aprendida");

        log.info("Lição aprendida registrada com sucesso. ID: {}", saved.getId());
        return toResponseDTO(saved);
    }

    @Override
    @Transactional
    public LicaoAprendidaResponseDTO update(Long id, LicaoAprendidaRequestDTO requestDTO) {
        Objects.requireNonNull(id, "O ID da lição aprendida não pode ser nulo para atualização.");
        Objects.requireNonNull(requestDTO, "Os dados para atualização não podem ser nulos.");
        log.info("Atualizando lição aprendida ID: {}", id);

        LicaoAprendida existing = licaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));

        if (!existing.getIncidente().getId().equals(requestDTO.getIncidenteId())) {
            Long novoIncidenteId = Objects.requireNonNull(requestDTO.getIncidenteId(), "O ID do novo incidente não pode ser nulo.");
            
            Incidente novoIncidente = incidenteRepository.findById(novoIncidenteId)
                    .orElseThrow(() -> new ResourceNotFoundException("Incidente", "id", novoIncidenteId));
            
            licaoRepository.findByIncidenteId(novoIncidenteId).ifPresent(l -> {
                throw new IllegalArgumentException(
                        String.format("O incidente ID %d já possui uma lição aprendida registrada.", novoIncidenteId));
            });
            existing.setIncidente(novoIncidente);
        }

        existing.setDescricaoResolucao(requestDTO.getDescricaoResolucao());
        LicaoAprendida updated = Objects.requireNonNull(saveInternal(existing));

        log.info("Lição aprendida ID {} atualizada com sucesso.", id);
        return toResponseDTO(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public LicaoAprendidaResponseDTO findByIncidenteId(Long incidenteId) {
        Long idValido = Objects.requireNonNull(incidenteId, "O ID do incidente não pode ser nulo.");
        log.debug("Buscando lição aprendida vinculada ao incidente ID: {}", idValido);
        return licaoRepository.findByIncidenteId(idValido)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "incidenteId", idValido));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LicaoAprendidaResponseDTO> findAll() {
        log.debug("Listando todas as lições aprendidas.");
        return licaoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Long idValido = Objects.requireNonNull(id, "O ID da lição aprendida não pode ser nulo");
        log.info("Excluindo lição aprendida ID: {}", idValido);
        if (!licaoRepository.existsById(idValido)) {
            throw new ResourceNotFoundException(RESOURCE_NAME, "id", idValido);
        }
        licaoRepository.deleteById(idValido);
    }

    private LicaoAprendida toEntity(LicaoAprendidaRequestDTO dto, Incidente incidente) {
        return LicaoAprendida.builder()
                .descricaoResolucao(dto.getDescricaoResolucao())
                .incidente(incidente)
                .build();
    }

    private LicaoAprendidaResponseDTO toResponseDTO(LicaoAprendida entity) {
        return LicaoAprendidaResponseDTO.builder()
                .id(entity.getId())
                .incidenteId(entity.getIncidente().getId())
                .incidenteTitulo(entity.getIncidente().getTitulo())
                .descricaoResolucao(entity.getDescricaoResolucao())
                .dataCriacao(entity.getDataCriacao())
                .build();
    }

    @Nullable
    private LicaoAprendida saveInternal(LicaoAprendida entity) {
        return licaoRepository.save(Objects.requireNonNull(entity, "Entidade não pode ser nula"));
    }
}