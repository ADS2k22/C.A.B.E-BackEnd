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
import br.com.biopark.models.User;
import br.com.biopark.models.Video;
import br.com.biopark.models.Video_User_Relacao;
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
		String htmlbody = "<tbody><tr><td width=\"8\" style=\"width:8px\"></td><td><div style=\"border-style:solid;border-width:thin;border-color:#dadce0;border-radius:8px;padding:40px 20px\" align=\"center\" class=\"m_-7537276007738394286mdv2rw\"><img src=\"https://lh3.googleusercontent.com/pw/AP1GczMuXXz3SfopFvDEzhavhgdmRmvdpmYVmPZVaRe-LmdTdbIPcuhf2qjQ3-TBMMquqUUJJ7YNkxbvJSGbDMVyAj9K04khezGD9mEM_IwPKmlxFkkp11ctLGhJLPwHzCeZ0562iN3RaIU7sDQMWs8a3HbgYSfCoSFM-7STQsNdYQGcT52FnTi-AihER8J7Z-vyYR2W-8glQx16lv5z5pJFrTqxmcxYSv33JePQGRHz51qzI-K3BbnUxvEj3joSWHHcydFssQ_79X_3GEUFPq1DrBRDumwNiUCZ9ImOco9PenfYQaIvA3vKCcDcDdkXtaB4LOeO9C5y8G4b61HN1eF0bul_ZgJafKa8hTVSmCaK2Y4QEf0tcxh3yrviU5UTYFXoiVBsNlilrOfAdJL0GYR6Bv1uixYIkePRRVbe7TMpE--sFpGmXSX3UUD-DmIwq5VOpS3qJGWSwm0nrcuiD2OwihJmdye_gx9PacIGP70wdO2I7b_a6tp5ev3XtrbWSsXpX1x0H3G_eDLTcHTzYJmNKGuqtQH7r-uuhxOEqHGUA0X1OergmoUVGEZ8eHluwe-FdZWBLqoBSv_qSdDRf_kLoEgI7NIDH6lzTRtnHsiwdSpfg5PwN7McS4U2sYGAC8Qef75eI1rrjWTdfGHymaVtZYH8qUkMZnao42Kz-LxrbvUwUKlN89KIdX7d-2b8b-WJJm1usMzaBvkGqeVZ0WG_ShvcOqIppsnUxnVHMyht4kEY12A3XAbMKy5tKrqrlHDJBUEUEk8-lLQxGLYVX7mut0TswOj2pNYECKFIdW4FCsP9y8hgON6PuV0dsoOfWtq3fTrP0ERWqR8xLqdBuN16G3Pe0OxZxF94xR64-Fx3t-ZBbulpdOvTFc_mYzoR3aw2TD_pN6up0vs71Yi6qZd_bJ6pOQ=w992-h945-s-no-gm?authuser=0\" width=\"128\" height=\"128\" aria-hidden=\"true\" style=\"margin-bottom:16px\" alt=\"Google\" class=\"CToWUd\" data-bit=\"iit\" jslog=\"138226; u014N:xr6bB; 53:WzAsMl0.\"><div style=\"font-family:'Google Sans',Roboto,RobotoDraft,Helvetica,Arial,sans-serif;border-bottom:thin solid #dadce0;color:rgba(0,0,0,0.87);line-height:32px;padding-bottom:24px;text-align:center;word-break:break-word\"><div style=\"font-size:24px\">Bem vindo ao Centro de Aprendizagem Biopark Educação(C.A.B.E)</div> </div><div style=\"font-family:Roboto-Regular,Helvetica,Arial,sans-serif;font-size:14px;color:rgba(0,0,0,0.87);line-height:20px;padding-top:20px;text-align:center\">Esperamos que nossos cursos enriqueçam o seu aprendizado e lhe ajudem a trilhar um caminho de conhecimento. Aproveite!\r\n"
				+ "</div></div></td><td width=\"8\" style=\"width:8px\"></td></tr></tbody>";
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
}
