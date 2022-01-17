package br.com.alura.forumalura.config.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import br.com.alura.forumalura.modelo.Usuario;
import br.com.alura.forumalura.repository.UsuarioRepository;

public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {
	
	/* Atributos não injetáveis pelo Bean por ser uma classe Filter */
	private TokenService tokenService;
	private UsuarioRepository repository;

	public AutenticacaoViaTokenFilter(TokenService tokenService, UsuarioRepository repository) {
		this.tokenService = tokenService;
		this.repository = repository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		String token = recuperarToken(request);
		boolean valido = tokenService.isTokenValido(token);
		if(valido) {
			autenticarCliente(token);
		}
		
		filterChain.doFilter(request, response); // Dizendo ao Spring que já foi executado o filtro e que pode seguir o fluxo da requisição
	}

	private void autenticarCliente(String token) {
		Long idUsuario = tokenService.getIdUsuario(token); //Obtendo o id do Usuario através do token
		Usuario usuario = repository.findById(idUsuario).get(); //Obtendo o Usuário logado do banco de dados através do id
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities()); //Criando o objeto autenticação do tipo UsernamePasswordAuthenticationToken (recebendo como parametro o objeto usuario, a senha [nula pq ja foi validada antes] e o tipo de perfil do usuário [herdado])
		SecurityContextHolder.getContext().setAuthentication(authentication); //Dizendo ao Spring que o usuário está autenticado setando a autenticação (método stático)
	}

	private String recuperarToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) { // Se o token for nulo ou vazio ou não for do tipo Bearer
			return null;
		}
		return token.substring(7, token.length()); // Se o token for ok, pega o token a partir do sétimo caracter da string (tirando o "Bearer ")
	}

}
