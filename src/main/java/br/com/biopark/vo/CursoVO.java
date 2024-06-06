package br.com.biopark.vo;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import br.com.biopark.models.Categoria;

@JsonPropertyOrder({"id", "nome", "carga_horaria", "categoria", "trilha", "feedback"})
public class CursoVO extends RepresentationModel<CursoVO> implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("id")
	private Long key;
	private String nome;
	private int carga_horaria;
	private Categoria categoria;
	private String trilha;
	private int feedback;
	
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

	public int getCarga_horaria() {
		return carga_horaria;
	}

	public void setCarga_horaria(int carga_horaria) {
		this.carga_horaria = carga_horaria;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public String getTrilha() {
		return trilha;
	}

	public void setTrilha(String trilha) {
		this.trilha = trilha;
	}

	public int getFeedback() {
		return feedback;
	}

	public void setFeedback(int feedback) {
		this.feedback = feedback;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(carga_horaria, categoria, feedback, key, nome, trilha);
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
		CursoVO other = (CursoVO) obj;
		return carga_horaria == other.carga_horaria && Objects.equals(categoria, other.categoria)
				&& feedback == other.feedback && Objects.equals(key, other.key) && Objects.equals(nome, other.nome)
				&& Objects.equals(trilha, other.trilha);
	}
}
