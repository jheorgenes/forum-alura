package br.com.alura.forumalura.config.security;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import br.com.alura.forumalura.modelo.Usuario;
import br.com.alura.forumalura.repository.UsuarioRepository;

@Service //Gerenciada pelo Spring como um Service
public class AutenticacaoService implements UserDetailsService { //Necessita implementar UserDetailsService para que o Spring saiba que essa á uma classe de autenticação
	
	@Autowired
	private UsuarioRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuario = repository.findByEmail(username);
		if(usuario.isPresent()) {
			return usuario.get();
		}
		throw new UsernameNotFoundException("Dados inválidos!");
	}

}
