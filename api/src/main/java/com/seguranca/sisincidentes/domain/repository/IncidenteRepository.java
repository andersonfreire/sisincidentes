package com.seguranca.sisincidentes.domain.repository;

import com.seguranca.sisincidentes.domain.model.Incidente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório Spring Data JPA para {@link Incidente}.
 *
 * <p>Estende {@link JpaRepository} fornecendo operações CRUD padrão e
 * queries JPQL personalizadas para pesquisa e geração de estatísticas.</p>
 *
 * <p><b>RF06 — Gerenciamento de Vulnerabilidades e Incidentes</b></p>
 * <p><b>RF07 / RF09 — Relatórios e Estatísticas</b></p>
 */
@Repository
public interface IncidenteRepository extends JpaRepository<Incidente, Long> {

    /**
     * Lista todos os incidentes ordenados pela data de registro (mais recentes primeiro).
     *
     * @return lista ordenada de incidentes
     */
    @Query("SELECT DISTINCT i FROM Incidente i LEFT JOIN FETCH i.vulnerabilidades ORDER BY i.dataRegistro DESC")
    List<Incidente> findAllOrderByDataRegistroDesc();

    /**
     * Busca incidentes cujo título ou descrição contenha o termo informado.
     *
     * @param termo palavra-chave a pesquisar
     * @return lista de incidentes correspondentes
     */
    @Query("SELECT DISTINCT i FROM Incidente i LEFT JOIN FETCH i.vulnerabilidades WHERE LOWER(i.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(i.descricao) LIKE LOWER(CONCAT('%', :termo, '%')) ORDER BY i.dataRegistro DESC")
    List<Incidente> findByTermoContainingIgnoreCase(@Param("termo") String termo);

    /**
     * Filtra incidentes com base no status atual (ex: ABERTO, RESOLVIDO).
     *
     * @param status situação do incidente
     * @return lista de incidentes com o status informado
     */
    @Query("SELECT DISTINCT i FROM Incidente i LEFT JOIN FETCH i.vulnerabilidades WHERE i.status = :status ORDER BY i.dataRegistro DESC")
    List<Incidente> findByStatus(@Param("status") String status);

    /**
     * Conta o total de incidentes agrupados por status.
     * Utilizado para dashboards e estatísticas (RF09).
     * * @return Lista de objetos contendo [status, count]
     */
    @Query("SELECT i.status, COUNT(i) FROM Incidente i GROUP BY i.status")
    List<Object[]> countIncidentesByStatus();

    /**
     * Conta o total de incidentes vinculados a cada severidade de vulnerabilidade.
     * * @return Lista de objetos contendo [severidade, count]
     */
    @Query("SELECT v.severidade, COUNT(i) FROM Incidente i JOIN i.vulnerabilidades v GROUP BY v.severidade")
    List<Object[]> countByVulnerabilidadeSeveridade();
}