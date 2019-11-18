package br.com.ms.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;

import br.com.ms.dao.AtendimentoDao;
import br.com.ms.dao.LiberacaoDao;
import br.com.ms.dao.LiberacaoVisitanteDao;
import br.com.ms.dao.RegistroDao;
import br.com.ms.model.Atendimento;
import br.com.ms.model.Empresa;
import br.com.ms.model.Liberacao;
import br.com.ms.model.LiberacaoVisitante;
import br.com.ms.model.NotaRegistro;
import br.com.ms.model.Registro;
import br.com.ms.model.Visitante;
import br.com.ms.util.ConverteChaveDeAcesso;

@ManagedBean
@ViewScoped
public class EdicaoRegistroBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1605272245152225605L;
	private Atendimento atendimento;
	private AtendimentoDao atendimentoDao;
	private Registro registro;
	private RegistroDao registroDao;
	private Liberacao liberacao;
	private LiberacaoDao liberacaoDao;
	private List<Atendimento> atendimentos;
	private List<Registro> registros;
	private List<Liberacao> liberacaos;
	private Date dataInicial, dataFinal;
	private Integer idConsulta;
	private Visitante visitante;
	private List<Visitante> visitantes;
	private String numeroNf;
	private List<Empresa> empresas;
	private Registro rAuxiliar;
	List<String> nfList;
	// private LiberacaoVisitanteDao libVisDao;

	private boolean viewPanel;

	public EdicaoRegistroBean() {
		atendimento = new Atendimento();
		atendimentoDao = new AtendimentoDao();
		registro = new Registro();
		registroDao = new RegistroDao();
		liberacao = new Liberacao();
		liberacaoDao = new LiberacaoDao();
		atendimentos = new ArrayList<>();
		registros = new ArrayList<>();
		dataInicial = Calendar.getInstance().getTime();
		dataFinal = Calendar.getInstance().getTime();
		rAuxiliar = new Registro();
		visitante = new Visitante();
		visitantes = new ArrayList<>();
		empresas = new ArrayList<>();
		nfList = new ArrayList<>();
		// libVisDao = new LiberacaoVisitanteDao();
	}

	public void consultaRegistroPorId() {
		try {
			try {
				registro = registroDao.consultaRegistroPeloId(idConsulta);
				try {
					if (registro.getId() > 0) {
						rAuxiliar = new Registro();
						viewPanel = true;
						verificaPrestador(registro);
					}
				} catch (NullPointerException e) {
					rAuxiliar = new Registro();
					Messages.addGlobalError(e.getMessage());
				}
			} catch (Exception e) {
				Messages.addGlobalError(e.getMessage());
			}
		} catch (Exception e) {
			Messages.addGlobalError("Registro não encontrado");
		}
	}

	private void verificaPrestador(Registro registro) {
		try {
			if (registro.getTipo().equals("SAIDA") && registro.getPrestadorDeServico().getTipo().equals("PRESTADOR")) {
				rAuxiliar = consultarLiberacao(registro.getId(), "SAIDA").getEntrada();
			} else if (registro.getTipo().equals("ENTRADA") && registro.getPrestadorDeServico().getTipo().equals("PRESTADOR")) {
				rAuxiliar = consultarLiberacao(registro.getId(), "ENTRADA").getSaida();
			} else if (registro.getTipo().equals("LIBERADO") && registro.getPrestadorDeServico().getTipo().equals("PRESTADOR")) {
				rAuxiliar = consultarLiberacao(registro.getId(), "LIBERADO").getEntrada();
			} else if (registro.getTipo().equals("SAIDA") && registro.getPrestadorDeServico().getTipo().equals("VISITANTE")) {
				rAuxiliar = consultaLibareacaoVisitante(registro.getId(), "SAIDA").getEntrada();
			} else if (registro.getTipo().equals("ENTRADA") && registro.getPrestadorDeServico().getTipo().equals("VISITANTE")) {
				rAuxiliar = consultaLibareacaoVisitante(registro.getId(), "ENTRADA").getSaida();
			} else if (registro.getTipo().equals("LIBERADO") && registro.getPrestadorDeServico().getTipo().equals("VISITANTE")) {
				rAuxiliar = consultaLibareacaoVisitante(registro.getId(), "LIBERADO").getEntrada();
			}
		} catch (Exception e) {
			new Exception("Registro de vinculo não encontrado");
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

	public void salvar() {
		try {
			try {
				if (rAuxiliar.getId() > 0) {
					verificaIntervalo(registro, rAuxiliar);
					Messages.addGlobalInfo("Registro salvo com sucesso!");
				} else if (registro.getId() > 0) {
					registroDao.salvar(registro);
					Messages.addGlobalInfo("Registro salvo com sucesso!");
				}

			} catch (Exception e) {
				Messages.addGlobalFatal(e.getMessage());
			}
		} catch (Exception e) {
			Messages.addGlobalError("Registro não encontrado");
		}
	}

	private void verificaIntervalo(Registro registro, Registro auxiliar) throws Exception {
		if (registro.getData().getTime() >= auxiliar.getData().getTime() && registro.getTipo().equals("ENTRADA")) {
			throw new Exception("A data da entrada não pode ser  maior que a data de saida");
		} else if (registro.getData().getTime() <= auxiliar.getData().getTime() && registro.getTipo().equals("SAIDA")) {
			throw new Exception("A data de saída não pode ser menor que a data de entrada");
		} else {
			registroDao.salvar(registro);
			registroDao.salvar(auxiliar);
		}
	}

	private Liberacao consultarLiberacao(long id, String tipo) {
		LiberacaoDao liDao = new LiberacaoDao();
		if (tipo.equals("SAIDA") ||  tipo.equals("LIBERADO")) {
			return liDao.consultarPorIdDoRegistroDeSaida(id);
		} else {
			return liDao.consultarPorIdDoRegistroDeEntrada(id);
		}
	}

	private LiberacaoVisitante consultaLibareacaoVisitante(long id, String tipo) {
		LiberacaoVisitanteDao libDao = new LiberacaoVisitanteDao();
		if (tipo.equals("SAIDA") || tipo.equals("LIBERADO")) {
			return libDao.consultarLiberacaoPorIdSaida(id);
		} else {
			return libDao.consultarLiberacaoPorIdEntrada(id);
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

	public AtendimentoDao getAtendimentoDao() {
		return atendimentoDao;
	}

	public void setAtendimentoDao(AtendimentoDao atendimentoDao) {
		this.atendimentoDao = atendimentoDao;
	}

	public Registro getRegistro() {
		return registro;
	}

	public void setRegistro(Registro registro) {
		this.registro = registro;
	}

	public RegistroDao getRegistroDao() {
		return registroDao;
	}

	public void setRegistroDao(RegistroDao registroDao) {
		this.registroDao = registroDao;
	}

	public Liberacao getLiberacao() {
		return liberacao;
	}

	public void setLiberacao(Liberacao liberacao) {
		this.liberacao = liberacao;
	}

	public LiberacaoDao getLiberacaoDao() {
		return liberacaoDao;
	}

	public void setLiberacaoDao(LiberacaoDao liberacaoDao) {
		this.liberacaoDao = liberacaoDao;
	}

	public Integer getIdConsulta() {
		return idConsulta;
	}

	public void setIdConsulta(Integer idConsulta) {
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
}
