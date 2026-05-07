package com.seguranca.sisincidentes.api.dto;

import com.seguranca.sisincidentes.domain.model.FuncaoUsuario;
import com.seguranca.sisincidentes.domain.model.Perfil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de saída para respostas de consulta de Usuários.
 *
 * <p><strong>A senha nunca é incluída neste DTO</strong> — apenas o hash BCrypt
 * existe na entidade e não deve ser exposto nas respostas REST.</p>
 *
 * <p>Inclui dados denormalizados da {@code UnidadeAdministrativa} vinculada
 * ({@code unidadeId}, {@code unidadeTitulo}, {@code unidadeSigla}) para evitar
 * chamadas adicionais do cliente.</p>
 *
 * <p><b>RF02 — Gerenciamento de Usuários</b></p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UsuarioResponse", description = "Dados do Usuário retornados pela API (sem senha)")
public class UsuarioResponseDTO {

    @Schema(description = "Identificador único do usuário", example = "1")
    private Long id;

    @Schema(description = "E-mail institucional do usuário", example = "joao.silva@orgao.gov.br")
    private String email;

    @Schema(description = "Nome completo do usuário", example = "João da Silva")
    private String nome;

    @Schema(description = "Matrícula funcional (pode ser nula)", example = "12345678")
    private String matricula;

    @Schema(description = "Telefone de contato (pode ser nulo)", example = "(95) 3212-3456")
    private String telefone;

    @Schema(description = "Função/cargo do usuário", example = "ANALISTA_TI")
    private FuncaoUsuario funcao;

    @Schema(description = "Descrição legível da função", example = "Analista de TI")
    private String funcaoDescricao;

    @Schema(description = "Perfil de acesso no sistema", example = "OPERADOR")
    private Perfil perfil;

    @Schema(description = "Observações adicionais (pode ser nulo)")
    private String observacoes;

    @Schema(description = "Indica se o usuário está ativo no sistema", example = "true")
    private boolean ativo;

    // ---- Dados da Unidade Administrativa (denormalizados) ----

    @Schema(description = "ID da Unidade Administrativa vinculada", example = "1")
    private Long unidadeId;

    @Schema(description = "Título da Unidade Administrativa", example = "Superintendência de Tecnologia da Informação")
    private String unidadeTitulo;

    @Schema(description = "Sigla da Unidade Administrativa", example = "STI")
    private String unidadeSigla;

    // ---- Auditoria ----

    @Schema(description = "Data/hora de criação do registro")
    private LocalDateTime dataCriacao;

    @Schema(description = "Data/hora da última atualização")
    private LocalDateTime dataAtualizacao;
}
