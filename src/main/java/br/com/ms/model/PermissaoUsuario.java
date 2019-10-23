package br.com.ms.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "permissoes_usuario")
//@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class PermissaoUsuario implements Serializable {

	private static final long serialVersionUID = -5709354333197539256L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private boolean manutencaoUsuario;
	private boolean manutencaoRegistroEntrada;
	private boolean manutencaoEmpresa;
	private boolean manutencaoNotasFiscais;
	private boolean manutencaoAtendimento;
	private boolean manutencaoVisitante;
	private boolean edicaoRegistro;

	public PermissaoUsuario() {
	}

	public boolean isManutencaoUsuario() {
		return manutencaoUsuario;
	}

	public void setManutencaoUsuario(boolean manutencaoUsuario) {
		this.manutencaoUsuario = manutencaoUsuario;
	}

	public boolean getManutencaoRegistroEntrada() {
		return manutencaoRegistroEntrada;
	}

	public void setManutencaoRegistroEntrada(boolean manutencaoRegistroEntrada) {
		this.manutencaoRegistroEntrada = manutencaoRegistroEntrada;
	}

	public boolean getManutencaoEmpresa() {
		return manutencaoEmpresa;
	}

	public void setManutencaoEmpresa(boolean manutencaoEmpresa) {
		this.manutencaoEmpresa = manutencaoEmpresa;
	}

	public boolean getManutencaoNotasFiscais() {
		return manutencaoNotasFiscais;
	}

	public void setManutencaoNotasFiscais(boolean manutencaoNotasFiscais) {
		this.manutencaoNotasFiscais = manutencaoNotasFiscais;
	}

	public boolean getManutencaoAtendimento() {
		return manutencaoAtendimento;
	}

	public void setManutencaoAtendimento(boolean manutencaoAtendimento) {
		this.manutencaoAtendimento = manutencaoAtendimento;
	}

	public boolean getManutencaoVisitante() {
		return manutencaoVisitante;
	}

	public void setManutencaoVisitante(boolean manutencaoVisitante) {
		this.manutencaoVisitante = manutencaoVisitante;
	}

	public boolean isEdicaoRegistro() {
		return edicaoRegistro;
	}

	public void setEdicaoRegistro(boolean edicaoRegistro) {
		this.edicaoRegistro = edicaoRegistro;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		PermissaoUsuario other = (PermissaoUsuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PermissaoUsuario [id=" + id + "]";
	}

}
