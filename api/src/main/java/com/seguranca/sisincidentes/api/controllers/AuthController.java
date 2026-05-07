package com.seguranca.sisincidentes.api.controllers;

import com.seguranca.sisincidentes.api.dto.AuthRequestDTO;
import com.seguranca.sisincidentes.api.dto.AuthResponseDTO;
import com.seguranca.sisincidentes.api.dto.UsuarioResponseDTO;
import com.seguranca.sisincidentes.domain.model.Usuario;
import com.seguranca.sisincidentes.security.TokenService;
import com.seguranca.sisincidentes.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller para autenticação de usuários.
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para login e geração de tokens JWT")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Operation(summary = "Realizar Login", description = "Valida as credenciais e retorna um token JWT.")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
        log.info("Tentativa de login para o usuário: {}", request.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Usuario usuario = userDetails.getUsuario();
        String token = tokenService.generateToken(userDetails);

        AuthResponseDTO response = AuthResponseDTO.builder()
                .token(token)
                .tipo("Bearer")
                .usuario(toResponseDTO(usuario))
                .build();

        log.info("Usuário {} autenticado com sucesso.", request.getEmail());
        return ResponseEntity.ok(response);
    }

    /**
     * Helper para converter a entidade Usuario em DTO de resposta.
     * (Pode ser movido para um Mapper no futuro).
     */
    private UsuarioResponseDTO toResponseDTO(Usuario entity) {
        return UsuarioResponseDTO.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .nome(entity.getNome())
                .matricula(entity.getMatricula())
                .telefone(entity.getTelefone())
                .funcao(entity.getFuncao())
                .funcaoDescricao(entity.getFuncao() != null ? entity.getFuncao().getDescricao() : null)
                .perfil(entity.getPerfil())
                .ativo(entity.isAtivo())
                .unidadeId(entity.getUnidade().getId())
                .unidadeTitulo(entity.getUnidade().getTitulo())
                .unidadeSigla(entity.getUnidade().getSigla())
                .build();
    }
}
