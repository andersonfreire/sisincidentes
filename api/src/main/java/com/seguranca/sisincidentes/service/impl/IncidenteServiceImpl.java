package com.seguranca.sisincidentes.service.impl;

import com.seguranca.sisincidentes.api.dto.IncidenteRequestDTO;
import com.seguranca.sisincidentes.api.dto.IncidenteResponseDTO;
import com.seguranca.sisincidentes.api.dto.VulnerabilidadeResponseDTO;
import com.seguranca.sisincidentes.api.exception.ForbiddenOperationException;
import com.seguranca.sisincidentes.api.exception.ResourceNotFoundException;
import com.seguranca.sisincidentes.domain.model.*;
import com.seguranca.sisincidentes.domain.repository.*;
import com.seguranca.sisincidentes.security.UserDetailsImpl;
import com.seguranca.sisincidentes.service.IncidenteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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
    private final CategoriaRepository categoriaRepository;
    private final UnidadeAdministrativaRepository unidadeRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public IncidenteResponseDTO create(@NonNull IncidenteRequestDTO requestDTO) {
        Objects.requireNonNull(requestDTO, "Os dados do incidente não podem ser nulos.");
        log.info("Registrando novo incidente: {}", requestDTO.getTitulo());

        // Resolve a lista de vulnerabilidades a partir dos IDs informados
        List<Vulnerabilidade> vulnerabilidades = findVulnerabilidadesOrThrow(requestDTO.getVulnerabilidadesIds());

        Incidente entity = toEntity(requestDTO, vulnerabilidades);
        // Garante o status inicial conforme regra de negócio
        entity.setStatus("ABERTO");

        Incidente saved = saveAndValidate(entity);

        log.info("Incidente registrado com sucesso. ID: {}", saved.getId());
        return toResponseDTO(saved);
    }

    @Override
    @Transactional
    public IncidenteResponseDTO update(@NonNull Long id, @NonNull IncidenteRequestDTO requestDTO) {
        Objects.requireNonNull(id, "O ID do incidente não pode ser nulo.");
        Objects.requireNonNull(requestDTO, "Os dados para atualização não podem ser nulos.");
        log.info("Atualizando incidente ID: {}", id);

        Incidente existing = findIncidenteOrThrow(id);
        List<Vulnerabilidade> vulnerabilidades = findVulnerabilidadesOrThrow(requestDTO.getVulnerabilidadesIds());

        existing.setTitulo(requestDTO.getTitulo());
        existing.setDescricao(requestDTO.getDescricao());
        existing.setStatus(requestDTO.getStatus() != null ? requestDTO.getStatus() : existing.getStatus());
        existing.setVulnerabilidades(vulnerabilidades);

        // Novos campos
        existing.setNumeroChamado(requestDTO.getNumeroChamado());
        existing.setTarefaRelacionada(requestDTO.getTarefaRelacionada());
        existing.setTipo(requestDTO.getTipo());
        existing.setPrioridade(requestDTO.getPrioridade());
        existing.setIpOrigem(requestDTO.getIpOrigem());
        existing.setIpDestino(requestDTO.getIpDestino());
        existing.setHost(requestDTO.getHost());
        existing.setTempoEstimado(requestDTO.getTempoEstimado());
        existing.setCc(requestDTO.getCc());
        existing.setNotas(requestDTO.getNotas());

        // Resolução de relacionamentos
        java.util.Optional.ofNullable(requestDTO.getCategoriaId()).flatMap(categoriaRepository::findById).ifPresent(existing::setCategoria);
        java.util.Optional.ofNullable(requestDTO.getUnidadeId()).flatMap(unidadeRepository::findById).ifPresent(existing::setUnidade);
        java.util.Optional.ofNullable(requestDTO.getAutorId()).flatMap(usuarioRepository::findById).ifPresent(existing::setAutor);
        java.util.Optional.ofNullable(requestDTO.getAtribuidoId()).flatMap(usuarioRepository::findById).ifPresent(existing::setAtribuido);

        Incidente updated = saveAndValidate(existing);
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
    public IncidenteResponseDTO findById(@NonNull Long id) {
        Objects.requireNonNull(id, "O ID do incidente não pode ser nulo.");
        log.debug("Buscando incidente por ID: {}", id);
        return toResponseDTO(findIncidenteOrThrow(id));
    }

    @Override
    @Transactional
    public void delete(@NonNull Long id) {
        // RF04 — Bloqueio de exclusão para OPERADOR
        if (!isAuthorizedToChangeSensitiveData()) {
            throw new ForbiddenOperationException("O perfil OPERADOR não tem permissão para excluir incidentes.");
        }

        Objects.requireNonNull(id, "O ID do incidente não pode ser nulo.");
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

    private Incidente findIncidenteOrThrow(@NonNull Long id) {
        Objects.requireNonNull(id, "O ID para busca não pode ser nulo.");
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

    private boolean isAuthorizedToChangeSensitiveData() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsImpl) {
            Perfil perfil = ((UserDetailsImpl) principal).getUsuario().getPerfil();
            return perfil == Perfil.ADMIN || perfil == Perfil.GESTOR_TI;
        }
        return false;
    }

    /**
     * Salva uma entidade e valida que o retorno não seja nulo.
     * Encapsula a validação de nulidade para satisfazer a análise estática da IDE.
     * O framework já gerencia a nulidade e garantimos a segurança com requireNonNull.
     */
    @NonNull
    @SuppressWarnings("null")
    private Incidente saveAndValidate(Incidente entity) {
        return Objects.requireNonNull(incidenteRepository.save(entity));
    }

    // =========================================================
    //  Mapeamentos DTO ↔ Entidade
    // =========================================================

    private Incidente toEntity(IncidenteRequestDTO dto, List<Vulnerabilidade> vulnerabilidades) {
        return Incidente.builder()
                .titulo(dto.getTitulo())
                .descricao(dto.getDescricao())
                .status(dto.getStatus())
                .numeroChamado(dto.getNumeroChamado())
                .tarefaRelacionada(dto.getTarefaRelacionada())
                .tipo(dto.getTipo())
                .prioridade(dto.getPrioridade())
                .ipOrigem(dto.getIpOrigem())
                .ipDestino(dto.getIpDestino())
                .host(dto.getHost())
                .tempoEstimado(dto.getTempoEstimado())
                .cc(dto.getCc())
                .notas(dto.getNotas())
                .categoria(java.util.Optional.ofNullable(dto.getCategoriaId()).flatMap(categoriaRepository::findById).orElse(null))
                .unidade(java.util.Optional.ofNullable(dto.getUnidadeId()).flatMap(unidadeRepository::findById).orElse(null))
                .autor(java.util.Optional.ofNullable(dto.getAutorId()).flatMap(usuarioRepository::findById).orElse(null))
                .atribuido(java.util.Optional.ofNullable(dto.getAtribuidoId()).flatMap(usuarioRepository::findById).orElse(null))
                .vulnerabilidades(vulnerabilidades)
                .build();
    }

    private IncidenteResponseDTO toResponseDTO(Incidente entity) {
        return IncidenteResponseDTO.builder()
                .id(entity.getId())
                .titulo(entity.getTitulo())
                .descricao(entity.getDescricao())
                .status(entity.getStatus())
                .numeroChamado(entity.getNumeroChamado())
                .tarefaRelacionada(entity.getTarefaRelacionada())
                .tipo(entity.getTipo())
                .prioridade(entity.getPrioridade())
                .ipOrigem(entity.getIpOrigem())
                .ipDestino(entity.getIpDestino())
                .host(entity.getHost())
                .tempoEstimado(entity.getTempoEstimado())
                .cc(entity.getCc())
                .notas(entity.getNotas())
                .categoriaId(entity.getCategoria() != null ? entity.getCategoria().getId() : null)
                .categoriaNome(entity.getCategoria() != null ? entity.getCategoria().getNome() : null)
                .unidadeId(entity.getUnidade() != null ? entity.getUnidade().getId() : null)
                .unidadeSigla(entity.getUnidade() != null ? entity.getUnidade().getSigla() : null)
                .autorId(entity.getAutor() != null ? entity.getAutor().getId() : null)
                .autorNome(entity.getAutor() != null ? entity.getAutor().getNome() : null)
                .atribuidoId(entity.getAtribuido() != null ? entity.getAtribuido().getId() : null)
                .atribuidoNome(entity.getAtribuido() != null ? entity.getAtribuido().getNome() : null)
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