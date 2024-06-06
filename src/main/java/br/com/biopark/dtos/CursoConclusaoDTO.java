package br.com.biopark.dtos;

import br.com.biopark.vo.CursoVO;

public class CursoConclusaoDTO {

	private boolean concluido;
	private CursoVO curso;
	public boolean isConcluido() {
		return concluido;
	}
	public void setConcluido(boolean concluido) {
		this.concluido = concluido;
	}
	public CursoVO getCurso() {
		return curso;
	}
	public void setCurso(CursoVO curso) {
		this.curso = curso;
	}
}
