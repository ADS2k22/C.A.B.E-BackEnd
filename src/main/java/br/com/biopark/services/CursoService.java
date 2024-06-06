package br.com.biopark.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.biopark.repositories.CursoRepository;

@Service
public class CursoService {

	@Autowired
	CursoRepository repository;
	
	
}
