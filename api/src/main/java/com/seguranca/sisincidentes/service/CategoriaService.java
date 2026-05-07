package com.seguranca.sisincidentes.service;

import com.seguranca.sisincidentes.api.dto.CategoriaRequestDTO;
import com.seguranca.sisincidentes.api.dto.CategoriaResponseDTO;
import com.seguranca.sisincidentes.domain.model.TipoCategoria;
import org.springframework.lang.NonNull;

import java.util.List;

public interface CategoriaService {
    
    CategoriaResponseDTO create(@NonNull CategoriaRequestDTO requestDTO);
    
    CategoriaResponseDTO update(@NonNull Long id, @NonNull CategoriaRequestDTO requestDTO);
    
    CategoriaResponseDTO findById(@NonNull Long id);
    
    List<CategoriaResponseDTO> findAll();
    
    List<CategoriaResponseDTO> findByTipo(TipoCategoria tipo);
    
    void delete(@NonNull Long id);
}
