package br.com.biopark.mapper;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import br.com.biopark.models.Categoria;
import br.com.biopark.models.Curso;
import br.com.biopark.models.Permission;
import br.com.biopark.models.User;
import br.com.biopark.models.Video;
import br.com.biopark.models.Video_User_Relacao;
import br.com.biopark.vo.CategoriaVO;
import br.com.biopark.vo.CursoVO;
import br.com.biopark.vo.PermissionVO;
import br.com.biopark.vo.UserVO;
import br.com.biopark.vo.VideoVO;
import br.com.biopark.vo.Video_User_RelacaoVO;

public class Mapper {

	private static ModelMapper mapper = new ModelMapper();
	
	static {
        mapper.createTypeMap(User.class, UserVO.class).addMapping(User::getId, UserVO::setKey);
        mapper.createTypeMap(UserVO.class, User.class).addMapping(UserVO::getKey, User::setId);
        mapper.createTypeMap(Permission.class, PermissionVO.class).addMapping(Permission::getId, PermissionVO::setKey);
        mapper.createTypeMap(PermissionVO.class, Permission.class).addMapping(PermissionVO::getKey, Permission::setId);
        mapper.createTypeMap(Curso.class, CursoVO.class).addMapping(Curso::getId, CursoVO::setKey);
        mapper.createTypeMap(CursoVO.class, Curso.class).addMapping(CursoVO::getKey, Curso::setId);
        mapper.createTypeMap(Video.class, VideoVO.class).addMapping(Video::getId, VideoVO::setKey);
        mapper.createTypeMap(VideoVO.class, Video.class).addMapping(VideoVO::getKey, Video::setId);
        mapper.createTypeMap(Categoria.class, CategoriaVO.class).addMapping(Categoria::getId, CategoriaVO::setKey);
        mapper.createTypeMap(CategoriaVO.class, Categoria.class).addMapping(CategoriaVO::getKey, Categoria::setId);
        mapper.createTypeMap(Video_User_Relacao.class, Video_User_RelacaoVO.class).addMapping(Video_User_Relacao::getId, Video_User_RelacaoVO::setKey);
        mapper.createTypeMap(Video_User_RelacaoVO.class, Video_User_Relacao.class).addMapping(Video_User_RelacaoVO::getKey, Video_User_Relacao::setId);
    }
	
	public static <O, D> D parseObject(O origin, Class<D> destination) {
		return mapper.map(origin, destination);
	}
	
	public static <O, D> List<D> parseListObjects(List<O> origin, Class<D> destination) {
		List<D> destinationObjects = new ArrayList<D>();
		for (O o : origin) {
			destinationObjects.add(mapper.map(o, destination));
		}
		return destinationObjects;
	}
}
