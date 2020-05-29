package br.com.ms.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;

import br.com.controller.EmpresaController;
import br.com.controller.VisitanteController;
import br.com.ms.model.Empresa;
import br.com.ms.model.Visitante;

@ManagedBean
@ViewScoped
public class VisitanteBean implements Serializable {

	private static final long serialVersionUID = -247732591206859698L;
	private Visitante visitante;
	private List<Empresa> empresas;
	private List<Visitante> visitantes;
	private String nomeEmpresa;
	private String nomeVisitante;
	private List<Empresa> empresasEscolhidas;
	private String tipo;
	private VisitanteController visitanteController;
	private EmpresaController empresaController;
	private boolean flagBotao;

	public VisitanteBean() {
		visitante = new Visitante();
		visitantes = new ArrayList<>();
		empresas = new ArrayList<>();
		empresasEscolhidas = new ArrayList<>();
		tipo = "PRESTADOR";
		visitanteController = new VisitanteController();
		empresaController = new EmpresaController();
		flagBotao = true;
	}

	public void salvar() {
		try {
			visitanteController.salvar(visitante, empresasEscolhidas, tipo);
			limpar();
			Messages.addGlobalInfo("Salvo com sucesso!");
		} catch (Exception e) {
			Messages.addGlobalError(e.getMessage());
		}
	}

	public void alterar() {
		try {
			visitanteController.alterar(visitante, empresasEscolhidas, tipo);
			flagBotao = false;
			limpar();
			Messages.addGlobalInfo("Alterado com sucesso!");
		} catch (Exception erro) {
			Messages.addGlobalError(erro.getMessage());
		}
	}

	public void exluir(ActionEvent event) {
		try {
			visitanteController.exluir((Visitante) event.getComponent().getAttributes().get("prestadorSelecionado"));
			limpar();
			Messages.addGlobalInfo("Excluído com sucesso!");
		} catch (Exception ex) {
			Messages.addGlobalError(ex.getMessage());
		}
	}

	public void consultarVisitante() {
		try {
			visitantes = visitanteController.consultaPorNome(nomeVisitante);
		} catch (Exception e) {
			Messages.addGlobalError(e.getMessage());
		}
	}

	public void prestadorSelecionado(ActionEvent event) {
		visitante = (Visitante) event.getComponent().getAttributes().get("prestadorSelecionado");
		tipo = visitante.getTipo();
		empresasEscolhidas = visitante.getEmpresas();
		flagBotao = true;
	}

	public void removeEmpresa(ActionEvent event) {
		visitante.getEmpresas().remove(((Empresa) event.getComponent().getAttributes().get("empresaSelecionada")));
	}

	public void adicionarEmpresa(ActionEvent event) {
		try {
			empresasEscolhidas.add((Empresa) event.getComponent().getAttributes().get("empresaSelecionada"));
			visitante.setEmpresas(empresasEscolhidas);
		} catch (RuntimeException ex) {
			Messages.addGlobalError(ex.getMessage());
		}
	}

	public void consultarEmpresa() {
		empresas = empresaController.consultarEmpresaPorNome(nomeEmpresa);
		if (empresas.isEmpty()) {
			Messages.addGlobalError("Empresa não encontrada!");
		}
	}

	public void limpar() {
		nomeEmpresa = "";
		visitante = new Visitante();
		visitantes = new ArrayList<>();
		empresasEscolhidas = new ArrayList<>();
		empresas = new ArrayList<>();
	}

	public void prestadorTp() {

	}

	// -------------------- gets and sets -------------------------//
	public Visitante getVisitante() {
		return visitante;
	}

	public void setVisitante(Visitante visitante) {
		this.visitante = visitante;
	}

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	public List<Visitante> getvisitantes() {
		return visitantes;
	}

	public void setvisitantes(List<Visitante> visitantes) {
		this.visitantes = visitantes;
	}

	public String getNomeEmpresa() {
		return nomeEmpresa;
	}

	public void setNomeEmpresa(String nomeEmpresa) {
		this.nomeEmpresa = nomeEmpresa.toUpperCase();
	}

	public List<Empresa> getEmpresasEscolhidas() {
		return empresasEscolhidas;
	}

	public void setEmpresasEscolhidas(List<Empresa> empresasEscolhidas) {
		this.empresasEscolhidas = empresasEscolhidas;
	}

	public String getNomeVisitante() {
		return nomeVisitante;
	}

	public void setNomeVisitante(String nomeVisitante) {
		this.nomeVisitante = nomeVisitante.toUpperCase();
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public boolean isFlagBotao() {
		return flagBotao;
	}

	public void setFlagBotao(boolean flagBotao) {
		this.flagBotao = flagBotao;
	}

}
