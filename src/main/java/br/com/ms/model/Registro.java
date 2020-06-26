package br.com.ms.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name = "Registro")
public class Registro implements Serializable {

	private static final long serialVersionUID = 5634188710473732871L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne(fetch = FetchType.EAGER)
	private Visitante visitante;

	private String placaVeiculo;

	@ManyToOne
	private Empresa empresa;

	@Temporal(TemporalType.TIMESTAMP)
	private Date data;

	@ManyToOne
	private Usuario usuario;

	private String tipo;

	@Column
	private String status;
	

	@OneToMany(mappedBy = "registro", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)//Aqui foi alterada esta propriedade [fetch = FetchType.LAZY]
	private List<NotaRegistro> notas;
	
	public Registro() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Visitante getPrestadorDeServico() {
		return visitante;
	}

	public void setPrestadorDeServico(Visitante visitante) {
		this.visitante = visitante;
	}

	public String getPlacaVeiculo() {
		return placaVeiculo;
	}

	public void setPlacaVeiculo(String placaVeiculo) {
		this.placaVeiculo = placaVeiculo.toUpperCase();
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * ABERTO - Ao gerar um regitro de entrada
	 * 
	 * LIBERADO - Ao gerar liberação para saida;
	 * 
	 * SAIDA - Após conferir liberaç ão
	 * 
	 * @return
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * ENTRADA - Ao gerar um regitro de entrada
	 * 
	 * LIBERADO - Ao gerar liberação para saida;
	 * 
	 * SAIDA - Após conferir liberação
	 * 
	 * @param tipo
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public List<NotaRegistro> getNotas() {
		return notas;
	}

	public void setnotas(List<NotaRegistro> notas2) {
		this.notas = notas2;
	}

	public String getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
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
		Registro other = (Registro) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RegistroBean [id=" + id + "]";
	}

}
