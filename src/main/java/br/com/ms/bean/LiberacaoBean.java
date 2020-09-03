package br.com.ms.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;

import br.com.controller.LiberacaoController;
import br.com.ms.dao.MotivoEdicaoRegistroDao;
import br.com.ms.model.Liberacao;
import br.com.ms.model.MotivoEdicaoRegistro;
import br.com.ms.model.Usuario;
import br.com.ms.util.AutenticarServidorNotas;
import br.com.ms.util.CalculaIntervadoDatas;
import br.com.ms.util.HoraDaInternet;

@ManagedBean
@ViewScoped
public class LiberacaoBean implements Serializable {

	private static final long serialVersionUID = 8310862975742614864L;
	private Liberacao liberacao;
	private MotivoEdicaoRegistro motivoEntrada;
	private MotivoEdicaoRegistro motivoSaida;
	private MotivoEdicaoRegistroDao motivoDao;
	private String tipoConsulta;
	private String consulta;
	private Date dataInicial;
	private Date dataFinal;
	private List<Liberacao> liberacoes;
	String tempo;
	String tempoAtendimento;
	private boolean flag;
	private Usuario user;
	
	private LiberacaoController liberacaoController;

	public LiberacaoBean() throws Exception {
		motivoEntrada = new MotivoEdicaoRegistro();
		motivoSaida = new MotivoEdicaoRegistro();
		motivoDao = new MotivoEdicaoRegistroDao();
		liberacao = new Liberacao();
		dataFinal = HoraDaInternet.getHora();
		dataInicial = HoraDaInternet.getHora();
		liberacoes = new ArrayList<>();
		tipoConsulta = "EMPRESA";
		flag = true;
		user = new Usuario();
		liberacaoController = new LiberacaoController();
	}

	public void consultaUpdate() {
		consulta = "";
		liberacoes = new ArrayList<>();
	}

	public void abrirArquivodeNF(String nf) throws IOException {
		try {
			AutenticarServidorNotas.buscarNotaEntrada(nf);
		} catch (Exception e) {
			Messages.addGlobalError("Não foi possivel encontrar o arquivo! " + e.getMessage());
		}
	}

	public void consultaPorNome() {
		try {
			liberacoes = liberacaoController.consultaPorNome(dataInicial, dataFinal, consulta);
		} catch (Exception ex) {
			Messages.addGlobalError(ex.getMessage());
		}
	}

	public void consultaPelaPlacaVeiculo() {
		try {
			liberacoes = liberacaoController.consultaPelaPlacaVeiculo(dataInicial, dataFinal, consulta);
		} catch (Exception e) {
			Messages.addGlobalError(e.getMessage());
		}
	}

	public void consultaPorEmpresa() {
		try {
			liberacoes = liberacaoController.consultaPorEmpresa(dataInicial, dataFinal, consulta);
		} catch (Exception ex) {
			Messages.addGlobalError(ex.getMessage());
		}
	}

	public void consultaPorCategoria() {
		try {
			liberacoes = liberacaoController.consultaPorCategoria(dataInicial, dataFinal, consulta);
		} catch (Exception ex) {
			Messages.addGlobalError(ex.getCause().getMessage());
		}
	}

	public void consultaPorNf() {
		try {
			if (flag) {
				liberacoes = liberacaoController.consultaPelaNotaDeEntrada(consulta);
			} else {
				liberacoes = liberacaoController.consultaPelaNotaDeSaida(consulta);
			}
		} catch (Exception ex) {
			Messages.addGlobalError(ex.getMessage());
		}
	}
	
	/**
	 * Realiza o calculo de intervalo de entrada de saída da portaria
	 * @param inicio
	 * @param fim
	 */
	public String calculaIntervalo(Date entrada, Date saida) {
		try {
			return CalculaIntervadoDatas.intevalo(entrada, saida);
		}catch(Exception e) {
			Messages.addGlobalError(e.getMessage());
			throw e;
		}
	}
	
	public void liberacaoSelecionada(ActionEvent event) {
		Liberacao lib = new Liberacao();
		lib = (Liberacao) event.getComponent().getAttributes().get("liberacaoSelecionada");
		liberacao = liberacaoController.consultaPorId(lib.getId());
		user = lib.getUsuario();
		motivoEntrada = motivoDao.consultar(lib.getEntrada().getId());
		motivoSaida = motivoDao.consultar(lib.getSaida().getId());
		tempo = CalculaIntervadoDatas.intevalo(liberacao.getEntrada().getData(), liberacao.getSaida().getData());
		tempoAtendimento = CalculaIntervadoDatas.intevalo(liberacao.getAtendimento().getData_inicio(), liberacao.getAtendimento().getData_fim());
	}

	public String quantidadeRegistros() {
		return String.valueOf(liberacoes.size());
	}

	/*---------------------- geters and seters --------------------------- */
	
	public Liberacao getLiberacao() {
		return liberacao;
	}

	public MotivoEdicaoRegistro getMotivoEntrada() {
		return motivoEntrada;
	}

	public void setMotivoEntrada(MotivoEdicaoRegistro motivoEntrada) {
		this.motivoEntrada = motivoEntrada;
	}

	public MotivoEdicaoRegistro getMotivoSaida() {
		return motivoSaida;
	}

	public void setMotivoSaida(MotivoEdicaoRegistro motivoSaida) {
		this.motivoSaida = motivoSaida;
	}

	public void setLiberacao(Liberacao liberacao) {
		this.liberacao = liberacao;
	}

	public String getTipoConsulta() {
		return tipoConsulta;
	}

	public void setTipoConsulta(String tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}

	public String getConsulta() {
		return consulta;
	}

	public void setConsulta(String consulta) {
		this.consulta = consulta.toUpperCase();
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

	public List<Liberacao> getLiberacoes() {
		return liberacoes;
	}

	public void setLiberacoes(List<Liberacao> liberacoes) {
		this.liberacoes = liberacoes;
	}

	public String getTempo() {
		return tempo;
	}

	public void setTempo(String tempo) {
		this.tempo = tempo;
	}

	public String getTempoAtendimento() {
		return tempoAtendimento;
	}

	public void setTempoAtendimento(String tempoAtendimento) {
		this.tempoAtendimento = tempoAtendimento;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public Usuario getUser() {
		return user;
	}

	public void setUser(Usuario user) {
		this.user = user;
	}
	
}
