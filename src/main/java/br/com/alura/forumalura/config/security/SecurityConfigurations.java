package br.com.alura.forumalura.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.alura.forumalura.repository.UsuarioRepository;

@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private AutenticacaoService autenticacaoService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	@Bean //Indicando ao Spring que esse método retorna um authenticationManager pro Spring conseguir injetá-lo onde precisar.
	protected AuthenticationManager authenticationManager() throws Exception { //Método sobrescrito que retorna um authenticationManager
		return super.authenticationManager();
	}
	
	//Configurações de autenticação
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder()); //Informa ao Spring qual é a classe que tem a lógica de autenticação e recebe um UserDetailsService e chama o método passwordEncoder
		//Método passwordEncoder recebe como parametro o tipo de criptografia esolhida para 
	}
	
	//Configurações de autorização
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests() //executando o método que realiza as autorizações
			.antMatchers(HttpMethod.GET, "/topicos") //Definindo qual endpoint e qual verbo HTTP será observado.
			.permitAll() //Liberando acesso ao endpoint
			.antMatchers(HttpMethod.GET, "/topicos")
			.permitAll()
			.antMatchers(HttpMethod.GET, "/actuator")
			.permitAll()
			.antMatchers(HttpMethod.GET, "/actuator/**")
			.permitAll()
			.antMatchers(HttpMethod.POST, "/auth")
			.permitAll()
			.anyRequest() //Demais endpoints
			.authenticated() //Obrigatório estar autenticado
			//.and().formLogin(); //E faça o Spring gerar um formulário de autenticação (O spring já tem esse formulário padrão)
			.and().csrf().disable() //Desabilitando cross-site request forgery (validador de ataque hacker de requisições web) pois o Spring realiza essa validação
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //Definindo ao Spring Security que nesse projeto não é pra criar sessões pois iremos trabalhar com autenticação stateless
			.and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class); //Adicionando um filtro ANTES dos filtros que existe na requisição (Passa como argumento o filtro que será adicionado ANTES e o filtro que já existe na requisição, pra saber a ordem de execução dos filtros)
	}
	
	//Configurações de recursos estáticos(js, css, imagens, etc.)
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web.ignoring()
	        .antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**");
	}
	
}
