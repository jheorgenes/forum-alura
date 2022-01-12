package br.com.alura.forumalura.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forumalura.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forumalura.controller.dto.TopicoDto;
import br.com.alura.forumalura.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forumalura.controller.form.TopicoForm;
import br.com.alura.forumalura.modelo.Topico;
import br.com.alura.forumalura.repository.CursoRepository;
import br.com.alura.forumalura.repository.TopicoRepository;

/**
 * @RestController anotation que indica ao Spring que o retorno do método deve ser devolvido no corpo da resposta da página
 * @RestController também utiliza uma biblioteca chamada Jackson que realiza as conversões dos dados em JSON e devolve uma String com esse formato
 * Ao utilizar o @RestController na classe e não nos métodos, estamos dizendo que todo o método dessa classe está com o @ResponseBody definido e que será retornado no corpo da resposta os objetos declarados
 */
@RestController
@RequestMapping("/topicos") //Definindo mapeamento geral de topicos
public class TopicosController {
	
	@Autowired
	private TopicoRepository topicoRepository;
	
	@Autowired
	private CursoRepository cursoRepository;

	@GetMapping //Atribuíndo ao verbo HTTP GET o método abaixo.
	public List<TopicoDto> lista(String nomeCurso) { //Recebendo parametro da URL
		if(nomeCurso == null) { //Se não houver parametro informado, faça
			List<Topico> topicos = topicoRepository.findAll(); //Lista tudo
			return TopicoDto.converter(topicos);
		} else { //Se tiver o parametro nomeCurso informado
			List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso); //Lista todos os topicos que tiver o nome do Curso que foi passado
			return TopicoDto.converter(topicos);
		}
	}
	
	/**
	 * Método cadastrar realiza o cadastro de um novo tópico no banco de dados e retorna pra view
	 * @param TopicoForm é uma classe criada para buscar atributos específicos que serão cadastrados. 
	 * A anotação @RequestBody especifica que esses dados não são parametros da URI e sim parametros recebidos no corpo da requisição.
	 * A anotação @Valid especifica que requisição que chegar será validada pelo BeanValidation (que integra com o spring através da dependencia) de acordo com as regras definidas como anotation na classe TopicoForm
	 * @param UriComponentsBuilder injeta um objeto responsável por buscar o caminho completo da uri que está sendo utilizado. 
	 * 
	 * uriBuilder precisa de um path para definir manualmente o ultimo trecho da rota, chamando o método buildAndExpand para receber qual o atributo do objeto que será consultado e converter tudo na URI completa.
	 * Essa uri será passada como objeto e será recebida no método created da classe ResponseEntity.
	 * 
	 * @return ResponseEntity.created, ou seja, retorna um objeto resposta com status HTTP 201 (de criação), a uri e corpo contendo os dados do objeto que representa a classe topico, que é a classe TopicoDto que foi criado. 
	 */
	@PostMapping
	@Transactional
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm topicoForm, UriComponentsBuilder uriBuilder) {
		Topico topico = topicoForm.converter(cursoRepository); //Convertendo um formulário de topico convertendo cursoRepository e obtendo um tópico completo
		topicoRepository.save(topico); //Salvando o topico no banco de dados
		
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri(); //Obtendo a URI completa da requisição
		return ResponseEntity.created(uri).body(new TopicoDto(topico)); //Retornando a resposta com o status, a uri e o corpo com a nova entidade cadastrada
	}
	
	/**
	 * Método para buscar dados detalhados de um tópico específico.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id) { //Anotation define que a variável recebida não é um parametro junto a url e sim uma variável path que vem após a url especificada
		Optional<Topico> topico = topicoRepository.findById(id);
		if(topico.isPresent()) {
			return ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get()));
		}
		return ResponseEntity.notFound().build();
	}
	
	/**
	 * Método para atualizar um tópico específico
	 * Atualiza apenas o Titulo e a Mensagem
	 */
	@PutMapping("/{id}")
	@Transactional //Dispara o commit no banco de dados quando for realizar a atualização
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm topicoForm) {
		Optional<Topico> optional = topicoRepository.findById(id);
		if(optional.isPresent()) {
			Topico topico = topicoForm.atualizar(id, topicoRepository);
			return ResponseEntity.ok(new TopicoDto(topico)); //Devolvendo a uri com status 200 e com o corpo da requisição criando um novo TopicoDto
		}
		return ResponseEntity.notFound().build();	
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Topico> optional = topicoRepository.findById(id);
		if(optional.isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();	
	}
}