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
import br.com.biopark.repositories.VideoRepository;
import br.com.biopark.repositories.Video_User_RelacaoRepository;
import br.com.biopark.vo.CursoVO;

@Service
public class CursoService {

	@Autowired
	CursoRepository repository;
	@Autowired
	VideoRepository videoRepository;
	@Autowired
	Video_User_RelacaoRepository vurRepository;
	@Autowired
	CustomRepository customRepository;
	
	public List<CursoConclusaoDTO> findAll(Long userId){
		List<CursoVO> cursos = Mapper.parseListObjects(repository.findAll(), CursoVO.class);
		List<CursoConclusaoDTO> lista = new ArrayList<>();
		for (CursoVO curso : cursos) {
			CursoConclusaoDTO dto = new CursoConclusaoDTO();
			boolean isConcluded = true;
			dto.setCurso(curso);
			System.out.println(curso.getKey());
			List<Long> videos = videoRepository.findAllByCurso(curso.getKey());
			List<Boolean> conclusoes = new ArrayList<>();
			for (Long video : videos) {
				boolean conc = vurRepository.findByUserAndVideo(video, userId);
				System.out.println(conc);
				conclusoes.add(conc);
			}
			for (Boolean concc : conclusoes) {
				if (concc == false) isConcluded = false;
			}
			if (isConcluded == false) dto.setConcluido(false); else dto.setConcluido(true);
			lista.add(dto);
		}
		return lista;
	}
	
	public List<CursoConclusaoDTO> findWithFiltragens(FiltrosCursoDTO filtros, Long userId){
		List<String> conditions = new ArrayList<>();
		String query = "SELECT c.id, c.nome, c.carga_horaria, c.trilha, c.categoria, c.feedback FROM Curso AS c ";
		if (filtros.getNome() != null) {
			conditions.add("c.nome LIKE '%" + filtros.getNome() + "%'");
		}
		if (filtros.getCategoria() != null) {
			if (filtros.getNome() != null) {
				conditions.add("AND c.categoria.nome = '" + filtros.getCategoria() + "'");
			} else {
				conditions.add("c.categoria.nome = '" + filtros.getCategoria() + "'");
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
			boolean isConcluded = true;
			List<Long> videos = videoRepository.findAllByCurso(curso.getKey());
			List<Boolean> conclusoes = new ArrayList<>();
			for (Long video : videos) {
				boolean conc = vurRepository.findByUserAndVideo(video, userId);
				System.out.println(conc);
				conclusoes.add(conc);
			}
			for (Boolean concc : conclusoes) {
				if (concc == false) isConcluded = false;
			}
			if (isConcluded == false) dto.setConcluido(false); else dto.setConcluido(true);
			lista.add(dto);
		}
		return lista;
	}
	
	public List<CursoConclusaoDTO> findByRoadMap(String roadMap, Long userId){
		List<CursoVO> cursos = Mapper.parseListObjects(repository.findAllByRoadMap(roadMap), CursoVO.class);
		List<CursoConclusaoDTO> lista = new ArrayList<>();
		for (CursoVO curso : cursos) {
			CursoConclusaoDTO dto = new CursoConclusaoDTO();
			dto.setCurso(curso);
			boolean isConcluded = true;
			List<Long> videos = videoRepository.findAllByCurso(curso.getKey());
			List<Boolean> conclusoes = new ArrayList<>();
			for (Long video : videos) {
				boolean conc = vurRepository.findByUserAndVideo(video, userId);
				System.out.println(conc);
				conclusoes.add(conc);
			}
			for (Boolean concc : conclusoes) {
				if (concc == false) isConcluded = false;
			}
			if (isConcluded == false) dto.setConcluido(false); else dto.setConcluido(true);
			lista.add(dto);
		}
		return lista;
	}
}
