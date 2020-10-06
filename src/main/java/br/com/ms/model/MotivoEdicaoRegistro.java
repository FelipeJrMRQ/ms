package br.com.ms.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class MotivoEdicaoRegistro  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 973420759242665699L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String motivo;
	@OneToOne
	private Registro registro;
	@OneToOne
	private Usuario responsavel;
	
	public MotivoEdicaoRegistro() {
	
	}

	public MotivoEdicaoRegistro( String motivo, Registro registro, Usuario responsavel) {
		super();
		this.motivo = motivo.toUpperCase();
		this.registro = registro;
		this.responsavel = responsavel;
	}



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo.toUpperCase();
	}

	public Registro getRegistro() {
		return registro;
	}

	public void setRegistro(Registro registro) {
		this.registro = registro;
	}

	public Usuario getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
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
		MotivoEdicaoRegistro other = (MotivoEdicaoRegistro) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
