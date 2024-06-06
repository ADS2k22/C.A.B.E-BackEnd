package br.com.biopark.mapper;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import br.com.biopark.vo.UserVO;
import br.com.biopark.vo.PermissionVO;
import br.com.biopark.models.User;
import br.com.biopark.models.Permission;

public class Mapper {

	private static ModelMapper mapper = new ModelMapper();
	
	static {
        mapper.createTypeMap(User.class, UserVO.class).addMapping(User::getId, UserVO::setKey);
        mapper.createTypeMap(UserVO.class, User.class).addMapping(UserVO::getKey, User::setId);
        mapper.createTypeMap(Permission.class, PermissionVO.class).addMapping(Permission::getId, PermissionVO::setKey);
        mapper.createTypeMap(PermissionVO.class, Permission.class).addMapping(PermissionVO::getKey, Permission::setId);
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
