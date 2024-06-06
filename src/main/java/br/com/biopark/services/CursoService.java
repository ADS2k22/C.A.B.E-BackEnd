package br.com.biopark.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.biopark.dtos.CursoConclusaoDTO;
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
	
	public List<CursoConclusaoDTO> findAll(){
		List<CursoVO> cursos = Mapper.parseListObjects(repository.findAll(), CursoVO.class);
		List<CursoConclusaoDTO> lista = new ArrayList<>();
		for (CursoVO curso : cursos) {
			CursoConclusaoDTO dto = new CursoConclusaoDTO();
			dto.setCurso(curso);
			// AQUI FEITO A VERIFICAÇÃO, MAS POR ENQUANTO TUDO NÃO
			dto.setConcluido(false);
		}
		return lista;
	}
	
	public List<CursoConclusaoDTO> findWithFiltragens(FiltrosCursoDTO filtros){
		List<String> conditions = new ArrayList<>();
		String query = "SELECT c.id, c.nome, c.carga_horaria, c.trilha, c.categoria, c.feedback FROM Curso AS c ";
		
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
		List<CursoVO> cursos = Mapper.parseListObjects(customRepository.findWithFiltros(query), CursoVO.class);
		List<CursoConclusaoDTO> lista = new ArrayList<>();
		for (CursoVO curso : cursos) {
			CursoConclusaoDTO dto = new CursoConclusaoDTO();
			dto.setCurso(curso);
			// AQUI FEITO A VERIFICAÇÃO, MAS POR ENQUANTO TUDO NÃO
			dto.setConcluido(false);
		}
		return lista;
	}
}
