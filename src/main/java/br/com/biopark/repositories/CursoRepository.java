package br.com.biopark.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.biopark.models.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long>{
	
	@Query(nativeQuery = true, value = "SELECT * FROM curso ORDER BY id ASC LIMIT 10 OFFSET :page")
	List<Curso> findAllPaged(@Param(value = "page") Integer page);
}
