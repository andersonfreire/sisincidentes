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
import java.util.Objects;
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
        Objects.requireNonNull(dto, "O DTO de interação não pode ser nulo");
        Long incidenteId = Objects.requireNonNull(dto.getIncidenteId(), "O ID do incidente não pode ser nulo");

        log.info("Registrando nova interação para o incidente ID: {}", incidenteId);

        Incidente incidente = incidenteRepository.findById(incidenteId)
                .orElseThrow(() -> new ResourceNotFoundException("Incidente", "id", incidenteId));

        Interacao interacao = Interacao.builder()
                .texto(dto.getTexto())
                .incidente(incidente)
                .build();

        Interacao persistida = interacaoRepository.save(Objects.requireNonNull(interacao, "Erro"));
        Interacao saved = Objects.requireNonNull(persistida, "Erro ao persistir a interação");
        return toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InteracaoResponseDTO> findByIncidenteId(Long incidenteId) {
        Long idValido = Objects.requireNonNull(incidenteId, "O ID do incidente não pode ser nulo");
        log.debug("Buscando histórico de interações do incidente ID: {}", idValido);

        if (!incidenteRepository.existsById(idValido)) {
            throw new ResourceNotFoundException("Incidente", "id", idValido);
        }

        return interacaoRepository.findByIncidenteIdOrderByDataCriacaoDesc(idValido)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Long idValido = Objects.requireNonNull(id, "O ID da interação não pode ser nulo");
        log.info("Excluindo interação ID: {}", idValido);

        if (!interacaoRepository.existsById(idValido)) {
            throw new ResourceNotFoundException("Interação", "id", idValido);
        }
        interacaoRepository.deleteById(idValido);
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