package com.seguranca.sisincidentes.service.impl;

import com.seguranca.sisincidentes.api.dto.InteracaoRequestDTO;
import com.seguranca.sisincidentes.api.dto.InteracaoResponseDTO;
import com.seguranca.sisincidentes.api.exception.ResourceNotFoundException;
import com.seguranca.sisincidentes.domain.model.Incidente;
import com.seguranca.sisincidentes.domain.model.Interacao;
import com.seguranca.sisincidentes.domain.repository.IncidenteRepository;
import com.seguranca.sisincidentes.domain.repository.InteracaoRepository;
import com.seguranca.sisincidentes.service.InteracaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InteracaoServiceImpl implements InteracaoService {

    private final InteracaoRepository interacaoRepository;
    private final IncidenteRepository incidenteRepository;

    @Override
    @Transactional
    public InteracaoResponseDTO create(InteracaoRequestDTO dto) {
        log.info("Registrando nova interação para o incidente ID: {}", dto.getIncidenteId());

        Incidente incidente = incidenteRepository.findById(dto.getIncidenteId())
                .orElseThrow(() -> new ResourceNotFoundException("Incidente", "id", dto.getIncidenteId()));

        Interacao interacao = Interacao.builder()
                .texto(dto.getTexto())
                .incidente(incidente)
                .build();

        return toResponseDTO(interacaoRepository.save(interacao));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InteracaoResponseDTO> findByIncidenteId(Long incidenteId) {
        log.debug("Buscando histórico de interações do incidente ID: {}", incidenteId);
        
        if (!incidenteRepository.existsById(incidenteId)) {
            throw new ResourceNotFoundException("Incidente", "id", incidenteId);
        }

        return interacaoRepository.findByIncidenteIdOrderByDataCriacaoDesc(incidenteId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Excluindo interação ID: {}", id);
        if (!interacaoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Interação", "id", id);
        }
        interacaoRepository.deleteById(id);
    }

    private InteracaoResponseDTO toResponseDTO(Interacao entity) {
        return InteracaoResponseDTO.builder()
                .id(entity.getId())
                .incidenteId(entity.getIncidente().getId())
                .texto(entity.getTexto())
                .dataCriacao(entity.getDataCriacao())
                .build();
    }
}