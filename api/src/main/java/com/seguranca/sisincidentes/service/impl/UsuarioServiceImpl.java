package com.seguranca.sisincidentes.service.impl;

import com.seguranca.sisincidentes.api.dto.UsuarioRequestDTO;
import com.seguranca.sisincidentes.api.dto.UsuarioResponseDTO;
import com.seguranca.sisincidentes.api.exception.ResourceNotFoundException;
import com.seguranca.sisincidentes.domain.model.UnidadeAdministrativa;
import com.seguranca.sisincidentes.domain.model.Usuario;
import com.seguranca.sisincidentes.domain.model.Perfil;
import com.seguranca.sisincidentes.domain.repository.UnidadeAdministrativaRepository;
import com.seguranca.sisincidentes.domain.repository.UsuarioRepository;
import com.seguranca.sisincidentes.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação concreta do serviço de Usuários.
 *
 * <p>Concentra toda a lógica de negócio do RF02:</p>
 * <ul>
 *   <li>Hash de senha via {@link PasswordEncoder} (BCrypt)</li>
 *   <li>Validação de unicidade de e-mail</li>
 *   <li>Resolução do relacionamento com {@link UnidadeAdministrativa}</li>
 *   <li>Toggle de status ativo/inativo</li>
 *   <li>Mapeamento DTO ↔ Entidade</li>
 * </ul>
 *
 * <p><b>RF02 — Gerenciamento de Usuários</b></p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private static final String RESOURCE_NAME = "Usuário";

    private final UsuarioRepository usuarioRepository;
    private final UnidadeAdministrativaRepository unidadeRepository;
    private final PasswordEncoder passwordEncoder;

    // =========================================================
    //  Criar
    // =========================================================

    @Override
    @Transactional
    public UsuarioResponseDTO create(UsuarioRequestDTO requestDTO) {
        log.info("Criando usuário com e-mail: {}", requestDTO.getEmail());

        // Valida unicidade do e-mail
        if (usuarioRepository.existsByEmailIgnoreCase(requestDTO.getEmail())) {
            throw new IllegalArgumentException(
                String.format("Já existe um usuário cadastrado com o e-mail '%s'.", requestDTO.getEmail())
            );
        }

        // Valida existência da Unidade Administrativa
        UnidadeAdministrativa unidade = findUnidadeOrThrow(requestDTO.getUnidadeId());

        // Hash da senha antes de persistir
        String senhaHash = passwordEncoder.encode(requestDTO.getSenha());

        Usuario entity = toEntity(requestDTO, senhaHash, unidade);
        Usuario saved = usuarioRepository.save(entity);

        log.info("Usuário criado com sucesso. ID: {}", saved.getId());
        return toResponseDTO(saved);
    }

    // =========================================================
    //  Atualizar
    // =========================================================

    @Override
    @Transactional
    public UsuarioResponseDTO update(Long id, UsuarioRequestDTO requestDTO) {
        log.info("Atualizando usuário ID: {}", id);

        Usuario existing = findUsuarioOrThrow(id);

        // Valida unicidade do e-mail, excluindo o próprio registro
        if (usuarioRepository.existsByEmailIgnoreCaseAndIdNot(requestDTO.getEmail(), id)) {
            throw new IllegalArgumentException(
                String.format("Já existe outro usuário cadastrado com o e-mail '%s'.", requestDTO.getEmail())
            );
        }

        // Valida existência da nova Unidade Administrativa
        UnidadeAdministrativa unidade = findUnidadeOrThrow(requestDTO.getUnidadeId());

        // Atualiza os campos editáveis
        existing.setEmail(requestDTO.getEmail());
        existing.setNome(requestDTO.getNome());
        existing.setMatricula(requestDTO.getMatricula());
        existing.setTelefone(requestDTO.getTelefone());
        existing.setFuncao(requestDTO.getFuncao());
        existing.setPerfil(requestDTO.getPerfil() != null ? requestDTO.getPerfil() : Perfil.OPERADOR);
        existing.setObservacoes(requestDTO.getObservacoes());
        existing.setUnidade(unidade);

        // Atualiza senha somente se uma nova foi informada
        if (StringUtils.hasText(requestDTO.getSenha())) {
            log.debug("Atualizando senha do usuário ID: {}", id);
            existing.setSenha(passwordEncoder.encode(requestDTO.getSenha()));
        }

        Usuario updated = usuarioRepository.save(existing);

        log.info("Usuário ID {} atualizado com sucesso.", id);
        return toResponseDTO(updated);
    }

    // =========================================================
    //  Consultas
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> findAll() {
        log.debug("Listando todos os usuários.");
        return usuarioRepository.findAllOrderByNomeAsc()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO findById(Long id) {
        log.debug("Buscando usuário por ID: {}", id);
        return toResponseDTO(findUsuarioOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> findByNome(String nome) {
        log.debug("Buscando usuários por nome: {}", nome);
        return usuarioRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> findByUnidade(Long unidadeId) {
        log.debug("Buscando usuários da unidade ID: {}", unidadeId);
        return usuarioRepository.findByUnidadeId(unidadeId)
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
        log.info("Excluindo usuário ID: {}", id);

        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException(RESOURCE_NAME, "id", id);
        }

        usuarioRepository.deleteById(id);
        log.info("Usuário ID {} excluído com sucesso.", id);
    }

    // =========================================================
    //  Toggle Ativo
    // =========================================================

    @Override
    @Transactional
    public UsuarioResponseDTO toggleAtivo(Long id) {
        log.info("Alternando status ativo/inativo do usuário ID: {}", id);

        Usuario usuario = findUsuarioOrThrow(id);
        usuario.setAtivo(!usuario.isAtivo());
        Usuario updated = usuarioRepository.save(usuario);

        log.info("Usuário ID {} agora está {}.", id, updated.isAtivo() ? "ATIVO" : "INATIVO");
        return toResponseDTO(updated);
    }

    // =========================================================
    //  Helpers privados
    // =========================================================

    private Usuario findUsuarioOrThrow(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "id", id));
    }

    private UnidadeAdministrativa findUnidadeOrThrow(Long unidadeId) {
        return unidadeRepository.findById(unidadeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Unidade Administrativa", "id", unidadeId
                ));
    }

    // =========================================================
    //  Mapeamentos DTO ↔ Entidade
    // =========================================================

    /**
     * Converte um {@link UsuarioRequestDTO} em entidade JPA {@link Usuario}.
     *
     * @param dto      dados de entrada
     * @param senhaHash senha já hasheada com BCrypt
     * @param unidade  entidade Unidade Administrativa resolvida
     * @return entidade pronta para persistência
     */
    private Usuario toEntity(UsuarioRequestDTO dto, String senhaHash, UnidadeAdministrativa unidade) {
        return Usuario.builder()
                .email(dto.getEmail())
                .senha(senhaHash)
                .nome(dto.getNome())
                .matricula(dto.getMatricula())
                .telefone(dto.getTelefone())
                .funcao(dto.getFuncao())
                .perfil(dto.getPerfil() != null ? dto.getPerfil() : Perfil.OPERADOR)
                .observacoes(dto.getObservacoes())
                .unidade(unidade)
                .build();
    }

    /**
     * Converte uma entidade {@link Usuario} em {@link UsuarioResponseDTO}.
     * A senha <strong>nunca</strong> é incluída no DTO de resposta.
     *
     * @param entity entidade gerenciada pelo JPA
     * @return DTO de resposta sem senha
     */
    private UsuarioResponseDTO toResponseDTO(Usuario entity) {
        return UsuarioResponseDTO.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .nome(entity.getNome())
                .matricula(entity.getMatricula())
                .telefone(entity.getTelefone())
                .funcao(entity.getFuncao())
                .funcaoDescricao(entity.getFuncao() != null ? entity.getFuncao().getDescricao() : null)
                .perfil(entity.getPerfil())
                .observacoes(entity.getObservacoes())
                .ativo(entity.isAtivo())
                .unidadeId(entity.getUnidade().getId())
                .unidadeTitulo(entity.getUnidade().getTitulo())
                .unidadeSigla(entity.getUnidade().getSigla())
                .dataCriacao(entity.getDataCriacao())
                .dataAtualizacao(entity.getDataAtualizacao())
                .build();
    }
}
