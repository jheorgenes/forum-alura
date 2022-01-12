package br.com.alura.forumalura.controller.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import br.com.alura.forumalura.modelo.Topico;

public class TopicoDto {
	
	private Long id;
	private String titulo;
	private String mensagem;
	private LocalDateTime dataCriacao;
	
	public TopicoDto(Topico topico) {
		this.id = topico.getId();
		this.titulo = topico.getTitulo();
		this.mensagem = topico.getMensagem();
		this.dataCriacao = topico.getDataCriacao();
	}
	
	public Long getId() {
		return id;
	}
	public String getTitulo() {
		return titulo;
	}
	public String getMensagem() {
		return mensagem;
	}
	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}
	
	// Faz a conversão de uma lista de topicos e retorna um topicoDto
	public static List<TopicoDto> converter(List<Topico> topicos) { //Utilizando java 8 abaixo para utilizar o Stream e não precisar fazer o loop manualmente
		return topicos.stream().map(TopicoDto::new).collect(Collectors.toList()); //fazendo um map de topico para topicoDto (chamando o construtor do topicoDto como parametro) e transformando em uma lista com .collect(Collectors.toList())
	}
}
