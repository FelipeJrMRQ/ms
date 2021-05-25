package br.com.ms.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;

import br.com.controller.AtendimentoController;
import br.com.controller.ConfiguracaoSistemaController;
import br.com.controller.NotasFiscaisController;
import br.com.ms.model.Atendimento;
import br.com.ms.model.ConfiguracaoSistema;
import br.com.ms.model.NotaRegistro;
import br.com.ms.model.Registro;

@ManagedBean
@ViewScoped
public class AtendimentoBean implements Serializable {

	private static final long serialVersionUID = 3556388209281192452L;
	private AtendimentoController atendimentoController;
	private NotasFiscaisController notasFiscaisController;

	private Atendimento atendimento;
	private List<Atendimento> atendimentos;
	private Date data;
	private Date dataFim;
	private List<NotaRegistro> notas;
	private String numeroNf;
	private Registro registro;
	private String duracao;
	private String nomeEmpresa;
	private String codigoProg;
	private ConfiguracaoSistema config;
	private ConfiguracaoSistemaController configController;

	/**
	 * Construtor
	 */
	public AtendimentoBean() {
		atendimento = new Atendimento();
		atendimentos = new ArrayList<>();
		notas = new ArrayList<>();
		registro = new Registro();
		data = Calendar.getInstance().getTime();
		dataFim = Calendar.getInstance().getTime();
		atendimentoController = new AtendimentoController();
		notasFiscaisController = new NotasFiscaisController();
		configController = new ConfiguracaoSistemaController();
		config = new ConfiguracaoSistema();
		config = configController.consultarConfiguracao();
	}

	@PostConstruct
	private void consultaInicial() {
		atendimentoController.consultaTabelasCompartilhadas();
	}

	/**
	 * Permite a inserção de notas fiscais
	 */
	public void addNotas() {
		if (!numeroNf.isEmpty()) {
			try {
				notas.add(notasFiscaisController.addNota(numeroNf, atendimento.getRegistro()));
				numeroNf = "";
			} catch (Exception e) {
				numeroNf = "";
			}
		}
	}
	
	
	/**
	 * Realiza a remoção de notas fiscais inseridas
	 * 
	 * @param event
	 */
	public void removeNota(ActionEvent event) {
		NotaRegistro nf = (NotaRegistro) event.getComponent().getAttributes().get("notaSelecionada");
		notasFiscaisController.removeNota(nf.getNumeroNfe());
		notas.remove(nf);
	}

	/**
	 * Utilizado pelos interessados para determinar o inicio do atendimento as
	 * empresas de forma dinamica.
	 * 
	 * @param registro
	 * @param status
	 * @throws Exception
	 */
	public synchronized void inicioAtendimento(Registro registro, String status) {
		try {
			atendimentoController.iniciarAtendimento(registro.getId(), status, this.codigoProg);
			Messages.addGlobalInfo("Atendimento iniciado com sucesso!");
			notasFiscaisController.limparListaDeNotas();
		} catch (Exception erro) {
			Messages.addGlobalError(erro.getMessage());
		}finally {
			limpar();
		}
	}

	/**
	 * Finaliza um atendimento em andamento
	 */
	public synchronized void finalizarAtendimento() {
		try {
			atendimentoController.finalizaAtendimento(atendimento, notas, this.codigoProg);
			notasFiscaisController.limparListaDeNotas();
			Messages.addGlobalInfo("Atendimento finalizado com sucesso!");
		} catch (Exception e) {
			Messages.addGlobalError(e.getMessage());
		}finally {
			limpar();
		}
	}

	/**
	 * Caso o usuario inicie um atendimento erroneamente será possivel através deste
	 * método desfazer o atendimento e retorna-lo ao seu estado original de entrada.
	 * 
	 * @param event
	 */
	public synchronized void desfazerAtendimento(ActionEvent event) {
		atendimento = (Atendimento) event.getComponent().getAttributes().get("atendimentoSelecionado");
		try {
			atendimentoController.desfazerAtendimento(atendimento, atendimento.getRegistro());
			Messages.addGlobalInfo("Atendimento desfeito com sucesso");
			limpar();
		} catch (Exception e) {
			Messages.addGlobalError(e.getMessage());
		}
	}

	/**
	 * Realiza o calculo em um intervalo de tempo
	 * 
	 * @param dt1
	 * @param dt2
	 * @return
	 * @throws Exception
	 */
	public String calculaDuracao(Date dt1, Date dt2) {
		return atendimentoController.calculaIntervalo(dt1, dt2);
	}

	/**
	 * Retorna a quantidade de registros da tebela
	 * 
	 * @return
	 */
	public String quantidadeAtendimentos() {
		return String.valueOf(atendimentos.size());
	}

	/**
	 * Utilazo quando algum resgistro da tabela é selecionado
	 * 
	 * @param event
	 */
	public void selecionado(ActionEvent event) {
		atendimento = atendimentoController.atendimentoSelecionado((Atendimento) event.getComponent().getAttributes().get("registroSelecionado"));
		config = configController.consultarConfiguracao();
	}

	public void consultarAtendimentoPorEmpresa() {
		try {
			atendimentos = atendimentoController.consultarAtendimentoPorEmpresa(data, dataFim, nomeEmpresa);
		} catch (Exception e) {
			Messages.addGlobalError("Não foi possivel realizar esta consulta");
		}
	}
	

	/**
	 * Este método será utilizado para atualizar informações de atendimentos que
	 * ainda estão aguardando atendimentos
	 */
	public synchronized void consultaAtendimentosIni() {
		try {
			 atendimentos = atendimentoController.consultarAtendimento("INICIADO");
		} catch (Exception erro) {
			Messages.addGlobalError(erro.getCause().getMessage());
		}
	}

	public void registroSelecionado(ActionEvent ev) {
		registro = (Registro) ev.getComponent().getAttributes().get("registroSelecionado");
	}

	public void limpar() {
		atendimento = new Atendimento();
		atendimentos = new ArrayList<>();
		registro = new Registro();
		notas = new ArrayList<>();
		this.codigoProg = "";
	}

	public String getNomeEmpresa() {
		return nomeEmpresa;
	}

	public void setNomeEmpresa(String nomeEmpresa) {
		this.nomeEmpresa = nomeEmpresa.toUpperCase();
	}

	public Atendimento getAtendimento() {
		return atendimento;
	}

	public void setAtendimento(Atendimento atendimento) {
		this.atendimento = atendimento;
	}

	public List<Atendimento> getAtendimentos() {
		return atendimentos;
	}

	public void setAtendimentos(List<Atendimento> atendimentos) {
		this.atendimentos = atendimentos;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public List<NotaRegistro> getNotas() {
		return notas;
	}

	public void setNotas(List<NotaRegistro> notas) {
		this.notas = notas;
	}

	public String getNumeroNf() {
		return numeroNf;
	}

	public void setNumeroNf(String numeroNf) {
		this.numeroNf = numeroNf;
	}

	public String getDuracao() {
		return duracao;
	}

	public void setDuracao(String duracao) {
		this.duracao = duracao;
	}

	public Registro getRegistro() {
		return registro;
	}

	public void setRegistro(Registro registro) {
		this.registro = registro;
	}

	public String getCodigoProg() {
		return codigoProg;
	}

	public void setCodigoProg(String codigoProg) {
		this.codigoProg = codigoProg.toUpperCase();
	}

	public ConfiguracaoSistema getConfig() {
		return config;
	}

	public void setConfig(ConfiguracaoSistema config) {
		this.config = config;
	}

}
