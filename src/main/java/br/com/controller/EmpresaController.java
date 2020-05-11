package br.com.controller;

import java.util.List;

import br.com.ms.dao.EmpresaDao;
import br.com.ms.model.Empresa;

public class EmpresaController {
	
	
	private EmpresaDao empresaDao;
	
	
	public EmpresaController() {
		empresaDao = new EmpresaDao();
	}
	
	public List<Empresa> consultarEmpresaPorNome(String nomeEmpresa){
		try {
			return empresaDao.consultarEmpresa(nomeEmpresa);
		}catch(Exception ex) {
			throw ex;
		}
	}
}
