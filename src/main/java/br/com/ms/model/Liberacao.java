package br.com.ms.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Liberacao implements Serializable{

	private static final long serialVersionUID = 6842914312803503814L;

	public Liberacao() {
	}
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private long id;
	
	@OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH} , fetch = FetchType.EAGER)
	private Registro entrada;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Registro saida;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataLiberacao;
	
	@ManyToOne(cascade ={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH})
	private Atendimento atendimento;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Usuario usuario;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public Date getDataLiberacao() {
		return dataLiberacao;
	}

	public void setDataLiberacao(Date dataLiberacao) {
		this.dataLiberacao = dataLiberacao;
	}

	public Atendimento getAtendimento() {
		return atendimento;
	}

	public void setAtendimento(Atendimento atendimento) {
		this.atendimento = atendimento;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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
		Liberacao other = (Liberacao) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Liberacao [id=" + id + "]";
	}
}
