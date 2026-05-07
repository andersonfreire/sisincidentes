package com.seguranca.sisincidentes.domain.repository;

import com.seguranca.sisincidentes.domain.model.UnidadeAdministrativa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório Spring Data JPA para {@link UnidadeAdministrativa}.
 *
 * <p>Estende {@link JpaRepository} fornecendo operações CRUD padrão e
 * queries personalizadas para pesquisa por campos específicos.</p>
 */
@Repository
public interface UnidadeAdministrativaRepository extends JpaRepository<UnidadeAdministrativa, Long> {

    /**
     * Busca unidades cuja sigla contenha o termo informado (case-insensitive).
     *
     * @param sigla parte ou totalidade da sigla a pesquisar
     * @return lista de unidades correspondentes
     */
    List<UnidadeAdministrativa> findBySiglaContainingIgnoreCase(String sigla);

    /**
     * Busca unidades cujo título contenha o termo informado (case-insensitive).
     *
     * @param titulo parte ou totalidade do título a pesquisar
     * @return lista de unidades correspondentes
     */
    List<UnidadeAdministrativa> findByTituloContainingIgnoreCase(String titulo);

    /**
     * Verifica se já existe uma unidade com o código informado.
     *
     * @param codigo código organizacional a verificar
     * @return {@code true} se o código já está em uso
     */
    boolean existsByCodigo(String codigo);

    /**
     * Verifica se existe outra unidade com o mesmo código, excluindo o ID atual.
     * Utilizado para validação na operação de atualização.
     *
     * @param codigo código organizacional a verificar
     * @param id     ID da unidade que está sendo atualizada (excluído da busca)
     * @return {@code true} se outro registro já usa este código
     */
    @Query("SELECT COUNT(u) > 0 FROM UnidadeAdministrativa u WHERE u.codigo = :codigo AND u.id <> :id")
    boolean existsByCodigoAndIdNot(@Param("codigo") String codigo, @Param("id") Long id);

    /**
     * Lista todas as unidades ordenadas por título de forma crescente.
     *
     * @return lista ordenada de todas as unidades
     */
    @Query("SELECT u FROM UnidadeAdministrativa u ORDER BY u.titulo ASC")
    List<UnidadeAdministrativa> findAllOrderByTituloAsc();
}
