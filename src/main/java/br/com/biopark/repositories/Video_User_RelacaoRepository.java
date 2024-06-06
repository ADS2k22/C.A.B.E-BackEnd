package br.com.biopark.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.biopark.models.Video_User_Relacao;

public interface Video_User_RelacaoRepository extends JpaRepository<Video_User_Relacao, Long> {
	
	@Query(nativeQuery = true, value = "SELECT * FROM video_user_relacao WHERE video_id = :videoid AND user_id = :userid")
	public Video_User_Relacao findByUserAndVideo(@Param(value = "videoid") Long videoid, @Param(value = "userid") Long userid);
	
	@Query(nativeQuery = true, value = "SELECT vur.id, vur.concluido, vur.video_id, vur.user_id FROM Video_User_Relacao AS vur "
			+ "JOIN video AS v ON vur.video_id = v.id "
			+ "WHERE vur.user_id = :userid AND v.curso_id = :cursoid")
	public List<Video_User_Relacao> findAllByCursoId(@Param(value = "userid") Long userid, @Param(value = "cursoid") Long cursoid);
}