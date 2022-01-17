package br.com.alura.forumalura.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.alura.forumalura.modelo.Topico;

public interface TopicoRepository extends JpaRepository<Topico, Long>{

	Page<Topico> findByCursoNome(String nomeCurso, Pageable paginacao);
	
//	@Query("SELECT T FROM TOPICO T WHERE T.CURSO.NOME = :nomeCurso")
//	List<Topico> carregarPorNomeCurso(@Param("nomeCurso") String nomeCurso); //Exemplo de como fazer uma query na mão sem a ajuda do Spring Data JPA
}
