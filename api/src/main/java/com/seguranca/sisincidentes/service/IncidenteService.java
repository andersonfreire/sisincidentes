package com.seguranca.sisincidentes.service;

import com.seguranca.sisincidentes.api.dto.IncidenteRequestDTO;
import com.seguranca.sisincidentes.api.dto.IncidenteResponseDTO;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * Interface de serviço para o gerenciamento de Incidentes.
 *
 * <p>Define o contrato de negócio do RF06, desacoplando a API REST
 * da implementação concreta, facilitando testes e manutenção.</p>
 *
 * <p><b>RF06 — Gerenciamento de Vulnerabilidades e Incidentes</b></p>
 */
public interface IncidenteService {

    /**
     * Registra um novo Incidente no sistema.
     *
     * @param requestDTO dados do incidente a ser criado e IDs das vulnerabilidades vinculadas
     * @return DTO com os dados do incidente criado
     * @throws com.seguranca.sisincidentes.api.exception.ResourceNotFoundException
     * se algum ID de vulnerabilidade informado não existir
     */
    IncidenteResponseDTO create(@NonNull IncidenteRequestDTO requestDTO);

    /**
     * Atualiza os dados de um Incidente existente.
     *
     * @param id identificador do incidente a ser atualizado
     * @param requestDTO novos dados para atualização
     * @return DTO com os dados atualizados
     * @throws com.seguranca.sisincidentes.api.exception.ResourceNotFoundException
     * se o ID do incidente ou das vulnerabilidades não existir
     */
    IncidenteResponseDTO update(@NonNull Long id, @NonNull IncidenteRequestDTO requestDTO);

    /**
     * Lista todos os Incidentes cadastrados, ordenados por data de registro (descendente).
     *
     * @return lista de DTOs ordenada
     */
    List<IncidenteResponseDTO> findAll();

    /**
     * Busca um Incidente pelo seu identificador único.
     *
     * @param id identificador do incidente
     * @return DTO com os dados encontrados
     * @throws com.seguranca.sisincidentes.api.exception.ResourceNotFoundException
     * se o ID não existir
     */
    IncidenteResponseDTO findById(@NonNull Long id);

    /**
     * Exclui permanentemente um Incidente pelo seu identificador.
     *
     * @param id identificador do incidente a ser excluído
     * @throws com.seguranca.sisincidentes.api.exception.ResourceNotFoundException
     * se o ID não existir
     */
    void delete(@NonNull Long id);
}