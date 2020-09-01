package br.com.ms.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity

public class Atendimento implements Serializable {

	private static final long serialVersionUID = 3212929252039535118L;

	public Atendimento() {
	}

	public Atendimento(long id, Date data_inicio, Date data_fim, Registro registro, String status) {
		super();
		this.id = id;
		this.data_inicio = data_inicio;
		this.data_fim = data_fim;
		this.registro = registro;
		this.status = status;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date data_inicio;

	@Temporal(TemporalType.TIMESTAMP)
	private Date data_fim;

	@ManyToOne
	private Registro registro;

	@ManyToOne
	private Usuario usuario_inicio;

	@ManyToOne
	private Usuario usuario_fim;

	private String status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getData_inicio() {
		return data_inicio;
	}

	public void setData_inicio(Date data_inicio) {
		this.data_inicio = data_inicio;
	}

	public Date getData_fim() {
		return data_fim;
	}

	public void setData_fim(Date data_fim) {
		this.data_fim = data_fim;
	}

	public Registro getRegistro() {
		return registro;
	}

	public void setRegistro(Registro registro) {
		this.registro = registro;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Usuario getUsuario_inicio() {
		return usuario_inicio;
	}

	public void setUsuario_inicio(Usuario usuario_inicio) {
		this.usuario_inicio = usuario_inicio;
	}

	public Usuario getUsuario_fim() {
		return usuario_fim;
	}

	public void setUsuario_fim(Usuario usuario_fim) {
		this.usuario_fim = usuario_fim;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		Atendimento other = (Atendimento) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Atendimento [id=" + id + "]";
	}

}
