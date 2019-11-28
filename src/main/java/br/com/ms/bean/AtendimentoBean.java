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

import br.com.ms.dao.AtendimentoDao;
import br.com.ms.dao.RegistroDao;
import br.com.ms.model.Atendimento;
import br.com.ms.model.Liberacao;
import br.com.ms.model.NotaRegistro;
import br.com.ms.model.Registro;
import br.com.ms.model.Usuario;
import br.com.ms.repository.LiberacaoRepository;
import br.com.ms.util.CalculaIntervadoDatas;
import br.com.ms.util.ConverteChaveDeAcesso;
import br.com.ms.util.HoraDaInternet;
import br.com.ms.util.PermissoesUsuarios;

@ManagedBean
@ViewScoped
public class AtendimentoBean implements Serializable {

	private static final long serialVersionUID = 3556388209281192452L;
	private AtendimentoDao atendimentoDao;
	private Atendimento atendimento;
	private List<Atendimento> atendimentos;
	private static final String INICIADO = "INICIADO";
	private static final String ABERTO = "ABERTO";
	private static final String FINALIZADO = "FINALIZADO";
	private static final String LIBERADO = "LIBERADO";
	private String status;
	private Date data;
	private Date dataFim;
	private RegistroDao registroDao;
	private Usuario usuario;
	private List<NotaRegistro> notas;
	private String numeroNf;
	Liberacao liberacao = new Liberacao();
	private Registro registro;
	private String duracao;
	List<String> numeroNota;
	//private List<Registro> registros;

	/**
	 * Construtor
	 */
	public AtendimentoBean() {
		atendimentoDao = new AtendimentoDao();
		atendimento = new Atendimento();
		atendimentos = new ArrayList<>();
		registroDao = new RegistroDao();
		notas = new ArrayList<>();
		registro = new Registro();
		data = Calendar.getInstance().getTime();
		dataFim = Calendar.getInstance().getTime();
		status = "INICIADO";
		numeroNota = new ArrayList<>();
		//registros = new ArrayList<>();
	}

	/**
	 * Permite a inserção de notas fiscais
	 */
	public void addNotas() {
		String nf = ConverteChaveDeAcesso.getNumeroNfe(numeroNf);
		if (!numeroNota.contains(nf)) {
			numeroNota.add(nf);
			NotaRegistro n = new NotaRegistro();
			// MontaRegistroNfe montaRegistroNfe = new MontaRegistroNfe();
			n.setNumeroNfe(nf);
			n.setChave(numeroNf);
			n.setRegistro(registro);
			notas.add(n);
			numeroNf = "";
		} else {
			numeroNf = "";
		}
	}

	public void limparListaNotas() {
		notas = new ArrayList<>();
		numeroNota = new ArrayList<>();
	}

	@PostConstruct
	private void consultaInicial() {
		SharedListBean.consultaRegistrosAguardando();
		SharedListBean.consultaAtendimentosIni();
	}

	public void removeNota(ActionEvent event) {
		notas.remove((NotaRegistro) event.getComponent().getAttributes().get("notaSelecionada"));
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
				/**
				 * Verifica se o registro já esta vinculado a um atendimento Como o sistema será
				 * utilizado por varias pessoas há possibilidade de que haja o inicio do
				 * atendimento por outras pessoas este método protege esta situação.
				 */
				atendimentoDao.consultarAtendimentoPorRegistro(registro.getId());
				// -------------------------------------------------------------------
				liberacao = new Liberacao();
				atendimento = new Atendimento();
				atendimento.setData_inicio(HoraDaInternet.getHora());
				atendimento.setData_fim(null);
				atendimento.setStatus(INICIADO);
				atendimento.setRegistro(registro);
				atendimento.setUsuario_inicio(PermissoesUsuarios.getUsuario());
				this.registro = registroDao.consultaRegistroPeloId(registro.getId());
				this.registro.setStatus(INICIADO);
				atendimentoDao.salvarAtendimento(atendimento, this.registro);
				limpar();
				consultaInicial();
				Messages.addGlobalInfo("Atendimento iniciado com sucesso!");
		} catch (Exception erro) {
			consultaInicial();
			Messages.addGlobalError(erro.getMessage());
		}
	}

	/**
	 * Caso o usuario inicie um atendimento erroneamente será possivel através deste
	 * método desfazer o atendimento e retorna-lo ao seu estado original de entrada.
	 * 
	 * @param event
	 */
	public synchronized void desfazerAtendimento(ActionEvent event) {
		Atendimento at = new Atendimento();
		at = (Atendimento) event.getComponent().getAttributes().get("atendimentoSelecionado");
		atendimento = atendimentoDao.consultarAtentimentoPorId(at.getId());
		registro = registroDao.consultaRegistroPeloId(atendimento.getRegistro().getId());
		if (atendimentoDao.consultarAtentimentoPorId(atendimento.getId()) == null) {
			consultaInicial();
			Messages.addGlobalInfo("Atendimento desfeito com sucesso!");
		} else {
			try {	
				registro.setStatus(ABERTO);
				atendimentoDao.excluirAtendimento(atendimento, registro);
				limpar();
				consultaInicial();
				Messages.addGlobalInfo("Atendimento desfeito com sucesso!");
			} catch (Exception e) {
				Messages.addGlobalError("Erro ao desfazer atendimento! " + e.getMessage());
			}
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
	public String calculaDuracao(Date dt1, Date dt2) throws Exception {
		try {
			return CalculaIntervadoDatas.intevalo(dt1, dt2);
		} catch (NullPointerException e) {
			return CalculaIntervadoDatas.intevalo(dt1, HoraDaInternet.getHora());
		}
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
		atendimento = (Atendimento) event.getComponent().getAttributes().get("registroSelecionado");
		registro = registroDao.consultaRegistroPeloId(atendimento.getRegistro().getId());
	}

	/**
	 * Este método impede que o usuário finalize duas vezes o mesmo atendimento.
	 */
	public synchronized void validaFimAtendimento() {
		try {
			if (atendimento != null) {
				try {
					if (!atendimento.getStatus().equals(FINALIZADO)) {
						fimAtendimento(atendimento);
					}
				} catch (Exception e) {
					consultaInicial();
				}
			}
		} catch (Exception e) {
			Messages.addGlobalError(e.getMessage());
		}
	}

	/**
	 * Finaliza o periodo de atendimento a um prestador de servico A ideia do
	 * synchronized e bloquear este trecho de codigo evitando concorrencia e
	 * possiveis problemas.
	 * 
	 * @param event
	 * @throws Exception
	 */
	private synchronized void fimAtendimento(Atendimento atdm) {
		Atendimento at = new Atendimento();
		at = atendimentoDao.consultarAtentimentoPorId(atdm.getId());
		try {
			if (gerarRegistroSaida(at)) {
				at.setData_fim(HoraDaInternet.getHora());
				at.setStatus(FINALIZADO);
				at.setUsuario_fim(PermissoesUsuarios.getUsuario());
				atendimentoDao.alterarAtendimento(at, registroDao.consultaRegistroPeloId(at.getRegistro().getId()));
				Messages.addGlobalInfo("Atendimento finalizado com sucesso!");
				limpar();
				consultaInicial();
				SharedListBean.consultaLiberadosSaida();
			}
		} catch (Exception erro) {
			Messages.addGlobalError(erro.getMessage());
		}
	}

	/**
	 * Realiza um registro de saida com base nos dados de uma entrada
	 * 
	 * @param atendimento
	 * @throws Exception
	 */

	private synchronized boolean gerarRegistroSaida(Atendimento atendimento) throws Exception {
		Atendimento at = new Atendimento();
		at = atendimentoDao.consultarAtentimentoPorId(atendimento.getId());
		try {
			registro = new Registro();
			registro.setData(HoraDaInternet.getHora());
			registro.setEmpresa(at.getRegistro().getEmpresa());
			registro.setnotas(notas);
			registro.setPlacaVeiculo(at.getRegistro().getPlacaVeiculo());
			registro.setPrestadorDeServico(at.getRegistro().getPrestadorDeServico());
			registro.setStatus(FINALIZADO);
			registro.setTipo(LIBERADO);
			registro.setUsuario(PermissoesUsuarios.getUsuario());
			Registro rgL = new Registro();
			rgL = registroDao.salvar(registro);
			if (gerarLiberacao(at, rgL)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			throw new Exception("Erro ao tentar gerar registro de saída!");
		}
	}

	/**
	 * Gera uma liberação de saida para que posteriormente seja realizada um
	 * conferencia e saida definitiva
	 * 
	 * @param a
	 * @param r
	 * @throws Exception
	 */
	private synchronized boolean gerarLiberacao(Atendimento atendimento, Registro saida) throws Exception {
		try {
			new LiberacaoRepository(atendimento.getRegistro(), saida, atendimento, PermissoesUsuarios.getUsuario());
			notas = new ArrayList<>();
			return true;
		} catch (Exception ex) {
			Messages.addGlobalError(ex.getMessage());
			throw ex;
		}
	}

	/**
	 * Este método será utilizado para atualizar informações de atendimentos que
	 * ainda estão aguardando atendimentos
	 */
	public synchronized void consultaAtendimentosIni() {
		status = "INICIADO";
		try {
			atendimentos = atendimentoDao.consultarAtentimento(status);
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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

	public List<String> getNumeroNota() {
		return numeroNota;
	}

	public void setNumeroNota(List<String> numeroNota) {
		this.numeroNota = numeroNota;
	}

	public Registro getRegistro() {
		return registro;
	}

	public void setRegistro(Registro registro) {
		this.registro = registro;
	}

}
