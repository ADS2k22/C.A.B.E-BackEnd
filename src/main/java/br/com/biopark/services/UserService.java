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
import br.com.biopark.exceptions.MinhaException;
import br.com.biopark.mapper.Mapper;
import br.com.biopark.models.Permission;
import br.com.biopark.models.User;
import br.com.biopark.models.Video;
import br.com.biopark.models.Video_User_Relacao;
import br.com.biopark.repositories.PermissionRepository;
import br.com.biopark.repositories.UserRepository;
import br.com.biopark.repositories.VideoRepository;
import br.com.biopark.repositories.Video_User_RelacaoRepository;
import br.com.biopark.vo.UserVO;
import jakarta.mail.MessagingException;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	UserRepository repository;
	@Autowired
	VideoRepository videoRepository;
	@Autowired
	Video_User_RelacaoRepository vurRepository;
	@Autowired
	PermissionRepository permissionRepository;
	@Autowired
	EmailService emailService;

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
		String to = admin.getUsername();
		String subject = "Bem vindo ao Centro de Aprendizagem Biopark Educação(C.A.B.E)";
		String htmlbody = """
	            <html>
	            <body>
	                <div style="border-style:solid;border-width:thin;border-color:#dadce0;border-radius:8px;padding:40px 20px" align="center">
	                    <div style="font-family:'Google Sans',Roboto,RobotoDraft,Helvetica,Arial,sans-serif;border-bottom:thin solid #dadce0;color:rgba(0,0,0,0.87);line-height:32px;padding-bottom:24px;text-align:center;word-break:break-word">
	                        <div style="font-size:24px">Bem vindo ao Centro de Aprendizagem Biopark Educação(C.A.B.E)</div>
	                    </div>
	                    <div style="font-family:Roboto-Regular,Helvetica,Arial,sans-serif;font-size:14px;color:rgba(0,0,0,0.87);line-height:20px;padding-top:20px;text-align:center">
	                        Esperamos que nossos cursos enriqueçam o seu aprendizado e lhe ajudem a trilhar um caminho de conhecimento. Aproveite!
	                    </div>
	                </div>
	            </body>
	            </html>
	            """;
		try {
            emailService.sendHtmlMessage(to, subject, htmlbody);
        } catch (MessagingException e) {
            e.printStackTrace();
        }	
		UserVO persisted = Mapper.parseObject(repository.save(entity), UserVO.class);
		persisted.add(linkTo(methodOn(UserController.class).findById(persisted.getKey())).withSelfRel());
		List<Video> videos = videoRepository.findAll();
		for (Video video : videos) {
			Video_User_Relacao vur = new Video_User_Relacao();
			vur.setUser(Mapper.parseObject(persisted, User.class));
			vur.setVideo(video);
			vur.setConcluido(false);
			vurRepository.save(vur);
		}
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
	
	@Transactional
	public UserVO updateUser(Long userId) {
		if (userId == null) throw new MinhaException("Id deve ser preenchido!");
		User user = repository.findById(userId).orElseThrow(() -> new MinhaException("Usuário não encontrado!"));
		Permission sub = permissionRepository.findById(3L).orElseThrow(() -> new MinhaException("Permissão não encontrada!"));
		user.getPermissions().add(sub);
		String to = user.getUsername();
		String subject = "Parabéns " + user.getNome() + "!";
		String htmlbody = """
	            <html>
	            <body>
	                <div style="border-style:solid;border-width:thin;border-color:#dadce0;border-radius:8px;padding:40px 20px" align="center">
	                    <div style="font-family:'Google Sans',Roboto,RobotoDraft,Helvetica,Arial,sans-serif;border-bottom:thin solid #dadce0;color:rgba(0,0,0,0.87);line-height:32px;padding-bottom:24px;text-align:center;word-break:break-word">
	                        <div style="font-size:24px">Você agora é assinante premium do C.A.B.E!</div>
	                    </div>
	                    <div style="font-family:Roboto-Regular,Helvetica,Arial,sans-serif;font-size:14px;color:rgba(0,0,0,0.87);line-height:20px;padding-top:20px;text-align:center">
	                        Agora você pode aproveitar os nossos serviços de roadmap e eventos. Aproveite!
	                    </div>
	                </div>
	            </body>
	            </html>
	            """;
		try {
            emailService.sendHtmlMessage(to, subject, htmlbody);
        } catch (MessagingException e) {
            e.printStackTrace();
        }	
		return Mapper.parseObject(repository.save(user), UserVO.class);
	}
}
