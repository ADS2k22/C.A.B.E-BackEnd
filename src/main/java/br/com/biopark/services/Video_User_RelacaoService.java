package br.com.biopark.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.biopark.mapper.Mapper;
import br.com.biopark.repositories.Video_User_RelacaoRepository;
import br.com.biopark.vo.Video_User_RelacaoVO;

@Service
public class Video_User_RelacaoService {
	
	@Autowired
	Video_User_RelacaoRepository repository;
	
	public List<Video_User_RelacaoVO> findAllByCursoId(Long cursoId, Long adminId) {
		return Mapper.parseListObjects(repository.findAllByCursoId(adminId, cursoId), Video_User_RelacaoVO.class);
	}
}
