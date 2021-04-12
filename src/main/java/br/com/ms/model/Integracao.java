package br.com.ms.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Integracao implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6561660032534710904L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private boolean status;
	private String cronParametros;
	private String hostHttp;
	private String localArquivo;

	public Integracao() {
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getCronParametros() {
		return cronParametros;
	}

	public void setCronParametros(String cronParametros) {
		this.cronParametros = cronParametros;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getHostHttp() {
		return hostHttp;
	}

	public void setHostHttp(String hostHttp) {
		this.hostHttp = hostHttp;
	}

	public String getLocalArquivo() {
		return localArquivo;
	}

	public void setLocalArquivo(String localArquivo) {
		this.localArquivo = localArquivo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cronParametros == null) ? 0 : cronParametros.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (status ? 1231 : 1237);
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
		Integracao other = (Integracao) obj;
		if (cronParametros == null) {
			if (other.cronParametros != null)
				return false;
		} else if (!cronParametros.equals(other.cronParametros))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Integracao [id=" + id + "]";
	}

}
