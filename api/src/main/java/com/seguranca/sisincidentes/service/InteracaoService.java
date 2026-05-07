package com.seguranca.sisincidentes.service;

import com.seguranca.sisincidentes.api.dto.InteracaoRequestDTO;
import com.seguranca.sisincidentes.api.dto.InteracaoResponseDTO;

import java.util.List;

public interface InteracaoService {
    InteracaoResponseDTO create(InteracaoRequestDTO requestDTO);
    List<InteracaoResponseDTO> findByIncidenteId(Long incidenteId);
    void delete(Long id);
}