package br.com.biopark.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.biopark.dtos.CursoConclusaoDTO;
import br.com.biopark.dtos.FiltrosCursoDTO;
import br.com.biopark.services.CursoService;
import br.com.biopark.util.MediaType;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/curso")
public class CursoController {

	@Autowired
	CursoService service;
	
	@GetMapping(produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	public List<CursoConclusaoDTO> findAll(){
		return service.findAll();
	}
	
	@PostMapping(value = "/filtragem",
			consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML },
			produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	public List<CursoConclusaoDTO> findWithFiltragem(@RequestBody FiltrosCursoDTO filtros){
		return service.findWithFiltragens(filtros);
	}
}
