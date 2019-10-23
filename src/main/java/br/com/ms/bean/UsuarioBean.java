package br.com.ms.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;

import br.com.ms.dao.UsuarioDao;
import br.com.ms.model.PermissaoUsuario;
import br.com.ms.model.Usuario;
import br.com.ms.util.PermissoesUsuarios;
import br.com.ms.util.SegurancaSenha;

@ManagedBean
@ViewScoped
public class UsuarioBean implements Serializable {

	private static final long serialVersionUID = 6839773916969670805L;
	private PermissaoUsuario permissao;
	private Usuario usuario;
	private UsuarioDao usuarioDao;
	private String consultaUsuario;
	private List<Usuario> usuarios;
	private String senha;
	private String confirmaSenha;

	public UsuarioBean() {
		permissao = new PermissaoUsuario();
		usuario = new Usuario();
		usuarioDao = new UsuarioDao();
		usuarios = new ArrayList<>();
		senha = "";
		confirmaSenha = "";
	}

	/**
	 * 
	 * @param event
	 */

	public void usuarioSelecionado(ActionEvent event) {
		usuario = (Usuario) event.getComponent().getAttributes().get("usuarioSelecionado");
	}

	public void salvarUsuario() {
		if (usuario.getSenha().equals(usuario.getConfirmaSenha())) {
			try {
				usuario.setSenha(SegurancaSenha.getMD5(usuario.getSenha()));
				usuario.setConfirmaSenha(SegurancaSenha.getMD5(usuario.getConfirmaSenha()));
				usuario.setPermissoes(permissao);
				usuarioDao.salvarUsuario(usuario);
				limpar();
				Messages.addGlobalInfo("Usuário cadastrado com sucesso!");
			} catch (Exception erro) {
				Messages.addGlobalError(erro.getMessage());
			}
		} else {
			Messages.addGlobalError("As senhas não são iguais");
		}

	}

	public void alterarUsuario() {
		try {
			usuarioDao.salvarUsuario(usuario);
			usuarios = new ArrayList<>();
			limpar();
			Messages.addGlobalInfo("Usuário alterado com sucesso!");
		} catch (Exception erro) {
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void redefinirSenha() {
		try {
			usuario.setSenha(SegurancaSenha.getMD5("102030"));
			usuario.setConfirmaSenha(SegurancaSenha.getMD5("102030"));
			usuarioDao.alterarUsuario(usuario);
			Messages.addGlobalInfo("A senha foi alterada com sucesso!");
		} catch (Exception e) {
			Messages.addGlobalError("Erro ao redefinir senha " + e.getMessage());
		}
	}

	public void alterarSenha() {
		try {
			if (SegurancaSenha.getMD5(this.senha).equals(SegurancaSenha.getMD5(this.confirmaSenha))) {
				if (senha.length() < 4) {
					Messages.addGlobalError("Sua senha deve ter mais do que 3 digitos");
				} else {
					usuario = PermissoesUsuarios.getUsuario();
					usuario.setSenha(SegurancaSenha.getMD5(senha));
					usuario.setConfirmaSenha(SegurancaSenha.getMD5(confirmaSenha));
					usuarioDao.alterarUsuario(usuario);
					Messages.addGlobalInfo("Senha alterada com sucesso!");
					FacesContext.getCurrentInstance().getExternalContext().redirect("/ms/pages/registro.xhtml");
				}
			} else {
				Messages.addGlobalError("As senhas não são iguais!");
			}
		} catch (Exception er) {
			Messages.addGlobalError("Falha ao tentar alterar senha! " + er.getMessage());
		}
	}
	
	/**
	 * Se o usuario não quiser alterar a senha ele retornará a tela principal
	 */
	public void cancelar() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/ms/pages/registro.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void excluirUsuario(Usuario usuario) {
		try {
			usuarioDao.excluirUsuario(usuario);
			Messages.addGlobalInfo("Usuário excluído com sucesso!");
		} catch (Exception erro) {
			Messages.addGlobalError(erro.getCause().getMessage());
		}
	}

	public void buscarUsuarioPorNome() {
		try {
			this.usuarios = usuarioDao.buscarUsuarioPorNome(consultaUsuario);
		} catch (Exception erro) {
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void novo() {
		usuario = new Usuario();
	}

	private void limpar() {
		permissao = new PermissaoUsuario();
		usuario = new Usuario();
		usuarioDao = new UsuarioDao();
		usuarios = new ArrayList<>();
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getConsultaUsuario() {
		return consultaUsuario;
	}

	public void setConsultaUsuario(String consultaUsuario) {
		this.consultaUsuario = consultaUsuario.toUpperCase();
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public PermissaoUsuario getPermissao() {
		return permissao;
	}

	public void setPermissao(PermissaoUsuario permissao) {
		this.permissao = permissao;
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

}
