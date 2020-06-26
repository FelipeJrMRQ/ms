package br.com.controller;

import br.com.ms.dao.UsuarioDao;
import br.com.ms.model.Usuario;

public class UsuarioController {
	
	private UsuarioDao usuarioDao;
	private Usuario usuario;
	
	public UsuarioController() {
		//System.out.println("Usuario controller");
		usuarioDao = new  UsuarioDao();
		usuario = new Usuario();
	}
	
	public Usuario consultaProgramadorPeloCodigo(String codigo) throws Exception {
		usuario = usuarioDao.buscarPorCodigoProgramador(codigo);
		if(usuario == null) {
			throw new Exception("Este codigo de programador não existe");
		}else {
			return usuario;
		}
	}

}
