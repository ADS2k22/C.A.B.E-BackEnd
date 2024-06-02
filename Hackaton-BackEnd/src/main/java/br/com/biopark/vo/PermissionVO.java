package br.com.biopark.vo;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "description"})
public class PermissionVO extends RepresentationModel<UserVO> implements Serializable{

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long key;
	private String description;

	public PermissionVO() {}

	public PermissionVO(Long key, String description) {
		this.key = key;
		this.description = description;
	}


	public Long getKey() {
		return key;
	}


	public void setKey(Long id) {
		this.key = id;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, key);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PermissionVO other = (PermissionVO) obj;
		return Objects.equals(description, other.description) && Objects.equals(key, other.key);
	}
}
