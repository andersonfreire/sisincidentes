package com.seguranca.sisincidentes.service;

import com.seguranca.sisincidentes.api.dto.LicaoAprendidaRequestDTO;
import com.seguranca.sisincidentes.api.dto.LicaoAprendidaResponseDTO;

import java.util.List;

/**
 * Interface de serviço para o gerenciamento de Lições Aprendidas.
 *
 * <p>Define o contrato de negócio para o RF08, assegurando o registro 
 * de resoluções técnicas e o desacoplamento da camada de exposição.</p>
 *
 * <p><b>RF08 — Gerenciamento de Lições Aprendidas</b></p>
 */
public interface LicaoAprendidaService {

    /**
     * Registra uma nova Lição Aprendida vinculada a um Incidente.
     *
     * @param requestDTO dados da resolução e ID do incidente correspondente.
     * @return DTO com os dados da lição aprendida persistida.
     * @throws com.seguranca.sisincidentes.api.exception.ResourceNotFoundException 
     * se o incidente informado não existir.
     * @throws IllegalArgumentException se o incidente já possuir uma lição registrada (violação 1:1).
     */
    LicaoAprendidaResponseDTO create(LicaoAprendidaRequestDTO requestDTO);

    /**
     * Recupera a Lição Aprendida associada a um incidente específico.
     *
     * @param incidenteId identificador do incidente.
     * @return DTO com os dados da resolução encontrada.
     * @throws com.seguranca.sisincidentes.api.exception.ResourceNotFoundException 
     * se não houver lição para o ID informado.
     */
    LicaoAprendidaResponseDTO findByIncidenteId(Long incidenteId);

    /**
     * Lista todas as Lições Aprendidas registradas no sistema.
     *
     * @return lista de DTOs representativos.
     */
    List<LicaoAprendidaResponseDTO> findAll();

    /**
     * Atualiza uma Lição Aprendida existente.
     *
     * @param id identificador da lição aprendida.
     * @param requestDTO novos dados da resolução.
     * @return DTO com os dados atualizados.
     */
    LicaoAprendidaResponseDTO update(Long id, LicaoAprendidaRequestDTO requestDTO);

    /**
     * Remove permanentemente um registro de Lição Aprendida.
     *
     * @param id identificador da lição aprendida.
     * @throws com.seguranca.sisincidentes.api.exception.ResourceNotFoundException 
     * se o registro não for localizado.
     */
    void delete(Long id);
}