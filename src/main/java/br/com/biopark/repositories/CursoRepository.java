package br.com.biopark.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.biopark.models.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long>{}
