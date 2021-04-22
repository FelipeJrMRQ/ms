package br.com.ms.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity(name = "visitante")
//@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Visitante implements Serializable {

	private static final long serialVersionUID = -651071507822487529L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String nome;
	private boolean status;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH })
	@JoinTable(name = "prestador_empresa", joinColumns = { @JoinColumn(name = "prestador_id") }, inverseJoinColumns = { @JoinColumn(name = "empresa_id") })
	private List<Empresa> empresas;

	private String cpf;

	@Column(nullable = false)
	private String tipo;

	@Column
	private String rg;

	private boolean naoMonitorado;

	public Visitante() {
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome.toUpperCase();
	}

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf.replaceAll("[^0-9]", "");
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg.replaceAll("[^aA-zZ 0-9]", "").toUpperCase();
	}

	public boolean isNaoMonitorado() {
		return naoMonitorado;
	}

	public void setNaoMonitorado(boolean naoMonitorado) {
		this.naoMonitorado = naoMonitorado;
	}

	@Override
	public String toString() {
		return "Visitante [id=" + id + "]";
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
		Visitante other = (Visitante) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
