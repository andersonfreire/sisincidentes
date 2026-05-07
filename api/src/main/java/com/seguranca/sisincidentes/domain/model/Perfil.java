package com.seguranca.sisincidentes.domain.model;

/**
 * Enum que representa o perfil de acesso de um Usuário no sistema.
 *
 * <ul>
 *   <li>{@link #ADMIN} — acesso irrestrito a todos os módulos e configurações.</li>
 *   <li>{@link #OPERADOR} — acesso operacional: registra e consulta incidentes,
 *       mas não gerencia usuários nem configurações sistêmicas.</li>
 * </ul>
 *
 * <p>Utilizado diretamente na entidade {@link Usuario} e preparado para ser
 * consumido pelo mecanismo de autorização JWT em RF futuro.</p>
 */
public enum Perfil {

    /** Administrador do sistema — acesso total. */
    ADMIN,

    /** Gestor de TI — acesso administrativo e de coordenação. */
    GESTOR_TI,

    /** Operador — acesso funcional padrão. */
    OPERADOR
}
