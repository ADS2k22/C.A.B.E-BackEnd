package br.com.biopark.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.biopark.dtos.FiltrosCursoDTO;
import br.com.biopark.mapper.Mapper;
import br.com.biopark.repositories.CursoRepository;
import br.com.biopark.repositories.CustomRepository;
import br.com.biopark.vo.CursoVO;

@Service
public class CursoService {

	@Autowired
	CursoRepository repository;
	@Autowired
	CustomRepository customRepository;
	
	public List<CursoVO> findAll(int page){
		int number = page * 10;
		List<CursoVO> cursos = Mapper.parseListObjects(repository.findAllPaged(number), CursoVO.class);
		return cursos;
	}
	
	public List<CursoVO> findWithFiltragens(FiltrosCursoDTO filtros, int page){
		List<String> conditions = new ArrayList<>();
		String query = "SELECT c.id, c.nome, c.carga_horaria, c.categoria FROM Conta AS c ";
		int number = page * 10;
		
		if (filtros.getNome() != null) {
			conditions.add("c.nome LIKE '%" + filtros.getNome() + "%'");
		}
		if (filtros.getCategoria() != null) {
			if (filtros.getNome() != null) {
				conditions.add("AND c.categoria = '" + filtros.getCategoria() + "'");
			} else {
				conditions.add("c.categoria = '" + filtros.getCategoria() + "'");
			}
		}
		if (conditions.size() != 0) {
			query = query + " WHERE " +  String.join(" ", conditions);
		}
		query = query + " GROUP BY (c) ORDER BY c.id ASC LIMIT 10 OFFSET " + number;
		List<CursoVO> cursos = Mapper.parseListObjects(customRepository.findWithFiltros(query), CursoVO.class);
		return cursos;
	}
}
