package com.seguranca.sisincidentes.service.impl;

import com.seguranca.sisincidentes.api.dto.IncidenteRequestDTO;
import com.seguranca.sisincidentes.api.dto.IncidenteResponseDTO;
import com.seguranca.sisincidentes.api.dto.VulnerabilidadeResponseDTO;
import com.seguranca.sisincidentes.api.exception.ResourceNotFoundException;
import com.seguranca.sisincidentes.domain.model.Incidente;
import com.seguranca.sisincidentes.domain.model.Vulnerabilidade;
import com.seguranca.sisincidentes.domain.repository.IncidenteRepository;
import com.seguranca.sisincidentes.domain.repository.VulnerabilidadeRepository;
import com.seguranca.sisincidentes.service.IncidenteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação concreta do serviço de Incidentes.
 *
 * <p>Concentra a lógica de negócio do RF06:</p>
 * <ul>
 * <li>Resolução do relacionamento Many-to-Many com {@link Vulnerabilidade}</li>
 * <li>Gestão de status do incidente</li>
 * <li>Mapeamento DTO ↔ Entidade</li>
 * </ul>
 *
 * <p><b>RF06 — Gerenciamento de Vulnerabilidades e Incidentes</b></p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IncidenteServiceImpl implements IncidenteService {

    private static final String RESOURCE_NAME = "Incidente";

    private final IncidenteRepository incidenteRepository;
    private final VulnerabilidadeRepository vulnerabilidadeRepository;

    @Override
    @Transactional
    public IncidenteResponseDTO create(IncidenteRequestDTO requestDTO) {
        log.info("Registrando novo incidente: {}", requestDTO.getTitulo());

        // Resolve a lista de vulnerabilidades a partir dos IDs informados
        List<Vulnerabilidade> vulnerabilidades = findVulnerabilidadesOrThrow(requestDTO.getVulnerabilidadesIds());

        Incidente entity = toEntity(requestDTO, vulnerabilidades);
        // Garante o status inicial conforme regra de negócio
        entity.setStatus("ABERTO");

        Incidente saved = incidenteRepository.save(entity);

        log.info("Incidente registrado com sucesso. ID: {}", saved.getId());
        return toResponseDTO(saved);
    }

    @Override
    @Transactional
    public IncidenteResponseDTO update(Long id, IncidenteRequestDTO requestDTO) {
        log.info("Atualizando incidente ID: {}", id);

        Incidente existing = findIncidenteOrThrow(id);
        List<Vulnerabilidade> vulnerabilidades = findVulnerabilidadesOrThrow(requestDTO.getVulnerabilidadesIds());

        existing.setTitulo(requestDTO.getTitulo());
        existing.setDescricao(requestDTO.getDescricao());
        existing.setStatus(requestDTO.getStatus() != null ? requestDTO.getStatus() : existing.getStatus());
        existing.setVulnerabilidades(vulnerabilidades);

        Incidente updated = incidenteRepository.save(existing);
        log.info("Incidente ID {} atualizado com sucesso.", id);
        return toResponseDTO(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncidenteResponseDTO> findAll() {
        log.debug("Listando todos os incidentes.");
        return incidenteRepository.findAllOrderByDataRegistroDesc()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public IncidenteResponseDTO findById(Long id) {
        log.debug("Buscando incidente por ID: {}", id);
        return toResponseDTO(findIncidenteOrThrow(id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Excluindo incidente ID: {}", id);
        if (!incidenteRepository.existsById(id)) {
            throw new ResourceNotFoundException(RESOURCE_NAME, "id", id);
        }
        incidenteRepository.deleteById(id);
        log.info("Incidente ID {} excluído com sucesso.", id);
    }

    // =========================================================
    //  Helpers privados
    // =========================================================

    private Incidente findIncidenteOrThrow(Long id) {
        return incidenteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
    }

    private List<Vulnerabilidade> findVulnerabilidadesOrThrow(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        List<Vulnerabilidade> found = vulnerabilidadeRepository.findAllById(ids);
        if (found.size() != ids.size()) {
            throw new ResourceNotFoundException("Vulnerabilidade", "ids", ids);
        }
        return found;
    }

    // =========================================================
    //  Mapeamentos DTO ↔ Entidade
    // =========================================================

    private Incidente toEntity(IncidenteRequestDTO dto, List<Vulnerabilidade> vulnerabilidades) {
        return Incidente.builder()
                .titulo(dto.getTitulo())
                .descricao(dto.getDescricao())
                .status(dto.getStatus())
                .vulnerabilidades(vulnerabilidades)
                .build();
    }

    private IncidenteResponseDTO toResponseDTO(Incidente entity) {
        return IncidenteResponseDTO.builder()
                .id(entity.getId())
                .titulo(entity.getTitulo())
                .descricao(entity.getDescricao())
                .status(entity.getStatus())
                .dataRegistro(entity.getDataRegistro())
                .vulnerabilidades(entity.getVulnerabilidades().stream()
                        .map(v -> VulnerabilidadeResponseDTO.builder()
                                .id(v.getId())
                                .titulo(v.getTitulo())
                                .severidade(v.getSeveridade())
                                .descricao(v.getDescricao())
                                .dataCriacao(v.getDataCriacao())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}