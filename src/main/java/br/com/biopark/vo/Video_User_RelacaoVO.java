package br.com.biopark.vo;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import br.com.biopark.models.User;
import br.com.biopark.models.Video;

@JsonPropertyOrder({"id", "video", "user", "concluido"})
public class Video_User_RelacaoVO extends RepresentationModel<Video_User_RelacaoVO> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("id")
	private Long key;
	private Video video;
	private User user;
	private boolean concluido;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isConcluido() {
		return concluido;
	}

	public void setConcluido(boolean concluido) {
		this.concluido = concluido;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(concluido, key, user, video);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Video_User_RelacaoVO other = (Video_User_RelacaoVO) obj;
		return concluido == other.concluido && Objects.equals(key, other.key) && Objects.equals(user, other.user)
				&& Objects.equals(video, other.video);
	}
}
