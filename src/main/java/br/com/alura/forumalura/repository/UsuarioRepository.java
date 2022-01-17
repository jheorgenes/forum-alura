package br.com.alura.forumalura.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.alura.forumalura.modelo.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	Optional<Usuario> findByEmail(String email);
}
