package br.com.biopark.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.biopark.controllers.PermissionController;
import br.com.biopark.vo.PermissionVO;
import br.com.biopark.exceptions.MinhaException;
import br.com.biopark.mapper.Mapper;
import br.com.biopark.models.Permission;
import br.com.biopark.repositories.PermissionRepository;

@Service
public class PermissionService {

	@Autowired
	PermissionRepository repository;
	
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<PermissionVO> findAll(){
		List<PermissionVO> perms = Mapper.parseListObjects(repository.findAll(), PermissionVO.class);
		perms.stream().forEach(a -> a.add(linkTo(methodOn(PermissionController.class).findById(a.getKey())).withSelfRel()));
		return perms;
	}
	
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public PermissionVO findById(Long id) {
		if (id == null) throw new MinhaException("Id deve ser preenchido!");
		Permission entity = repository.findById(id).
				orElseThrow(() -> new MinhaException("Permissão não encontrada!"));
		PermissionVO vo = Mapper.parseObject(entity, PermissionVO.class);
		vo.add(linkTo(methodOn(PermissionController.class).findById(id)).withSelfRel());
		return vo;
	}
}
