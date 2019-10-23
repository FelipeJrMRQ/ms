package br.com.ms.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.omnifaces.util.Messages;

import br.com.ms.dao.LiberacaoDeUsoDao;
import br.com.ms.dao.UsuarioDao;
import br.com.ms.model.LiberacaoDeUso;
import br.com.ms.model.Usuario;
import br.com.ms.seguranca.SegurancaDeUso;
import br.com.ms.util.HoraDaInternet;
import br.com.ms.util.KeyValidator;
import br.com.ms.util.SegurancaSenha;

@ManagedBean
@SessionScoped
public class LoginBean implements Serializable{
	
	private static final long serialVersionUID = -9053845422907217393L;
	private Usuario usuario;
	private Usuario usuarioLogado;
	private UsuarioDao usuarioDao;
	private LiberacaoDeUso liberacaoDeUso;
	private LiberacaoDeUsoDao liberacaoDeUsoDao;
	private String chaveLiberacao;

	public LoginBean() {
		this.usuario = new Usuario();
		this.usuarioDao = new UsuarioDao();
		this.liberacaoDeUso = new LiberacaoDeUso();
		this.liberacaoDeUsoDao = new LiberacaoDeUsoDao();
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void salvarChaveDeAcesso() {
		try {
			if (SegurancaDeUso.getChaveAcesso().equals(SegurancaSenha.getMD5(chaveLiberacao))) {
				liberacaoDeUso.setId(1);
				liberacaoDeUso.setChaveDeAcesso(SegurancaSenha.getMD5(chaveLiberacao));
				liberacaoDeUso.setDtValidacao(HoraDaInternet.getHora());
				liberacaoDeUsoDao.salvarLiberacao(liberacaoDeUso);
				Messages.addGlobalInfo("Validação de software efetuada com sucesso!");
				FacesContext.getCurrentInstance().getExternalContext().redirect("/ms/pages/registro.xhtml");
			}else {
				Messages.addGlobalError("Chave de liberação inválida!");
			}
		} catch (Exception e) {
			Messages.addGlobalError("Chave de liberação inválida!");
		}
	}

	@SuppressWarnings({ "deprecation", "static-access" })
	private void validarAcesso() {
		try {
			if (liberacaoDeUsoDao.consultaLiberacaoDeUso().isEmpty()) {
				FacesContext.getCurrentInstance().getExternalContext().redirect("/ms/pages/chaveDeAcesso.xhtml");
			} else {
				GregorianCalendar calendar = new GregorianCalendar();
				List<LiberacaoDeUso> lista = new ArrayList<>();
				lista = liberacaoDeUsoDao.consultaLiberacaoDeUso();
				for (LiberacaoDeUso ls : lista) {
					if(ls.getDtValidacao().getMonth() == calendar.get(calendar.MONTH)) {
						FacesContext.getCurrentInstance().getExternalContext().redirect("/ms/pages/registro.xhtml");
					}else {
						//Realiza a validação do software via API validator
						if(!KeyValidator.getKey().equals(ls.getChaveDeAcesso())) {
							FacesContext.getCurrentInstance().getExternalContext().redirect("/ms/pages/chaveDeAcesso.xhtml");
						}else {
							FacesContext.getCurrentInstance().getExternalContext().redirect("/ms/pages/registro.xhtml");
						}
					}
				}
			}
		} catch (Exception e) {
			
		}
	}

	public void autenticar() {
		try {
			usuario.setSenha(SegurancaSenha.getMD5(usuario.getSenha()));
			if (usuarioDao.autenticarUsuario(this.usuario) != null) {
				usuarioLogado = usuarioDao.autenticarUsuario(this.usuario);
				if (usuarioLogado.getSenha().equals(SegurancaSenha.getMD5("102030"))) {
					FacesContext.getCurrentInstance().getExternalContext().redirect("/ms/pages/alterar_senha_padrao.xhtml");
				} else {
					validarAcesso();
				}
			} else {
				Messages.addGlobalError("Usuário ou senha inválidos!");
			}
		} catch (Exception erro) {
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void logout() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/ms/login/login.xhtml");
			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getChaveLiberacao() {
		return chaveLiberacao;
	}

	public void setChaveLiberacao(String chaveLiberacao) {
		this.chaveLiberacao = chaveLiberacao;
	}

	public LiberacaoDeUso getLiberacaoDeUso() {
		return liberacaoDeUso;
	}

	public void setLiberacaoDeUso(LiberacaoDeUso liberacaoDeUso) {
		this.liberacaoDeUso = liberacaoDeUso;
	}

}
