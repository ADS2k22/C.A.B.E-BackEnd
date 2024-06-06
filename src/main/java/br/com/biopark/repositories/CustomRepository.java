package br.com.biopark.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.biopark.models.Curso;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class CustomRepository {

	@PersistenceContext
	private EntityManager em;
	
	public List<Curso> findWithFiltros(String query){
		var q = em.createQuery(query, Curso.class);
		return q.getResultList();
	}
}
