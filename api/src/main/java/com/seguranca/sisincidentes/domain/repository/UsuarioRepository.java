package com.seguranca.sisincidentes.domain.repository;

import com.seguranca.sisincidentes.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório Spring Data JPA para {@link Usuario}.
 *
 * <p>Estende {@link JpaRepository} fornecendo operações CRUD padrão e
 * queries JPQL personalizadas para pesquisa por campos específicos.</p>
 *
 * <p><b>RF02 — Gerenciamento de Usuários</b></p>
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca um usuário pelo e-mail (case-insensitive).
     * Utilizado para autenticação e validação de unicidade.
     *
     * @param email endereço de e-mail a pesquisar
     * @return {@link Optional} com o usuário, se encontrado
     */
    Optional<Usuario> findByEmailIgnoreCase(String email);

    /**
     * Verifica se já existe um usuário com o e-mail informado.
     *
     * @param email endereço de e-mail a verificar
     * @return {@code true} se o e-mail já está em uso
     */
    boolean existsByEmailIgnoreCase(String email);

    /**
     * Verifica se existe outro usuário com o mesmo e-mail, excluindo o ID atual.
     * Utilizado para validação na operação de atualização.
     *
     * @param email endereço de e-mail a verificar
     * @param id    ID do usuário que está sendo atualizado (excluído da busca)
     * @return {@code true} se outro registro já usa este e-mail
     */
    @Query("SELECT COUNT(u) > 0 FROM Usuario u WHERE LOWER(u.email) = LOWER(:email) AND u.id <> :id")
    boolean existsByEmailIgnoreCaseAndIdNot(@Param("email") String email, @Param("id") Long id);

    /**
     * Lista todos os usuários ordenados pelo nome de forma crescente.
     * Query personalizada JPQL para garantir ordenação consistente.
     *
     * @return lista ordenada de todos os usuários
     */
    @Query("SELECT u FROM Usuario u JOIN FETCH u.unidade ORDER BY u.nome ASC")
    List<Usuario> findAllOrderByNomeAsc();

    /**
     * Busca usuários cujo nome contenha o termo informado (case-insensitive).
     * Suporta pesquisa parcial.
     *
     * @param nome parte ou totalidade do nome a pesquisar
     * @return lista de usuários correspondentes
     */
    @Query("SELECT u FROM Usuario u JOIN FETCH u.unidade WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%')) ORDER BY u.nome ASC")
    List<Usuario> findByNomeContainingIgnoreCase(@Param("nome") String nome);

    /**
     * Filtra usuários por Unidade Administrativa.
     *
     * @param unidadeId ID da unidade administrativa
     * @return lista de usuários pertencentes à unidade
     */
    @Query("SELECT u FROM Usuario u JOIN FETCH u.unidade WHERE u.unidade.id = :unidadeId ORDER BY u.nome ASC")
    List<Usuario> findByUnidadeId(@Param("unidadeId") Long unidadeId);

    /**
     * Filtra usuários pelo status ativo/inativo.
     *
     * @param ativo {@code true} para usuários ativos, {@code false} para inativos
     * @return lista de usuários com o status informado
     */
    @Query("SELECT u FROM Usuario u JOIN FETCH u.unidade WHERE u.ativo = :ativo ORDER BY u.nome ASC")
    List<Usuario> findByAtivo(@Param("ativo") boolean ativo);
}
