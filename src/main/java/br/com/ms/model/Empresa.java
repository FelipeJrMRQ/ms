package br.com.ms.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name = "empresa")
//@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Empresa implements Serializable {

	private static final long serialVersionUID = -8080885590004442770L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(unique = true)
	private String cnpj;
	private String nome;
	private String status;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro;
	private int categoria;

	public Empresa() {
	}

	public Empresa(Long id, String cnpj, String nome, String status, Date dataCadastro, int categoria) {
		super();
		this.id = id;
		this.cnpj = cnpj;
		this.nome = nome;
		this.status = status;
		this.dataCadastro = dataCadastro;
		this.categoria = categoria;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj.replaceAll("[^0-9]", "");
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome.toUpperCase();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status.toUpperCase();
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
	/**
	 * * 1 CLIENTE
	 * 2 CLIENTE / FORNECEDOR
	 * 3 FORNECEDOR
	 * 
	 * @return
	 */
	public int getCategoria() {
		return categoria;
	}
	
	/**
	 * 1 CLIENTE
	 * 2 CLIENTE / FORNECEDOR
	 * 3 FORNECEDOR
	 * 
	 * @return
	 */
	public void setCategoria(int categoria) {
		this.categoria = categoria;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Empresa other = (Empresa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return id + " " + nome;
	}
}
