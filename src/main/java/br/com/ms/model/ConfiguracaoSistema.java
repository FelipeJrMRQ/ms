package br.com.ms.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import br.com.ms.util.Seguranca;

@Entity(name = "configuracaosistema")
public class ConfiguracaoSistema implements Serializable {

	private static final long serialVersionUID = -4221641655119962898L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private boolean atualizacaoNfe;
	private boolean ativarProgramador;
	private String caminhoCertificado;
	private String senhaCertificado;
	private String senhaServidor;
	private String chaveEncrypt;
	private String dominio;
	private String usuario;

	public ConfiguracaoSistema(Long id, boolean atualizacaoNfe, boolean ativarProgramador, String caminhoCertificado, String senhaCertificado, String senhaServidor, String chaveEncrypt) {
		super();
		this.id = id;
		this.atualizacaoNfe = atualizacaoNfe;
		this.ativarProgramador = ativarProgramador;
		this.caminhoCertificado = caminhoCertificado;
		this.senhaCertificado = senhaCertificado;
		this.senhaServidor = senhaServidor;
		this.chaveEncrypt = chaveEncrypt;
	}

	public ConfiguracaoSistema() {

	}

	public boolean isAtualizacaoNfe() {
		return atualizacaoNfe;
	}

	public void setAtualizacaoNfe(boolean atualizacaoNfe) {
		this.atualizacaoNfe = atualizacaoNfe;
	}

	public boolean isAtivarProgramador() {
		return ativarProgramador;
	}

	public void setAtivarProgramador(boolean ativarProgramador) {
		this.ativarProgramador = ativarProgramador;
	}

	public String getCaminhoCertificado() {
		return caminhoCertificado;
	}

	public void setCaminhoCertificado(String caminhoCertificado) {
		this.caminhoCertificado = caminhoCertificado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSenhaCertificado() {
		return senhaCertificado;
	}
	
	

	public String getDominio() {
		return dominio;
	}

	public void setDominio(String dominio) {
		this.dominio = dominio;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public void setSenhaCertificado(String senhaCertificado) {
		try {
			this.senhaCertificado = new String(Seguranca.encrypt(new String(senhaCertificado), Seguranca.getConfig().getChaveEncrypt()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getSenhaServidor() {
		return senhaServidor;
	}

	public void setSenhaServidor(String senhaServidor) {
		try {
			this.senhaServidor = new String(Seguranca.encrypt(new String(senhaServidor), Seguranca.getConfig().getChaveEncrypt()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getChaveEncrypt() {
		return chaveEncrypt;
	}

	public void setChaveEncrypt(String chaveEncrypt) {
		this.chaveEncrypt = chaveEncrypt;
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
		ConfiguracaoSistema other = (ConfiguracaoSistema) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
