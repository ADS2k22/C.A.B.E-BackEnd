package br.com.biopark.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.biopark.dtos.CursoConclusaoDTO;
import br.com.biopark.dtos.FiltrosCursoDTO;
import br.com.biopark.services.CursoService;
import br.com.biopark.util.MediaType;
import br.com.biopark.vo.Video_User_RelacaoVO;

@RestController
@RequestMapping("/api/curso")
public class CursoController {

	@Autowired
	CursoService service;
	
	@GetMapping(produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	public List<CursoConclusaoDTO> findAll(@RequestParam(value = "id", defaultValue = "1") Long idUser){
		return service.findAll(idUser);
	}
	
	@GetMapping(value = "/filtragem",
			produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	public List<CursoConclusaoDTO> findWithFiltragem(@RequestParam(value = "nome", required = false) String nome, 
			@RequestParam(value = "categoria", required = false) String categoria,
			@RequestParam(value = "id", defaultValue = "1") Long idUser){
		FiltrosCursoDTO filtros = new FiltrosCursoDTO();
		filtros.setNome(nome);
		filtros.setCategoria(categoria);
		return service.findWithFiltragens(filtros, idUser);
	}
	
	@GetMapping(value = "/roadmap", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	public List<CursoConclusaoDTO> findAllByRoadMap(@RequestParam(value = "roadmap") String roadmap,
	        @RequestParam(value = "idUser", defaultValue = "1") Long idUser) {
	    return service.findByRoadMap(roadmap, idUser);
	}
	
	@GetMapping(value = "/videos", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	public List<Video_User_RelacaoVO> findAllByCursoId(@RequestParam(value = "cursoid") Long cursoid, @RequestParam(value = "userid") Long userid){
		return service.findAllByCursoId(cursoid, userid);
	}
}
