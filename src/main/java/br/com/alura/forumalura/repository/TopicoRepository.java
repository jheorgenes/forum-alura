package br.com.alura.forumalura.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.alura.forumalura.modelo.Topico;

public interface TopicoRepository extends JpaRepository<Topico, Long>{

	List<Topico> findByCursoNome(String nomeCurso);
	
//	@Query("SELECT T FROM TOPICO T WHERE T.CURSO.NOME = :nomeCurso")
//	List<Topico> carregarPorNomeCurso(@Param("nomeCurso") String nomeCurso); //Exemplo de como fazer uma query na mão sem a ajuda do Spring Data JPA
}
