package br.com.ms.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name = "liberacaodeuso")
public class LiberacaoDeUso implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8829079573328479047L;

	@Id
	private int id;
	
	private String chaveDeAcesso;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtValidacao;
	
	public LiberacaoDeUso() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getChaveDeAcesso() {
		return chaveDeAcesso;
	}

	public void setChaveDeAcesso(String chaveDeAcesso) {
		this.chaveDeAcesso = chaveDeAcesso;
	}

	public Date getDtValidacao() {
		return dtValidacao;
	}

	public void setDtValidacao(Date dtValidacao) {
		this.dtValidacao = dtValidacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LiberacaoDeUso other = (LiberacaoDeUso) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LiberacaoDeUso [id=" + id + "]";
	}	
}
