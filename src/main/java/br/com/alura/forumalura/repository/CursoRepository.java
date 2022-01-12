package br.com.alura.forumalura.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.forumalura.modelo.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long>{

	Curso findByNome(String nomeCurso);

}
