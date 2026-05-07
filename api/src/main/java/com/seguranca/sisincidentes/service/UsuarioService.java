package com.seguranca.sisincidentes.service;

import com.seguranca.sisincidentes.api.dto.UsuarioRequestDTO;
import com.seguranca.sisincidentes.api.dto.UsuarioResponseDTO;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * Interface de serviço para o gerenciamento de Usuários.
 *
 * <p>Define o contrato de negócio do RF02, desacoplando a API REST
 * da implementação concreta, facilitando testes e manutenção.</p>
 *
 * <p><b>RF02 — Gerenciamento de Usuários</b></p>
 */
public interface UsuarioService {

    /**
     * Cadastra um novo Usuário no sistema.
     *
     * <p>A senha informada é hasheada com BCrypt antes de ser persistida.</p>
     *
     * @param requestDTO dados do usuário a ser criado (incluindo senha em texto plano)
     * @return DTO com os dados do usuário criado (sem senha)
     * @throws IllegalArgumentException se o e-mail já estiver em uso
     * @throws com.seguranca.sisincidentes.api.exception.ResourceNotFoundException
     *         se a {@code unidadeId} não existir
     */
    UsuarioResponseDTO create(@NonNull UsuarioRequestDTO requestDTO);

    /**
     * Atualiza os dados de um Usuário existente.
     *
     * <p>Se a senha não for informada no DTO (nula ou vazia), a senha
     * atual do usuário é mantida. Caso contrário, a nova senha é hasheada.</p>
     *
     * @param id         identificador do usuário a ser atualizado
     * @param requestDTO novos dados do usuário
     * @return DTO com os dados atualizados (sem senha)
     * @throws com.seguranca.sisincidentes.api.exception.ResourceNotFoundException
     *         se o ID não existir
     * @throws IllegalArgumentException se o e-mail já estiver em uso por outro usuário
     */
    UsuarioResponseDTO update(@NonNull Long id, @NonNull UsuarioRequestDTO requestDTO);

    /**
     * Lista todos os Usuários cadastrados, ordenados por nome.
     *
     * @return lista de DTOs (pode ser vazia)
     */
    List<UsuarioResponseDTO> findAll();

    /**
     * Busca um Usuário pelo seu identificador único.
     *
     * @param id identificador do usuário
     * @return DTO com os dados do usuário encontrado
     * @throws com.seguranca.sisincidentes.api.exception.ResourceNotFoundException
     *         se o ID não existir
     */
    UsuarioResponseDTO findById(@NonNull Long id);

    /**
     * Busca Usuários cujo nome contenha o termo informado (parcial, case-insensitive).
     *
     * @param nome termo de busca
     * @return lista de DTOs correspondentes
     */
    List<UsuarioResponseDTO> findByNome(String nome);

    /**
     * Filtra Usuários por Unidade Administrativa.
     *
     * @param unidadeId ID da unidade administrativa
     * @return lista de usuários pertencentes à unidade
     */
    List<UsuarioResponseDTO> findByUnidade(@NonNull Long unidadeId);

    /**
     * Exclui permanentemente um Usuário pelo seu identificador.
     *
     * @param id identificador do usuário a ser excluído
     * @throws com.seguranca.sisincidentes.api.exception.ResourceNotFoundException
     *         se o ID não existir
     */
    void delete(@NonNull Long id);

    /**
     * Alterna o status ativo/inativo de um Usuário.
     *
     * <p>Usuários desativados mantêm seus dados no sistema mas não
     * conseguem autenticar (futuro mecanismo JWT).</p>
     *
     * @param id identificador do usuário
     * @return DTO atualizado com o novo status
     * @throws com.seguranca.sisincidentes.api.exception.ResourceNotFoundException
     *         se o ID não existir
     */
    UsuarioResponseDTO toggleAtivo(@NonNull Long id);
}
