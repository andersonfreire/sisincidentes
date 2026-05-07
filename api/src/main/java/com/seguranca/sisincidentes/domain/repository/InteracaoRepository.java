package com.seguranca.sisincidentes.domain.repository;

import com.seguranca.sisincidentes.domain.model.Interacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InteracaoRepository extends JpaRepository<Interacao, Long> {

    /**
     * Busca o histórico de interações de um incidente específico,
     * ordenado da mais recente para a mais antiga.
     */
    @Query("SELECT i FROM Interacao i JOIN FETCH i.incidente WHERE i.incidente.id = :incidenteId ORDER BY i.dataCriacao DESC")
    List<Interacao> findByIncidenteIdOrderByDataCriacaoDesc(@Param("incidenteId") Long incidenteId);
}