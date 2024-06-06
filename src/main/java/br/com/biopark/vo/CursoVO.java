package br.com.biopark.vo;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import br.com.biopark.models.Categoria;

@JsonPropertyOrder({"id", "nome", "carga_horaria", "categoria"})
public class CursoVO extends RepresentationModel<CursoVO> implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("id")
	private Long key;
	private String nome;
	private int carga_horaria;
	private Categoria cagetoria;

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

	public Categoria getCagetoria() {
		return cagetoria;
	}

	public void setCagetoria(Categoria cagetoria) {
		this.cagetoria = cagetoria;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cagetoria, carga_horaria, key, nome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CursoVO other = (CursoVO) obj;
		return Objects.equals(cagetoria, other.cagetoria) && carga_horaria == other.carga_horaria
				&& Objects.equals(key, other.key) && Objects.equals(nome, other.nome);
	}
}
