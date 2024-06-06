package br.com.biopark.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.biopark.dtos.CursoConclusaoDTO;
import br.com.biopark.dtos.FiltrosCursoDTO;
import br.com.biopark.exceptions.MinhaException;
import br.com.biopark.mapper.Mapper;
import br.com.biopark.models.User;
import br.com.biopark.models.Video_User_Relacao;
import br.com.biopark.repositories.CursoRepository;
import br.com.biopark.repositories.CustomRepository;
import br.com.biopark.repositories.UserRepository;
import br.com.biopark.repositories.VideoRepository;
import br.com.biopark.repositories.Video_User_RelacaoRepository;
import br.com.biopark.vo.CursoVO;
import br.com.biopark.vo.Video_User_RelacaoVO;
import jakarta.mail.MessagingException;

@Service
public class CursoService {

	@Autowired
	CursoRepository repository;
	@Autowired
	VideoRepository videoRepository;
	@Autowired
	Video_User_RelacaoRepository vurRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	CustomRepository customRepository;
	@Autowired
	Video_User_RelacaoService vurService;
	@Autowired
	EmailService emailService;
	
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
				Video_User_Relacao vur = vurRepository.findByUserAndVideo(video, userId);
				boolean conc = vur.isConcluido();
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
				Video_User_Relacao vur = vurRepository.findByUserAndVideo(video, userId);
				boolean conc = vur.isConcluido();
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
				Video_User_Relacao vur = vurRepository.findByUserAndVideo(video, userId);
				boolean conc = vur.isConcluido();
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
	
	public List<Video_User_RelacaoVO> findAllByCursoId(Long cursoId, Long adminId){
		return vurService.findAllByCursoId(cursoId, adminId);
	}
	
	public Video_User_RelacaoVO marcarVideo(Long idVideo, Long idUser) {
		Video_User_Relacao vur = vurRepository.findByUserAndVideo(idVideo, idUser);
		if(vur.isConcluido()) {
			vur.setConcluido(false);
		} else {
			vur.setConcluido(true);
			boolean isConcluded = true;
			List<Long> videos = videoRepository.findAllByCurso(vur.getVideo().getCurso().getId());
			List<Boolean> conclusoes = new ArrayList<>();
			for (Long video : videos) {
				Video_User_Relacao vur2 = vurRepository.findByUserAndVideo(video, idUser);
				boolean conc = vur2.isConcluido();
				conclusoes.add(conc);
			}
			for (Boolean concc : conclusoes) {
				if (concc == false) isConcluded = false;
			}
			if (isConcluded == true) {
				String to = vur.getUser().getUsername();
				String subject = "Parabéns " + vur.getUser().getNome() + "! Você concluiu um curso com sucesso!";
				String htmlbody = """
			            <html>
			            <body>
			                <div style="border-style:solid;border-width:thin;border-color:#dadce0;border-radius:8px;padding:40px 20px" align="center">
			                    <div style="font-family:'Google Sans',Roboto,RobotoDraft,Helvetica,Arial,sans-serif;border-bottom:thin solid #dadce0;color:rgba(0,0,0,0.87);line-height:32px;padding-bottom:24px;text-align:center;word-break:break-word">
			                        <div style="font-size:24px">O seu certificado foi gerado e está a tua espera no C.A.B.E</div>
			                    </div>
			                    <div style="font-family:Roboto-Regular,Helvetica,Arial,sans-serif;font-size:14px;color:rgba(0,0,0,0.87);line-height:20px;padding-top:20px;text-align:center">
			                        Esperamos que nossos cursos enriqueçam ainda mais o seu aprendizado e lhe ajudem a trilhar um caminho de conhecimento. Boa sorte!
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
			}
		}
		return Mapper.parseObject(vurRepository.save(vur), Video_User_RelacaoVO.class);
	}
	
	public void enviarEmail(Long idUser) {
		if (idUser == null) throw new MinhaException("Id não pode ser nulo!");
		User user = userRepository.findById(idUser).orElseThrow(() -> new MinhaException("Usuário não encontrado!"));
		String to = user.getUsername();
		String subject = "Olá " + user.getNome() + "! Você foi inscrito em um evento!";
		String htmlbody = """
	            <html>
	            <body>
	                <div style="border-style:solid;border-width:thin;border-color:#dadce0;border-radius:8px;padding:40px 20px" align="center">
	                    <div style="font-family:'Google Sans',Roboto,RobotoDraft,Helvetica,Arial,sans-serif;border-bottom:thin solid #dadce0;color:rgba(0,0,0,0.87);line-height:32px;padding-bottom:24px;text-align:center;word-break:break-word">
	                        <div style="font-size:24px">Você foi inscrito em evento do biopark!</div>
	                    </div>
	                    <div style="font-family:Roboto-Regular,Helvetica,Arial,sans-serif;font-size:14px;color:rgba(0,0,0,0.87);line-height:20px;padding-top:20px;text-align:center">
	                        Lembre-se: não se atrase e leve o caderno para fazer anotações. Esperamos você lá!
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
	}
}
