package br.com.biopark.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.biopark.models.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long>{
	
	@Query(nativeQuery = true, value = "SELECT * FROM curso WHERE trilha = :trilha")
	public List<Curso> findAllByRoadMap(@Param(value = "trilha") String trilha);
}
