package com.seguranca.sisincidentes.service.impl;

import com.seguranca.sisincidentes.api.dto.UnidadeAdministrativaRequestDTO;
import com.seguranca.sisincidentes.api.dto.UnidadeAdministrativaResponseDTO;
import com.seguranca.sisincidentes.api.exception.ResourceNotFoundException;
import com.seguranca.sisincidentes.domain.model.UnidadeAdministrativa;
import com.seguranca.sisincidentes.domain.repository.UnidadeAdministrativaRepository;
import com.seguranca.sisincidentes.service.UnidadeAdministrativaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação concreta do serviço de Unidades Administrativas.
 *
 * <p>Concentra toda a lógica de negócio do RF01, incluindo validações
 * de unicidade de código, mapeamento DTO ↔ Entidade e delegação
 * ao repositório.</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnidadeAdministrativaServiceImpl implements UnidadeAdministrativaService {

    private static final String RESOURCE_NAME = "Unidade Administrativa";

    private final UnidadeAdministrativaRepository repository;

    // =========================================================
    //  Criar
    // =========================================================

    @Override
    @Transactional
    public UnidadeAdministrativaResponseDTO create(UnidadeAdministrativaRequestDTO requestDTO) {
        log.info("Criando unidade administrativa com código: {}", requestDTO.getCodigo());

        // Validação de unicidade do código
        if (repository.existsByCodigo(requestDTO.getCodigo())) {
            throw new IllegalArgumentException(
                String.format("Já existe uma Unidade Administrativa com o código '%s'.", requestDTO.getCodigo())
            );
        }

        UnidadeAdministrativa entity = toEntity(requestDTO);
        UnidadeAdministrativa saved = repository.save(entity);

        log.info("Unidade administrativa criada com sucesso. ID: {}", saved.getId());
        return toResponseDTO(saved);
    }

    // =========================================================
    //  Atualizar
    // =========================================================

    @Override
    @Transactional
    public UnidadeAdministrativaResponseDTO update(Long id, UnidadeAdministrativaRequestDTO requestDTO) {
        log.info("Atualizando unidade administrativa ID: {}", id);

        UnidadeAdministrativa existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));

        // Valida unicidade do código, excluindo o próprio registro
        if (repository.existsByCodigoAndIdNot(requestDTO.getCodigo(), id)) {
            throw new IllegalArgumentException(
                String.format("Já existe outra Unidade Administrativa com o código '%s'.", requestDTO.getCodigo())
            );
        }

        // Atualiza apenas os campos editáveis
        existing.setCodigo(requestDTO.getCodigo());
        existing.setSigla(requestDTO.getSigla());
        existing.setTitulo(requestDTO.getTitulo());
        existing.setResponsavel(requestDTO.getResponsavel());
        existing.setContato(requestDTO.getContato());

        UnidadeAdministrativa updated = repository.save(existing);

        log.info("Unidade administrativa ID {} atualizada com sucesso.", id);
        return toResponseDTO(updated);
    }

    // =========================================================
    //  Consultas
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<UnidadeAdministrativaResponseDTO> findAll() {
        log.debug("Listando todas as unidades administrativas.");
        return repository.findAllOrderByTituloAsc()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UnidadeAdministrativaResponseDTO findById(Long id) {
        log.debug("Buscando unidade administrativa por ID: {}", id);
        UnidadeAdministrativa entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
        return toResponseDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnidadeAdministrativaResponseDTO> findBySigla(String sigla) {
        log.debug("Buscando unidades por sigla: {}", sigla);
        return repository.findBySiglaContainingIgnoreCase(sigla)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnidadeAdministrativaResponseDTO> findByTitulo(String titulo) {
        log.debug("Buscando unidades por título: {}", titulo);
        return repository.findByTituloContainingIgnoreCase(titulo)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // =========================================================
    //  Excluir
    // =========================================================

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Excluindo unidade administrativa ID: {}", id);

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(RESOURCE_NAME, "id", id);
        }

        repository.deleteById(id);
        log.info("Unidade administrativa ID {} excluída com sucesso.", id);
    }

    // =========================================================
    //  Mapeamentos DTO ↔ Entidade
    // =========================================================

    /**
     * Converte um {@link UnidadeAdministrativaRequestDTO} em entidade JPA.
     */
    private UnidadeAdministrativa toEntity(UnidadeAdministrativaRequestDTO dto) {
        return UnidadeAdministrativa.builder()
                .codigo(dto.getCodigo())
                .sigla(dto.getSigla())
                .titulo(dto.getTitulo())
                .responsavel(dto.getResponsavel())
                .contato(dto.getContato())
                .build();
    }

    /**
     * Converte uma entidade {@link UnidadeAdministrativa} em DTO de resposta.
     */
    private UnidadeAdministrativaResponseDTO toResponseDTO(UnidadeAdministrativa entity) {
        return UnidadeAdministrativaResponseDTO.builder()
                .id(entity.getId())
                .codigo(entity.getCodigo())
                .sigla(entity.getSigla())
                .titulo(entity.getTitulo())
                .responsavel(entity.getResponsavel())
                .contato(entity.getContato())
                .dataCriacao(entity.getDataCriacao())
                .dataAtualizacao(entity.getDataAtualizacao())
                .build();
    }
}
