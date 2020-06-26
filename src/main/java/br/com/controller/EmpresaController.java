package br.com.controller;

import java.util.List;

import br.com.ms.dao.EmpresaDao;
import br.com.ms.model.Empresa;
import br.com.ms.validacao.ValidaCNPJ;

public class EmpresaController {

	private EmpresaDao empresaDao;

	public EmpresaController() {
		//System.out.println("Controller");
		empresaDao = new EmpresaDao();
	}

	public List<Empresa> consultarEmpresaPorNome(String nomeEmpresa) {
		try {
			return empresaDao.consultarEmpresa(nomeEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	public void excluirEmpresa(Empresa empresa) throws Exception {
		try {
			validaExclusaoEmpresa(empresa);
			empresaDao.excluirEmpresa(empresa);
		} catch (Exception e) {
			throw e;
		}
	}

	public void adicionarEmpresa(String cnpj, Empresa empresa) throws Exception {
		try {
			validaCNPJ(cnpj);
			consultaCNPJExistente(cnpj);
			empresa.setCnpj(cnpj);
			empresaDao.salvarEmpresa(empresa);
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void alterarEmpresa(Empresa empresa) {
		try {
			empresaDao.alterarEmpresa(empresa);
		} catch (Exception e) {
			throw e;
		}
	}
	
	private void validaExclusaoEmpresa(Empresa e) throws Exception {
		if (empresaDao.consultaRegistroEmpresaPorId(e.getId()) != null) {
			throw new Exception("Este registro possui vínculos que impossibilitam sua exclusão!");
		}
	}

	private void validaCNPJ(String c) throws Exception {
		if (!ValidaCNPJ.isCNPJ(c)) {
			throw new Exception("O CNPJ digitado é inválido!");
		}
	}

	private void consultaCNPJExistente(String cnpj) throws Exception {
		Empresa empresa = new Empresa();
		empresa = empresaDao.consultarEmpresaPeloCnpj(cnpj);
		if (empresa != null) {
			if (empresa.getCnpj().equals(cnpj)) {
			throw new Exception("Esta empresa já está cadastrada no sistema!");
			}
		}
	}
	
	public List<Empresa> consultaEmpresaPeloNome(String nomeEmpresa) {
		try {
			return empresaDao.consultarEmpresa(nomeEmpresa);
		} catch (RuntimeException e) {
			throw e;
		}
	}

	public Empresa consultaEmpresaPeloCnpj(String cnpj) {
		try {
			 return empresaDao.consultarEmpresaPeloCnpj(cnpj);
		} catch (Exception e) {
			throw e;
		}
	}
}
