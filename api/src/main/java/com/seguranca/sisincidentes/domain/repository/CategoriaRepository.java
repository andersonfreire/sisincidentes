package com.seguranca.sisincidentes.domain.repository;

import com.seguranca.sisincidentes.domain.model.Categoria;
import com.seguranca.sisincidentes.domain.model.TipoCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    Optional<Categoria> findByNomeIgnoreCase(String nome);
    
    boolean existsByNomeIgnoreCase(String nome);
    
    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);
    
    List<Categoria> findByTipo(TipoCategoria tipo);
}
