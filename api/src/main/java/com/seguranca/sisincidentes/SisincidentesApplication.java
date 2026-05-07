package com.seguranca.sisincidentes;

import com.seguranca.sisincidentes.domain.model.FuncaoUsuario;
import com.seguranca.sisincidentes.domain.model.Perfil;
import com.seguranca.sisincidentes.domain.model.UnidadeAdministrativa;
import com.seguranca.sisincidentes.domain.model.Usuario;
import com.seguranca.sisincidentes.domain.repository.UnidadeAdministrativaRepository;
import com.seguranca.sisincidentes.domain.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

@SpringBootApplication
public class SisincidentesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SisincidentesApplication.class, args);
	}

	@Bean
	public CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, 
                                          UnidadeAdministrativaRepository unidadeRepository,
                                          PasswordEncoder passwordEncoder) {
		return args -> {
			if (unidadeRepository.count() == 0) {
				UnidadeAdministrativa unidadeRaw = UnidadeAdministrativa.builder()
                        .codigo("DTI-001")
						.titulo("Diretoria de Tecnologia")
						.sigla("DTI")
						.build();
				UnidadeAdministrativa unidade = unidadeRepository.save(Objects.requireNonNull(unidadeRaw));

				Usuario adminRaw = Usuario.builder()
						.nome("Admin")
						.email("admin@sisincidentes.gov.br")
						.senha(passwordEncoder.encode("admin123"))
						.perfil(Perfil.ADMIN)
						.funcao(FuncaoUsuario.ANALISTA_TI)
						.unidade(unidade)
						.ativo(true)
						.build();
				usuarioRepository.save(Objects.requireNonNull(adminRaw));
                
                Usuario operadorRaw = Usuario.builder()
						.nome("Operador")
						.email("operador@sisincidentes.gov.br")
						.senha(passwordEncoder.encode("operador123"))
						.perfil(Perfil.OPERADOR)
						.funcao(FuncaoUsuario.TECNICO_TI)
						.unidade(unidade)
						.ativo(true)
						.build();
				usuarioRepository.save(Objects.requireNonNull(operadorRaw));
			}
		};
	}
}
