package br.com.ms.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;

import br.com.ms.dao.EmpresaDao;
import br.com.ms.dao.VisitanteDao;
import br.com.ms.model.Empresa;
import br.com.ms.model.Visitante;
import br.com.ms.validacao.ValidaCpf;

@ManagedBean
@ViewScoped
public class VisitanteBean implements Serializable {

	private static final long serialVersionUID = -247732591206859698L;
	private Visitante visitante;
	private VisitanteDao visitanteDao;
	private List<Empresa> empresas;
	private List<Visitante> visitantes;
	private Empresa empresa;
	private EmpresaDao empresaDao;
	private String consulta;
	private String consultaVisitante;
	private List<Empresa> empresasEscolhidas;
	private boolean prestadorTipo;
	private String tipo;
	private String tipoEdicao;
	private boolean pestadorOuVisitante;
	IllegalArgumentException erro;

	public VisitanteBean() {
		visitante = new Visitante();
		visitanteDao = new VisitanteDao();
		visitantes = new ArrayList<>();
		empresaDao = new EmpresaDao();
		empresas = new ArrayList<>();
		empresasEscolhidas = new ArrayList<>();
		tipo = "PRESTADOR";
	}

	public void salvar() {
		try {
			validaCPF(visitante.getCpf());
			validaTamhoNome(visitante.getNome());
			validaRg(visitante.getRg(), visitante.getCpf());
			verificaExistenciaCPF(visitante);
			verificaExistenciaRg(visitante);
			validaEmpresas(visitante);// Este método deve ficar em último
		} catch (Exception e) {
			Messages.addGlobalError(e.getMessage());
		}
	}

	public void alterar() {
		try {
			validaTamhoNome(visitante.getNome());
			validaCPF(visitante.getCpf());
			validaRg(visitante.getRg(), visitante.getCpf());
			validaEmpresas(visitante);
		} catch (Exception erro) {
			Messages.addGlobalError(erro.getMessage());
		}
	}

	private void salvarVisitante() {
		try {
			empresasEscolhidas = new ArrayList<>();
			visitante.setEmpresas(empresasEscolhidas);
			visitante.setTipo("VISITANTE");
			visitante.setStatus(true);
			visitanteDao.salvarPrestadorDeServico(visitante);
			Messages.addGlobalInfo("Visitante cadastrado com sucesso!");
			limpar();
		} catch (Exception ex) {
			Messages.addGlobalError(ex.getMessage());
		}
	}

	private void salvarPrestador() {
		try {
			visitante.setTipo("PRESTADOR");
			visitante.setStatus(true);
			visitante.setEmpresas(empresasEscolhidas);
			visitanteDao.salvarPrestadorDeServico(visitante);
			Messages.addGlobalInfo("Prestador de serviço cadastrado com sucesso!");
			limpar();
		} catch (Exception ex) {
			Messages.addGlobalError(erro.getMessage());
		}
	}

	private void verificaExistenciaCPF(Visitante v) throws Exception {
		if (!v.getCpf().isEmpty()) {
			Visitante vis = visitanteDao.validaExistenciaCpf(v);
			if (vis != null) {
				throw new Exception("Este CPF já está cadastrado no sistema");
			}
		}
	}

	private void verificaExistenciaRg(Visitante v) throws Exception {
		if (!v.getRg().isEmpty()) {
			Visitante vis = visitanteDao.validaExistenciaRg(v);
			if (vis != null) {
				throw new Exception("Este RG já está cadastrado no sistema");
			}
		}
	}

	private boolean validaTamhoNome(String nome) throws Exception {
		if (nome.length() >= 10) {
			return true;
		} else {
			throw new Exception("Preencha o nome completo!");
		}
	}

	private boolean validaCPF(String cpf) throws Exception {
		if (cpf.isEmpty() || ValidaCpf.isCPF(cpf)) {
			return true;
		} else {
			throw new Exception("O CPF digitado não é válido");
		}
	}

	private void validaRg(String rg, String cpf) throws Exception {
		if (cpf.isEmpty() && rg.trim().isEmpty()) {
			throw new Exception("O CPF ou RG deve ser preenchido");
		}
	}

	private void validaEmpresas(Visitante v) throws Exception {
		if (tipo.equals("PRESTADOR")) {
			if (v.getEmpresas() == null || v.getEmpresas().isEmpty()) {
				throw new Exception("Selecione ao menos uma empresa!");
			} else {
				salvarPrestador();
			}
		} else if (tipo.equals("VISITANTE")) {
			empresasEscolhidas = new ArrayList<>();
			salvarVisitante();
		}
	}

	/**
	 * Realiza alteração entre os tipos prestador de serviço e visitante
	 */
	public void alterarTipo() {

	}

	public void exluir(ActionEvent event) {
		try {
			visitante = (Visitante) event.getComponent().getAttributes().get("prestadorSelecionado");
			visitante = visitanteDao.consultaVisitantePorId(visitante.getId());
			validaExclusao(visitante);
			visitanteDao.excluirPrestadorDeServico(visitanteDao.consultaVisitantePorId(visitante.getId()));
			Messages.addGlobalInfo("Registro excluído com sucesso!");
		} catch (Exception ex) {
			Messages.addGlobalError(ex.getMessage());
		}
	}

	private void validaExclusao(Visitante v) throws Exception {
		if (visitanteDao.consultaRegistroVisitantePorId(v.getId()) != null) {
			throw new Exception("Este registro possui vínculos que impossibilitam sua exclusão!");
		}
	}

	public void consultarVisitante() {
		visitantes = visitanteDao.consultaPrestadorDeServicoPeloNome(consultaVisitante);
	}

	public void prestadorSelecionado(ActionEvent event) {
		visitante = (Visitante) event.getComponent().getAttributes().get("prestadorSelecionado");
		tipo = visitante.getTipo();
		empresasEscolhidas = visitante.getEmpresas();
	}

	public void removeEmpresa(ActionEvent event) {
		visitante.getEmpresas().remove(((Empresa) event.getComponent().getAttributes().get("empresaSelecionada")));
	}

	public void adicionarEmpresa(ActionEvent event) {
		try {
			empresa = (Empresa) event.getComponent().getAttributes().get("empresaSelecionada");
			empresasEscolhidas.add(empresa);
			visitante.setEmpresas(empresasEscolhidas);
		} catch (RuntimeException ex) {
			Messages.addGlobalError(ex.getMessage());
		}
	}

	public void consultarEmpresa() {
		try {
			if (consulta.isEmpty()) {
				Messages.addGlobalError("Digite o nome da empresa");
				consulta = "";
			} else if (!consulta.isEmpty() || consulta != "") {
				empresas = empresaDao.consultarEmpresa(consulta);
				consulta = "";
			}
		} catch (Exception ex) {
			Messages.addGlobalError(ex.getMessage());
		}
	}

	public void limpar() {
		consulta = "";
		visitante = new Visitante();
		visitantes = new ArrayList<>();
		empresasEscolhidas = new ArrayList<>();
		empresas = new ArrayList<>();
		consultarVisitante();
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

	public String getTipoEdicao() {
		return tipoEdicao;
	}

	public void setTipoEdicao(String tipoEdicao) {
		this.tipoEdicao = tipoEdicao;
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

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public String getConsulta() {
		return consulta;
	}

	public void setConsulta(String consulta) {
		this.consulta = consulta.toUpperCase();
	}

	public List<Empresa> getEmpresasEscolhidas() {
		return empresasEscolhidas;
	}

	public void setEmpresasEscolhidas(List<Empresa> empresasEscolhidas) {
		this.empresasEscolhidas = empresasEscolhidas;
	}

	public String getconsultaVisitante() {
		return consultaVisitante;
	}

	public void setconsultaVisitante(String consultaVisitante) {
		this.consultaVisitante = consultaVisitante.toUpperCase();
	}

	public boolean getPrestadorTipo() {
		return prestadorTipo;
	}

	public void setPrestadorTipo(boolean prestadorTipo) {
		this.prestadorTipo = prestadorTipo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public boolean isPestadorOuVisitante() {
		return pestadorOuVisitante;
	}

	public void setPestadorOuVisitante(boolean pestadorOuVisitante) {
		this.pestadorOuVisitante = pestadorOuVisitante;
	}

}
