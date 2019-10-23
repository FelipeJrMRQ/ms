package br.com.ms.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name = "notas_registro")
//@Audited
public class NotaRegistro implements Serializable {

	private static final long serialVersionUID = 6378559214297605178L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String numeroNfe;
	private double valor;
	private String cnpj;
	private String nome;
	private Date emissao;
	private String chave;
	@ManyToOne
	private Registro registro;

	public NotaRegistro() {
	}

	public NotaRegistro(Long id, String numeroNfe, Registro registro) {
		super();
		this.id = id;
		this.numeroNfe = numeroNfe;
		this.registro = registro;
	}

	public Registro getRegistro() {
		return registro;
	}

	public void setRegistro(Registro registro) {
		this.registro = registro;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumeroNfe() {
		return numeroNfe;
	}

	public void setNumeroNfe(String numeroNfe) {
		this.numeroNfe = numeroNfe;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getEmissao() {
		return emissao;
	}

	public void setEmissao(Date emissao) {
		this.emissao = emissao;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}
}
