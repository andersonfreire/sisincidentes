package com.seguranca.sisincidentes.service;

import com.seguranca.sisincidentes.api.dto.VulnerabilidadeRequestDTO;
import com.seguranca.sisincidentes.api.dto.VulnerabilidadeResponseDTO;
import java.util.List;

public interface VulnerabilidadeService {
    VulnerabilidadeResponseDTO create(VulnerabilidadeRequestDTO dto);
    VulnerabilidadeResponseDTO update(Long id, VulnerabilidadeRequestDTO dto);
    VulnerabilidadeResponseDTO findById(Long id);
    List<VulnerabilidadeResponseDTO> findAll();
    void delete(Long id);
}
