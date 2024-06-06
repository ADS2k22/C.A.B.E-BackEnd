package br.com.biopark.vo;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import br.com.biopark.models.Curso;

@JsonPropertyOrder({"id", "nome", "url", "posicao", "curso"})
public class VideoVO extends RepresentationModel<VideoVO> implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("id")
	private Long key;
	private String nome;
	private String url;
	private int posicao;
	private Curso curso;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getPosicao() {
		return posicao;
	}

	public void setPosicao(int posicao) {
		this.posicao = posicao;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCapitulo(Curso curso) {
		this.curso = curso;
	}

	@Override
	public int hashCode() {
		return Objects.hash(curso, key, nome, posicao, url);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VideoVO other = (VideoVO) obj;
		return Objects.equals(curso, other.curso) && Objects.equals(key, other.key)
				&& Objects.equals(nome, other.nome) && posicao == other.posicao && Objects.equals(url, other.url);
	}
}
