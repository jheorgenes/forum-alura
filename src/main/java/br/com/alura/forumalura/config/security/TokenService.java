package br.com.alura.forumalura.config.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import br.com.alura.forumalura.modelo.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {
	
	@Value("${forum.jwt.expiration}")
	private String expiration; //Tempo de expiração em milisegundos definido equivalente a 1 dia
	
	@Value("${forum.jwt.secret}")
	private String secret;

	/** Gerando token para o Cliente */
	public String gerarToken(Authentication authentication) {
		Usuario logado = (Usuario) authentication.getPrincipal(); //Recuperando o usuário logado (necessita fazer cast pois o método getPrincipal devolve um Object classe) 
		Date hoje = new Date(); //Criando uma instância da data de hoje (Classe antiga do java.util pois o JWTS ainda não está atualizado)
		Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration)); //Pegando a hora atual e somando com a data de expiração formatada em long (obtida do application.properties e injetado aqui) e obtendo o tempo de expiração do Token
		
		return Jwts.builder() //Chamando a classe JWTS com o método builder (Permite setar as configurações para gerar o Token)
				   .setIssuer("API do forum da Alura") //Descrevendo quem é que está gerando a autenticação
				   .setSubject(logado.getId().toString()) //Quem é o usuário autenticado (precisa ser uma String, então converte o ID em toString)
				   .setIssuedAt(hoje) //A data de geração do Token
				   .setExpiration(dataExpiracao) //Data de expiração do Token
				   .signWith(SignatureAlgorithm.HS256, secret) //Definindo o tipo de criptografia do Token (recebe como parametro o algoritmo de criptografia e a senha criptografada
				   .compact(); //Compactando e salvando em uma String
	}

	public boolean isTokenValido(String token) {
		
		try {
			Jwts.parser() //método que tem a lógica pra fazer a descriptografia do token e verificar se está ok
				.setSigningKey(this.secret) //usando a chave pra criptografar ou descriptografar
				.parseClaimsJws(token); //Valida o token e se tiver correto devolve um objeto, se tiver errado devolve uma exception
			return true; //Retorna que está válido.
		} catch (Exception e) {
			return false; //Retorna que não está válido
		}
	}

	public Long getIdUsuario(String token) {
		Claims claims = Jwts.parser()
							.setSigningKey(this.secret) 
							.parseClaimsJws(token) 
							.getBody(); //Pegando o corpo do token
		
		return Long.parseLong(claims.getSubject()); //Retorna o id do usuário do token que foi autenticado (convertido de string para Long)
	}

}
