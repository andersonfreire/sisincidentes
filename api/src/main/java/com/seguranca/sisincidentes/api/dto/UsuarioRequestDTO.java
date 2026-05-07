package com.seguranca.sisincidentes.api.dto;

import com.seguranca.sisincidentes.domain.model.FuncaoUsuario;
import com.seguranca.sisincidentes.domain.model.Perfil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para criação e atualização de Usuários.
 *
 * <p>Contém todas as validações Bean Validation (Hibernate Validator)
 * aplicadas antes de chegar à camada de serviço.</p>
 *
 * <p>A senha é obrigatória somente na criação. Na atualização,
 * se omitida ({@code null} ou vazia), a senha atual é mantida.</p>
 *
 * <p><b>RF02 — Gerenciamento de Usuários</b></p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UsuarioRequest", description = "Dados para cadastro ou atualização de um Usuário")
public class UsuarioRequestDTO {

    @NotBlank(message = "O campo 'e-mail' é obrigatório.")
    @Email(message = "O campo 'e-mail' deve conter um endereço de e-mail válido.")
    @Size(max = 150, message = "O campo 'e-mail' deve ter no máximo 150 caracteres.")
    @Schema(
        description = "E-mail institucional do usuário — único no sistema",
        example = "joao.silva@orgao.gov.br",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;

    /**
     * Senha do usuário. Obrigatória na criação (mínimo 6 caracteres).
     * Na atualização, pode ser omitida para manter a senha atual.
     */
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
    @Schema(
        description = "Senha de acesso (mín. 6 caracteres). Omita na atualização para manter a senha atual.",
        example = "SenhaSegura@123"
    )
    private String senha;

    @NotBlank(message = "O campo 'nome' é obrigatório.")
    @Size(min = 3, max = 150, message = "O campo 'nome' deve ter entre 3 e 150 caracteres.")
    @Schema(
        description = "Nome completo do usuário",
        example = "João da Silva",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nome;

    @Size(max = 30, message = "O campo 'matrícula' deve ter no máximo 30 caracteres.")
    @Schema(description = "Matrícula funcional do servidor (opcional)", example = "12345678")
    private String matricula;

    @Size(max = 30, message = "O campo 'telefone' deve ter no máximo 30 caracteres.")
    @Schema(description = "Telefone de contato (opcional)", example = "(95) 3212-3456")
    private String telefone;

    @NotNull(message = "O campo 'função' é obrigatório.")
    @Schema(
        description = "Função/cargo do usuário na organização",
        example = "ANALISTA_TI",
        requiredMode = Schema.RequiredMode.REQUIRED,
        allowableValues = {"ANALISTA_TI", "COORDENADOR_SI", "GESTOR_TI", "SUPERINTENDENTE_TI", "TECNICO_TI"}
    )
    private FuncaoUsuario funcao;

    @NotNull(message = "O campo 'perfil' é obrigatório.")
    @Schema(
        description = "Perfil de acesso no sistema",
        example = "OPERADOR",
        requiredMode = Schema.RequiredMode.REQUIRED,
        allowableValues = {"ADMIN", "OPERADOR"}
    )
    @Builder.Default
    private Perfil perfil = Perfil.OPERADOR;

    @Size(max = 500, message = "O campo 'observações' deve ter no máximo 500 caracteres.")
    @Schema(description = "Observações adicionais sobre o usuário (opcional)", example = "Responsável pelo turno noturno.")
    private String observacoes;

    @NotNull(message = "O campo 'unidadeId' é obrigatório.")
    @Positive(message = "O campo 'unidadeId' deve ser um número positivo.")
    @Schema(
        description = "ID da Unidade Administrativa à qual o usuário pertence",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long unidadeId;
}
