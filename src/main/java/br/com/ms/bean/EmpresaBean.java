package br.com.ms.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;

import br.com.controller.EmpresaController;
import br.com.ms.model.Empresa;

@ManagedBean
@ViewScoped
public class EmpresaBean implements Serializable {

	private static final long serialVersionUID = 2701635752069790578L;
	private Empresa empresa;
	private List<Empresa> empresas;
	private String nomeEmpresa;
	private String cnpj;

	private EmpresaController empresaController;

	public EmpresaBean() {
		this.empresa = new Empresa();
		this.empresas = new ArrayList<>();
		empresaController = new EmpresaController();
	}

	public void salvarEmpresa() {
		try {
			empresaController.adicionarEmpresa(cnpj, empresa);
			Messages.addGlobalInfo("Empresa cadastrada com sucesso!");
			limparComponentes();
		} catch (Exception erro) {
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void alterarEmpresa() {
		try {
			empresaController.alterarEmpresa(empresa);
			Messages.addGlobalInfo("Empresa alterada com sucesso!");
			limparComponentes();
		} catch (Exception erro) {
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void excluirEmpresa(ActionEvent event) {
		try {
			empresaController.excluirEmpresa((Empresa) event.getComponent().getAttributes().get("empresaSelecionada"));
			Messages.addGlobalInfo("Empresa excluída com sucesso!");
			limparComponentes();
		} catch (Exception erro) {
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void consultaEmpresaPeloNome() {
		try {
			empresas = empresaController.consultaEmpresaPeloNome(nomeEmpresa);
		} catch (RuntimeException erro) {
			Messages.addGlobalError(erro.getCause().getMessage());
		}
	}

	public void consultaEmpresaPeloCnpj() {
		try {
			empresa = empresaController.consultaEmpresaPeloCnpj(cnpj);
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

	public String getNomeEmpresa() {
		return nomeEmpresa;
	}

	public void setNomeEmpresa(String nomeEmpresa) {
		this.nomeEmpresa = nomeEmpresa.toUpperCase();
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj.replaceAll("[^0-9]", "");
	}

}
