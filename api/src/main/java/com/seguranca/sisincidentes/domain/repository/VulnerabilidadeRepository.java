package com.seguranca.sisincidentes.domain.repository;

import com.seguranca.sisincidentes.domain.model.Vulnerabilidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório Spring Data JPA para {@link Vulnerabilidade}.
 *
 * <p>Estende {@link JpaRepository} fornecendo operações CRUD padrão e
 * queries JPQL personalizadas para pesquisa por níveis de severidade.</p>
 *
 * <p><b>RF06 — Gerenciamento de Vulnerabilidades e Incidentes</b></p>
 */
@Repository
public interface VulnerabilidadeRepository extends JpaRepository<Vulnerabilidade, Long> {

    /**
     * Busca uma vulnerabilidade pelo título exato.
     *
     * @param titulo título da vulnerabilidade
     * @return {@link Optional} com a vulnerabilidade, se encontrada
     */
    Optional<Vulnerabilidade> findByTituloIgnoreCase(String titulo);

    /**
     * Filtra vulnerabilidades por nível de severidade.
     *
     * @param severidade nível de risco (ex: CRITICA, ALTA)
     * @return lista de vulnerabilidades correspondentes
     */
    @Query("SELECT v FROM Vulnerabilidade v WHERE v.severidade = :severidade ORDER BY v.titulo ASC")
    List<Vulnerabilidade> findBySeveridade(@Param("severidade") String severidade);
}