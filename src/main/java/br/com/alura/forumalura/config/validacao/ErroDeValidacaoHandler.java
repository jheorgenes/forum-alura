package br.com.alura.forumalura.config.validacao;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice //Define que essa classe será uma classe de validação de erros de requisição
public class ErroDeValidacaoHandler {
	
	@Autowired
	private MessageSource messageSource;
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST) //Anotation define que ao executar esse método, será retornado na requisição a resposta com código 400 (Bad Request)
	@ExceptionHandler(MethodArgumentNotValidException.class) //Anotation gerencia as validações que ocorrerem nas requisições que gerarem exceptions do tipo MethodArgumentNotValidException classe
	public List<ErroDeFormularioDto> handle(MethodArgumentNotValidException exception) { //Recebendo como argumento a classe de exceptions que foram geradas
		List<ErroDeFormularioDto> dto = new ArrayList<ErroDeFormularioDto>(); //Instanciando uma lista de ErroDeFormularioDto
		
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors(); //Obtendo os campos que estão com erro na requisição e retornando em uma lista de erros ao objeto FieldError do spring (que realiza validação de erros http)
		fieldErrors.forEach(e -> { //Percorrendo os erros
			String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale()); //Obtendo uma mensagem do spring gerada da classe injetada MessageSource (recebe como parametro a mensagem e a LocaleContextHolder.getLocale() que obtém o idioma do browser que enviou a requisição
			ErroDeFormularioDto erro = new ErroDeFormularioDto(e.getField(), mensagem); //Instanciando a classe ErroDeFormularioDto e passando o campo do erro e a mensagem
			dto.add(erro); //Adicionando o erro formado na lista ErroDeFormularioDto
		});
		
		return dto; //Retorna a lista completa de erros
	}
}
