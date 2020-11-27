package br.com.ms.util;

import org.omnifaces.util.Faces;

import br.com.ms.bean.LoginBean;
import br.com.ms.model.Usuario;

public class PermissoesUsuarios {
	private static Usuario usuario;
	private static LoginBean loginBean;
	
	public static Usuario getUsuario() {
		try {
			loginBean = Faces.getSessionAttribute("loginBean");
			usuario = loginBean.getUsuarioLogado();
			return usuario;
		}catch (Exception e) {
			throw e;
		}
	}
	
}
