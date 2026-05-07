package com.seguranca.sisincidentes.service.impl;

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
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementação concreta do serviço de Lições Aprendidas.
 *
 * <p>Concentra a lógica de negócio para documentação post-mortem:</p>
 * <ul>
 * <li>Validação de unicidade para relacionamento 1:1 com Incidentes.</li>
 * <li>Mapeamento manual entre Entidade e DTO via Builder.</li>
 * <li>Persistência transacional dos dados.</li>
 * </ul>
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

        // Valida restrição de integridade 1:1
        licaoRepository.findByIncidenteId(incidenteId).ifPresent(l -> {
            throw new IllegalArgumentException(
                String.format("O incidente ID %d já possui uma lição aprendida registrada.", incidenteId)
            );
        });

        LicaoAprendida entity = toEntity(requestDTO, incidente);

        // Chamada via método auxiliar para conformidade com @Nullable e segurança de tipos
        LicaoAprendida saved = Objects.requireNonNull(saveInternal(entity), "Erro ao persistir a lição aprendida");

        log.info("Lição aprendida registrada com sucesso. ID: {}", saved.getId());
        return toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public LicaoAprendidaResponseDTO findByIncidenteId(Long incidenteId) {
        log.debug("Buscando lição aprendida vinculada ao incidente ID: {}", incidenteId);
        return licaoRepository.findByIncidenteId(incidenteId)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "incidenteId", incidenteId));
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
        // Linhas 88 e 91: Validação defensiva imediata para informar ao compilador
        Long idValido = Objects.requireNonNull(id, "O ID da lição aprendida não pode ser nulo");

        log.info("Excluindo lição aprendida ID: {}", idValido);
        if (!licaoRepository.existsById(idValido)) {
            throw new ResourceNotFoundException(RESOURCE_NAME, "id", idValido);
        }
        licaoRepository.deleteById(idValido);
    }

    // =========================================================
    //  Mapeamentos e Helpers
    // =========================================================

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

    /**
     * Método auxiliar para conformidade de nulidade.
     * Encapsula o retorno do repositório permitindo o uso de @Nullable no contrato do método.
     */
    @Nullable
    private LicaoAprendida saveInternal(LicaoAprendida entity) {
        return licaoRepository.save(entity);
    }
}