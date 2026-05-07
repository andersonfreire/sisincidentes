package com.seguranca.sisincidentes.service.impl;

import com.seguranca.sisincidentes.domain.model.Usuario;
import com.seguranca.sisincidentes.domain.repository.UsuarioRepository;
import com.seguranca.sisincidentes.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Serviço que carrega os detalhes do usuário a partir do banco de dados.
 * Utilizado pelo AuthenticationManager do Spring Security.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (!StringUtils.hasText(email)) {
            throw new UsernameNotFoundException("O e-mail do usuário não pode ser nulo ou vazio.");
        }

        Usuario usuario = repository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email));

        if (!usuario.isAtivo()) {
            throw new UsernameNotFoundException("O usuário informado está inativo no sistema.");
        }

        return new UserDetailsImpl(usuario);
    }
}
