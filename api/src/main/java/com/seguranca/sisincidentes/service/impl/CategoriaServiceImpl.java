package com.seguranca.sisincidentes.service.impl;

import com.seguranca.sisincidentes.api.dto.CategoriaRequestDTO;
import com.seguranca.sisincidentes.api.dto.CategoriaResponseDTO;
import com.seguranca.sisincidentes.api.exception.ForbiddenOperationException;
import com.seguranca.sisincidentes.api.exception.ResourceNotFoundException;
import com.seguranca.sisincidentes.domain.model.Categoria;
import com.seguranca.sisincidentes.domain.model.Perfil;
import com.seguranca.sisincidentes.domain.model.TipoCategoria;
import com.seguranca.sisincidentes.domain.repository.CategoriaRepository;
import com.seguranca.sisincidentes.security.UserDetailsImpl;
import com.seguranca.sisincidentes.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository repository;

    @Override
    @Transactional
    public CategoriaResponseDTO create(@NonNull CategoriaRequestDTO requestDTO) {
        Objects.requireNonNull(requestDTO, "O DTO de requisição não pode ser nulo");
        log.info("Criando nova categoria: {}", requestDTO.getNome());

        if (repository.existsByNomeIgnoreCase(requestDTO.getNome())) {
            throw new IllegalArgumentException("Já existe uma categoria com o nome: " + requestDTO.getNome());
        }

        Categoria entity = toEntity(requestDTO);
        Categoria saved = saveAndValidate(entity);

        return toResponseDTO(saved);
    }

    @Override
    @Transactional
    public CategoriaResponseDTO update(@NonNull Long id, @NonNull CategoriaRequestDTO requestDTO) {
        Objects.requireNonNull(id, "O ID não pode ser nulo");
        Objects.requireNonNull(requestDTO, "O DTO de requisição não pode ser nulo");
        log.info("Atualizando categoria ID: {}", id);

        Categoria existing = findCategoriaOrThrow(id);

        if (repository.existsByNomeIgnoreCaseAndIdNot(requestDTO.getNome(), id)) {
            throw new IllegalArgumentException("Já existe outra categoria com o nome: " + requestDTO.getNome());
        }

        existing.setNome(requestDTO.getNome());
        existing.setDescricao(requestDTO.getDescricao());
        existing.setTipo(requestDTO.getTipo());

        Categoria updated = saveAndValidate(existing);
        return toResponseDTO(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponseDTO findById(@NonNull Long id) {
        Objects.requireNonNull(id, "O ID não pode ser nulo");
        return toResponseDTO(findCategoriaOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> findByTipo(TipoCategoria tipo) {
        return repository.findByTipo(tipo).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(@NonNull Long id) {
        Objects.requireNonNull(id, "O ID não pode ser nulo");
        
        // RF04 — Bloqueio de exclusão para OPERADOR
        if (isOperador()) {
            throw new ForbiddenOperationException("O perfil OPERADOR não tem permissão para excluir categorias.");
        }

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria", "id", id);
        }
        repository.deleteById(id);
    }

    // =========================================================
    //  Helpers
    // =========================================================

    private Categoria findCategoriaOrThrow(@NonNull Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", id));
    }

    private boolean isOperador() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsImpl) {
            return ((UserDetailsImpl) principal).getUsuario().getPerfil() == Perfil.OPERADOR;
        }
        return false;
    }

    private Categoria toEntity(CategoriaRequestDTO dto) {
        return Categoria.builder()
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .tipo(dto.getTipo())
                .build();
    }

    private CategoriaResponseDTO toResponseDTO(Categoria entity) {
        return CategoriaResponseDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .descricao(entity.getDescricao())
                .tipo(entity.getTipo())
                .build();
    }

    @NonNull
    @SuppressWarnings("null")
    private Categoria saveAndValidate(Categoria entity) {
        return Objects.requireNonNull(repository.save(entity));
    }
}
