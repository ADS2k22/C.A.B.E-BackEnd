package br.com.biopark.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.biopark.models.Video;

public interface VideoRepository extends JpaRepository<Video, Long>{
	
	@Query(nativeQuery = true, value = "SELECT id FROM video WHERE curso_id = :cursoid")
	public List<Long> findAllByCurso(@Param(value = "cursoid") Long cursoid);
}
