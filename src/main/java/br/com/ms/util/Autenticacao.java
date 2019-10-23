package br.com.ms.util;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.omnifaces.util.Faces;

import br.com.ms.bean.LoginBean;

@SuppressWarnings("serial")
public class Autenticacao implements PhaseListener {

	@Override
	public void afterPhase(PhaseEvent arg0) {
		String paginaAtual = Faces.getViewId();
		boolean paginaDeLogin = paginaAtual.contains("login.xhtml");

		if (!paginaDeLogin) {
			LoginBean loginBean = Faces.getSessionAttribute("loginBean");

			if (loginBean == null) {
				try {
					FacesContext.getCurrentInstance().getExternalContext().redirect("/ms/login/login.xhtml");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
			if (PermissoesUsuarios.getUsuario() == null) {
				try {
					FacesContext.getCurrentInstance().getExternalContext().redirect("/ms/login/login.xhtml");
				} catch (IOException erro) {
					erro.printStackTrace();
				}
			}
		}

	}

	@Override
	public void beforePhase(PhaseEvent arg0) {
	
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
