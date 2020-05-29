package br.com.ms.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;

import br.com.controller.EdicaoRegistroController;
import br.com.controller.MotivoEdicaoRegistroController;
import br.com.controller.RegistroController;
import br.com.ms.model.Atendimento;
import br.com.ms.model.Empresa;
import br.com.ms.model.Liberacao;
import br.com.ms.model.MotivoEdicaoRegistro;
import br.com.ms.model.NotaRegistro;
import br.com.ms.model.Registro;
import br.com.ms.model.Visitante;
import br.com.ms.util.ConverteChaveDeAcesso;
import br.com.ms.util.PermissoesUsuarios;

@ManagedBean
@ViewScoped
public class EdicaoRegistroBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1605272245152225605L;
	private Atendimento atendimento;
	private MotivoEdicaoRegistro motivo;
	private Registro registro;
	private Liberacao liberacao;
	private List<Atendimento> atendimentos;
	private List<Registro> registros;
	private List<Liberacao> liberacaos;
	private Date dataInicial, dataFinal;
	private Long idConsulta;
	private Visitante visitante;
	private List<Visitante> visitantes;
	private String numeroNf;
	private List<Empresa> empresas;
	private String strMotivo;
	private Registro rAuxiliar;
	List<String> nfList;
	private String registroId;

	private MotivoEdicaoRegistroController motivoController;
	private RegistroController registroController;
	private EdicaoRegistroController edicaoRegistroController;

	private boolean viewPanel;

	public EdicaoRegistroBean() {
		motivo = new MotivoEdicaoRegistro();
		atendimento = new Atendimento();
		registro = new Registro();
		liberacao = new Liberacao();
		atendimentos = new ArrayList<>();
		registros = new ArrayList<>();
		dataInicial = Calendar.getInstance().getTime();
		dataFinal = Calendar.getInstance().getTime();
		rAuxiliar = new Registro();
		visitante = new Visitante();
		visitantes = new ArrayList<>();
		empresas = new ArrayList<>();
		nfList = new ArrayList<>();

		// Controllers
		registroController = new RegistroController();
		motivoController = new MotivoEdicaoRegistroController();
		edicaoRegistroController = new EdicaoRegistroController();
	}

	/**
	 * Utilizado para carregamento da tela de edição via passagem de parametro
	 */
	@PostConstruct
	public void init() {
		ConsultaPorParametro();
	}

	/**
	 * Se a consultar for feita por url este parametro será executado
	 */
	private void ConsultaPorParametro() {
		try {
			idConsulta = Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("registroId"));
			consultaRegistroPorId();
		} catch (Exception e) {

		}
	}

	public void consultaRegistroPorId() {
		try {
			registro = registroController.consultarRegistroPorId(idConsulta);
			motivo = motivoController.consultarMotivo(registro.getId());
			if (motivo != null) {
				strMotivo = motivo.getMotivo();
			}
			rAuxiliar = new Registro();
			rAuxiliar = edicaoRegistroController.verificaTipoDeRegistroEpessoa(registro);
			if(rAuxiliar != null) {
				viewPanel = true;
			}
		} catch (Exception e) {
			Messages.addGlobalError(e.getMessage());
		}
	}

	public void addNF() {
		NotaRegistro nota = new NotaRegistro();
		nota.setNumeroNfe(ConverteChaveDeAcesso.getNumeroNfe(numeroNf));
		if (!nfList.contains(nota.getNumeroNfe())) {
			nota.setRegistro(registro);
			try {
				registro.getNotas().add(nota);
				nfList.add(nota.getNumeroNfe());
				numeroNf = "";
			} catch (Exception e) {
				throw e;
			}
		} else {
			numeroNf = "";
		}
	}

	public void removeNf(ActionEvent event) {
		try {
			registro.getNotas().remove(event.getComponent().getAttributes().get("notaSelecionada"));
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * realiza o salvamento de um motivo de edição
	 * 
	 * @param registro
	 */
	private void salvarMotivo(Registro registro) {
		try {
			if (motivo == null) {
				motivo = new MotivoEdicaoRegistro(strMotivo, registro, PermissoesUsuarios.getUsuario());
				motivoController.salvar(motivo);
			} else {
				motivo.setMotivo(strMotivo);
				motivoController.salvar(motivo);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public void salvar() {
		try {
			try {
				if (rAuxiliar.getId() > 0) {
					salvarMotivo(registro);
					verificaIntervalo(registro, rAuxiliar);
					Messages.addGlobalInfo("Registro salvo com sucesso!");
					limpar();
				} else if (registro.getId() > 0) {
					registroController.salvarRegistro(registro);
					salvarMotivo(registro);
					Messages.addGlobalInfo("Registro salvo com sucesso!");
					limpar();
				}

			} catch (Exception e) {
				Messages.addGlobalFatal(e.getMessage());
			}
		} catch (Exception e) {
			Messages.addGlobalError("Registro não encontrado");
		}
	}

	public void limpar() {
		motivo = new MotivoEdicaoRegistro();
		registro = new Registro();
		rAuxiliar = new Registro();
		viewPanel = false;
		idConsulta = null;
	}

	private void verificaIntervalo(Registro registro, Registro auxiliar) throws Exception {
		if (registro.getData().getTime() >= auxiliar.getData().getTime() && registro.getTipo().equals("ENTRADA")) {
			throw new Exception("A data da entrada não pode ser  maior que a data de saida");
		} else if (registro.getData().getTime() <= auxiliar.getData().getTime() && registro.getTipo().equals("SAIDA")) {
			throw new Exception("A data de saída não pode ser menor que a data de entrada");
		} else {
			registroController.salvarRegistro(registro);
			registroController.salvarRegistro(auxiliar);
		}
	}

	public void pessoaSelecionada(ActionEvent event) {
		visitante = (Visitante) event.getComponent().getAttributes().get("pessoaSelecionada");
		empresas = visitante.getEmpresas();
		registro.setPrestadorDeServico(visitante);
	}

	// ------------ Gets and Sets ------------------- //

	public Atendimento getAtendimento() {
		return atendimento;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public List<Atendimento> getAtendimentos() {
		return atendimentos;
	}

	public void setAtendimentos(List<Atendimento> atendimentos) {
		this.atendimentos = atendimentos;
	}

	public List<Registro> getRegistros() {
		return registros;
	}

	public void setRegistros(List<Registro> registros) {
		this.registros = registros;
	}

	public List<Liberacao> getLiberacaos() {
		return liberacaos;
	}

	public void setLiberacaos(List<Liberacao> liberacaos) {
		this.liberacaos = liberacaos;
	}

	public void setAtendimento(Atendimento atendimento) {
		this.atendimento = atendimento;
	}

	public Registro getRegistro() {
		return registro;
	}

	public void setRegistro(Registro registro) {
		this.registro = registro;
	}

	public Liberacao getLiberacao() {
		return liberacao;
	}

	public void setLiberacao(Liberacao liberacao) {
		this.liberacao = liberacao;
	}

	public Long getIdConsulta() {
		return idConsulta;
	}

	public void setIdConsulta(Long idConsulta) {
		this.idConsulta = idConsulta;
	}

	public Visitante getVisitante() {
		return visitante;
	}

	public void setVisitante(Visitante visitante) {
		this.visitante = visitante;
	}

	public List<Visitante> getVisitantes() {
		return visitantes;
	}

	public void setVisitantes(List<Visitante> visitantes) {
		this.visitantes = visitantes;
	}

	public String getNumeroNf() {
		return numeroNf;
	}

	public void setNumeroNf(String numeroNf) {
		this.numeroNf = numeroNf;
	}

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	public boolean isViewPanel() {
		return viewPanel;
	}

	public void setViewPanel(boolean viewPanel) {
		this.viewPanel = viewPanel;
	}

	public Registro getrAuxiliar() {
		return rAuxiliar;
	}

	public void setrAuxiliar(Registro rAuxiliar) {
		this.rAuxiliar = rAuxiliar;
	}

	public String getRegistroId() {
		return registroId;
	}

	public void setRegistroId(String registroId) {
		this.registroId = registroId;
	}

	public MotivoEdicaoRegistro getMotivo() {
		return motivo;
	}

	public void setMotivo(MotivoEdicaoRegistro motivo) {
		this.motivo = motivo;
	}

	public String getStrMotivo() {
		return strMotivo;
	}

	public void setStrMotivo(String strMotivo) {
		this.strMotivo = strMotivo;
	}
}
