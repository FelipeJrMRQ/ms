package br.com.ms.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class LiberacaoVisitante implements Serializable {

	private static final long serialVersionUID = 8117259241395868712L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataLiberacao;

	@OneToOne(fetch = FetchType.EAGER)
	private Registro entrada;

	@OneToOne(fetch = FetchType.EAGER)
	private Registro saida;

	@OneToOne(fetch = FetchType.EAGER)
	private Usuario usuario;

	public LiberacaoVisitante() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDataLiberacao() {
		return dataLiberacao;
	}

	public void setDataLiberacao(Date dataLiberacao) {
		this.dataLiberacao = dataLiberacao;
	}

	public Registro getEntrada() {
		return entrada;
	}

	public void setEntrada(Registro entrada) {
		this.entrada = entrada;
	}

	public Registro getSaida() {
		return saida;
	}

	public void setSaida(Registro saida) {
		this.saida = saida;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return "LiberacaoVisitante [id=" + id + "]";
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
		LiberacaoVisitante other = (LiberacaoVisitante) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
