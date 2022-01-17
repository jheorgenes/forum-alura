package br.com.alura.forumalura.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.alura.forumalura.config.security.TokenService;
import br.com.alura.forumalura.controller.dto.TokenDto;
import br.com.alura.forumalura.controller.form.LoginForm;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

	@Autowired
	private AuthenticationManager authManager; // Classe injetada para fazer as autenticações (O spring não injeta sozinho, por isso a classe SecurityConfigurations herda WebSecurityConfigurerAdapter que sabe criar um AuthenticationManager

	@Autowired
	private TokenService tokenService; //Classe service para utilizar a biblioteca JJWT (Classe criada manualmente dentro do pacote config.security)

	@PostMapping
	public ResponseEntity<TokenDto> autenticar(@RequestBody @Valid LoginForm form) { //Método que fará a authenticação, pegando os dados do corpo da requisição de um formulário (dto)
		UsernamePasswordAuthenticationToken dadosLogin = form.converter(); //Criando um objeto dadosLogin do tipo UsernamePasswordAuthenticationToken usando a conversão do formulário

		try {
			Authentication authentication = authManager.authenticate(dadosLogin); //Realiza a autenticação com os dados de login e retorna um objeto authenticado
			String token = tokenService.gerarToken(authentication); //Gera um token contendo a autenticação criada
			return ResponseEntity.ok(new TokenDto(token, "Bearer")); //Retorna status 200 se deu tudo certo (passando o TokenDto e o tipo de autenticação HTTP a ser feita pelo cliente)(Tem outros tipos como Basic e Digest)
		} catch (AuthenticationException e) { //Exception específica para erro de autenticação
			return ResponseEntity.badRequest().build(); //Retorna status 400 bad Request
		}
	}
}
