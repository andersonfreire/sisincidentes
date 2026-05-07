package com.seguranca.sisincidentes.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitação de login.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Objeto de requisição para autenticação de usuário")
public class AuthRequestDTO {

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "O e-mail deve ser válido.")
    @Schema(description = "E-mail institucional do usuário", example = "ana.analista@orgao.gov.br")
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    @Schema(description = "Senha de acesso do usuário", example = "SenhaSegura123")
    private String senha;
}
