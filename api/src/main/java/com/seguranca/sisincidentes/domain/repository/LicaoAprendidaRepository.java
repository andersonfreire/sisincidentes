package com.seguranca.sisincidentes.domain.repository;

import com.seguranca.sisincidentes.domain.model.LicaoAprendida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório Spring Data JPA para {@link LicaoAprendida}.
 *
 * <p>Estende {@link JpaRepository} fornecendo operações CRUD padrão.</p>
 *
 * <p><b>RF08 — Gerenciamento de Lições Aprendidas</b></p>
 */
@Repository
public interface LicaoAprendidaRepository extends JpaRepository<LicaoAprendida, Long> {

    /**
     * Busca a lição aprendida associada a um incidente específico.
     * Utiliza JOIN FETCH para carregar os dados do incidente atrelado.
     *
     * @param incidenteId identificador único do incidente
     * @return {@link Optional} com a lição aprendida documentada
     */
    @Query("SELECT l FROM LicaoAprendida l JOIN FETCH l.incidente i WHERE i.id = :incidenteId")
    Optional<LicaoAprendida> findByIncidenteId(@Param("incidenteId") Long incidenteId);
}