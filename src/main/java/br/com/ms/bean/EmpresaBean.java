package br.com.ms.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;

import br.com.ms.dao.EmpresaDao;
import br.com.ms.model.Empresa;
import br.com.ms.util.HoraDaInternet;
import br.com.ms.validacao.ValidaCNPJ;

@ManagedBean
@ViewScoped
public class EmpresaBean implements Serializable {

	private static final long serialVersionUID = 2701635752069790578L;
	private Empresa empresa;
	private EmpresaDao empresaDao;
	private List<Empresa> empresas;
	private String consulta;
	private String cnpj;

	public EmpresaBean() {
		this.empresa = new Empresa();
		this.empresaDao = new EmpresaDao();
		this.empresas = new ArrayList<>();
	}

	public void salvarEmpresa() throws Exception {
		try {
			validaCNPJ(cnpj);
			consultaCNPJExistente(cnpj);
			empresa.setDataCadastro(HoraDaInternet.getHora());
			empresa.setCnpj(cnpj);
			empresaDao.salvarEmpresa(empresa);
			Messages.addGlobalInfo("Empresa cadastrada com sucesso!");
			limparComponentes();
		} catch (Throwable erro) {
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void alterarEmpresa() {
		try {
			validaCNPJ(cnpj);
			empresa.setCnpj(cnpj);
			empresaDao.alterarEmpresa(empresa);
			Messages.addGlobalInfo("Empresa alterada com sucesso!");
			limparComponentes();
		} catch (Exception erro) {
			Messages.addGlobalError(erro.getMessage());
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
		if(empresa != null) {
			if(empresa.getCnpj().equals(cnpj)) {
				throw new Exception("Esta empresa já está cadastrada no sistema!");
			}
		}
	}

	public void excluirEmpresa(ActionEvent event) {
		try {
			validaExclusaoEmpresa((Empresa) event.getComponent().getAttributes().get("empresaSelecionada"));
			empresaDao.excluirEmpresa((Empresa) event.getComponent().getAttributes().get("empresaSelecionada"));
			Messages.addGlobalInfo("Empresa excluída com sucesso!");
			limparComponentes();
		} catch (Exception erro) {
			Messages.addGlobalError(erro.getMessage());
		}
	}

	private void validaExclusaoEmpresa(Empresa e) throws Exception {
		if (empresaDao.consultaRegistroEmpresaPorId(e.getId()) != null) {
			throw new Exception("Este registro possui vínculos que impossibilitam sua exclusão!");
		}
	}

	public void consultaEmpresaPeloNome() {
		try {
			empresas = empresaDao.consultarEmpresa(consulta);
		} catch (RuntimeException erro) {
			Messages.addGlobalError(erro.getCause().getMessage());
		}
	}

	public void consultaEmpresaPeloCnpj() {
		try {
			empresa = empresaDao.consultarEmpresaPeloCnpj(cnpj);
		} catch (RuntimeException erro) {
			Messages.addGlobalError(erro.getCause().getMessage());
		}
	}

	public void selectEmpresa(ActionEvent event) {
		empresa = (Empresa) event.getComponent().getAttributes().get("empresaSelecionada");
		this.cnpj = empresa.getCnpj();
	}

	private void limparComponentes() {
		empresa = new Empresa();
		empresas = new ArrayList<>();
		setCnpj("");
	}

	// -------------------------- gets and sets ----------------------------//
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	public String getConsulta() {
		return consulta;
	}

	public void setConsulta(String consulta) {
		this.consulta = consulta.toUpperCase();
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj.replaceAll("[^0-9]", "");
	}

}
