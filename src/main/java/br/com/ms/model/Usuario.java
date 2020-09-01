package br.com.ms.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
//@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Usuario implements Serializable {

	private static final long serialVersionUID = -3279003996421078158L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int codigo;

	@Column(nullable = false, length = 35, unique = true)
	private String nome;

	@OneToOne(cascade = CascadeType.ALL)
	private PermissaoUsuario permissoes;


	@Column(nullable = false, length = 150)
	private String senha;
	
	private String confirmaSenha;
	
	private String codigoProgramador;
	
	public Usuario() {
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public PermissaoUsuario getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(PermissaoUsuario permissoes) {
		this.permissoes = permissoes;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome.toUpperCase();
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha.toUpperCase();
	}

	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha.toUpperCase();
	}

	public String getCodigoProgramador() {
		return codigoProgramador;
	}

	public void setCodigoProgramador(String codigoProgramador) {
		this.codigoProgramador = codigoProgramador.toUpperCase();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + codigo;
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
		Usuario other = (Usuario) obj;
		if (codigo != other.codigo)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Usuario [codigo=" + codigo + "]";
	}

}
