package br.com.biopark.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.biopark.controllers.UserController;
import br.com.biopark.vo.UserVO;
import br.com.biopark.exceptions.MinhaException;
import br.com.biopark.mapper.Mapper;
import br.com.biopark.models.User;
import br.com.biopark.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	UserRepository repository;

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<UserVO> findAll(Integer page){
		int number = 10 * page;
		List<UserVO> admins = Mapper.parseListObjects(repository.findAllPaged(number), UserVO.class);
		admins.stream().forEach(a -> a.add(linkTo(methodOn(UserController.class).findById(a.getKey())).withSelfRel()));
		return admins;
	}
	
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public UserVO findById(Long id) {
		if (id == null) throw new MinhaException("Id deve ser preenchido!");
		User entity = repository.findById(id).
				orElseThrow(() -> new MinhaException("Administrador não encontrado!"));
		UserVO vo = Mapper.parseObject(entity, UserVO.class);
		vo.add(linkTo(methodOn(UserController.class).findById(id)).withSelfRel());
		return vo;
	}
	
	@Transactional
	public UserVO save(UserVO admin) {
		if (admin.getKey() != null) throw new MinhaException("Id não deve ser preenchido!");
		if (admin.getNome() == null) throw new MinhaException("Nome deve ser preenchido!");
		if (admin.getUsername() == null) throw new MinhaException("Email deve ser preenchido!");
		if (admin.getPassword() == null) throw new MinhaException("Senha deve ser preenchida!");
		if (admin.getPermissions() == null || admin.getPermissions().size() == 0) throw new MinhaException("Permissões devem ser concedidas!");
		if (repository.existsByUsername(admin.getUsername()) > 0) throw new MinhaException("Username já está sendo utilizado por outra conta!");
		String password = encriptPassword(admin.getPassword());
		admin.setPassword(password);
		User entity = Mapper.parseObject(admin, User.class);
		entity.setAccountNonExpired(true);
		entity.setAccountNonLocked(true);
		entity.setCredentialsNonExpired(true);
		entity.setEnabled(true);
		// ENVIO DE EMAIL PARA O USERNAME
		UserVO persisted = Mapper.parseObject(repository.save(entity), UserVO.class);
		persisted.add(linkTo(methodOn(UserController.class).findById(persisted.getKey())).withSelfRel());
		return persisted;
	}
	
	@Transactional
	public UserVO update(UserVO admin) {
		if (admin.getKey() == null) throw new MinhaException("Id deve ser preenchido!");
		if (admin.getNome() == null) throw new MinhaException("Nome deve ser preenchido!");
		if (admin.getUsername() == null) throw new MinhaException("Email deve ser preenchido!");
		if (admin.getPermissions() == null || admin.getPermissions().size() == 0) throw new MinhaException("Permissões devem ser concedidas!");
		User adminAntigo = repository.findById(admin.getKey()).
				orElseThrow(() -> new MinhaException("Administrador não encontrado!"));
		adminAntigo.setNome(admin.getNome());
		adminAntigo.setUsername(admin.getUsername());
		adminAntigo.setPermissions(admin.getPermissions());
		UserVO persisted = Mapper.parseObject(repository.save(adminAntigo), UserVO.class) ;
		persisted.add(linkTo(methodOn(UserController.class).findById(persisted.getKey())).withSelfRel());
		return persisted;
	}
	
	@Transactional
	public void delete(Long id) {
		if (id == null) throw new MinhaException("Id deve ser preenchido!");
		User admin = repository.findById(id).
				orElseThrow(() -> new MinhaException("Administrador não encontrado!"));
		repository.delete(admin);
	}
	
	@Transactional
	public void changePassword(Long id, String password) {
		if (id == null) throw new MinhaException("Id deve ser preenchido!");
		if (password == null) throw new MinhaException("Senha deve ser preenchida!");
		User user = repository.findById(id).orElseThrow(() -> new MinhaException("Administrador não encontrado!"));
		String senha = encriptPassword(password);
		user.setPassword(senha);
		repository.save(user);
	}
	
	@Transactional
	public UserVO desableUser(Long id) {
		if (id == null) throw new MinhaException("Id deve ser preenchido!");
		User user = repository.findById(id).orElseThrow(() -> new MinhaException("Usuário não encontrado!"));
		if (user.isAccountNonExpired() == true && user.isAccountNonLocked() == true && user.isCredentialsNonExpired() == true && user.isEnabled() == true) {
			user.setAccountNonExpired(false);
			user.setAccountNonLocked(false);
			user.setCredentialsNonExpired(false);
			user.setEnabled(false);
		} else {
			user.setAccountNonExpired(true);
			user.setAccountNonLocked(true);
			user.setCredentialsNonExpired(true);
			user.setEnabled(true);
		}
		UserVO persisted = Mapper.parseObject(repository.save(user), UserVO.class);
		persisted.add(linkTo(methodOn(UserController.class).findById(id)).withSelfRel());
		return persisted;
	}
	
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public UserVO findByUsername(String username) {
		if (username == null) throw new MinhaException("Username deve ser preenchido!");
		User entity = repository.findByUsername(username);
		UserVO vo = Mapper.parseObject(entity, UserVO.class);
		vo.add(linkTo(methodOn(UserController.class).findById(entity.getId())).withSelfRel());
		return vo;
	}
	
	public String encriptPassword(String password) {
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		Pbkdf2PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder("", 8, 185000, SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
		encoders.put("pbkdf2", pbkdf2Encoder);
		DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
		passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
		String result = passwordEncoder.encode(password);
		return result;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User admin = repository.findByUsername(username);
		if(admin != null) {
			return admin;
		} else {
			throw new UsernameNotFoundException("Username " + username + " not found!");
		}
	}
}
