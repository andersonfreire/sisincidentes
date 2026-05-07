package com.seguranca.sisincidentes.service;

import com.seguranca.sisincidentes.api.dto.UnidadeAdministrativaRequestDTO;
import com.seguranca.sisincidentes.api.dto.UnidadeAdministrativaResponseDTO;

import java.util.List;

/**
 * Interface de serviço para o gerenciamento de Unidades Administrativas.
 *
 * <p>Define o contrato de negócio do RF01, desacoplando a API REST
 * da implementação concreta, facilitando testes e manutenção.</p>
 */
public interface UnidadeAdministrativaService {

    /**
     * Cadastra uma nova Unidade Administrativa.
     *
     * @param requestDTO dados da unidade a ser criada
     * @return DTO com os dados da unidade criada (incluindo ID gerado)
     * @throws IllegalArgumentException se o código já estiver em uso
     */
    UnidadeAdministrativaResponseDTO create(UnidadeAdministrativaRequestDTO requestDTO);

    /**
     * Atualiza uma Unidade Administrativa existente.
     *
     * @param id         identificador da unidade a ser atualizada
     * @param requestDTO novos dados da unidade
     * @return DTO com os dados atualizados
     * @throws com.seguranca.sisincidentes.api.exception.ResourceNotFoundException se o ID não existir
     * @throws IllegalArgumentException se o código já estiver em uso por outra unidade
     */
    UnidadeAdministrativaResponseDTO update(Long id, UnidadeAdministrativaRequestDTO requestDTO);

    /**
     * Lista todas as Unidades Administrativas cadastradas, ordenadas por título.
     *
     * @return lista de DTOs (pode ser vazia)
     */
    List<UnidadeAdministrativaResponseDTO> findAll();

    /**
     * Busca uma Unidade Administrativa pelo seu identificador único.
     *
     * @param id identificador da unidade
     * @return DTO com os dados da unidade encontrada
     * @throws com.seguranca.sisincidentes.api.exception.ResourceNotFoundException se o ID não existir
     */
    UnidadeAdministrativaResponseDTO findById(Long id);

    /**
     * Exclui uma Unidade Administrativa pelo seu identificador único.
     *
     * @param id identificador da unidade a ser excluída
     * @throws com.seguranca.sisincidentes.api.exception.ResourceNotFoundException se o ID não existir
     */
    void delete(Long id);

    /**
     * Busca Unidades Administrativas cuja sigla contenha o termo informado.
     *
     * @param sigla termo de busca (parcial, case-insensitive)
     * @return lista de DTOs correspondentes
     */
    List<UnidadeAdministrativaResponseDTO> findBySigla(String sigla);

    /**
     * Busca Unidades Administrativas cujo título contenha o termo informado.
     *
     * @param titulo termo de busca (parcial, case-insensitive)
     * @return lista de DTOs correspondentes
     */
    List<UnidadeAdministrativaResponseDTO> findByTitulo(String titulo);
}
