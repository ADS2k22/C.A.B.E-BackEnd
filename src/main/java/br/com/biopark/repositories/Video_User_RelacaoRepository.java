package br.com.biopark.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.biopark.models.Video_User_Relacao;

public interface Video_User_RelacaoRepository extends JpaRepository<Video_User_Relacao, Long> {
	
	@Query(nativeQuery = true, value = "SELECT concluido FROM video_user_relacao WHERE video_id = :videoid AND user_id = :userid")
	public boolean findByUserAndVideo(@Param(value = "videoid") Long videoid, @Param(value = "userid") Long userid);
}