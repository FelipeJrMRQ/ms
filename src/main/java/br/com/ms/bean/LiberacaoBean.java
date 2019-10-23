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

import br.com.ms.dao.LiberacaoDao;
import br.com.ms.model.Liberacao;
import br.com.ms.util.AutenticarServidorNotas;
import br.com.ms.util.CalculaIntervadoDatas;
import br.com.ms.util.HoraDaInternet;

@ManagedBean
@ViewScoped
public class LiberacaoBean implements Serializable {

	private static final long serialVersionUID = 8310862975742614864L;
	private Liberacao liberacao;
	private String tipoConsulta;
	private String consulta;
	private Date dataInicial;
	private Date dataFinal;
	private LiberacaoDao liberacaoDao;
	private List<Liberacao> liberacoes;
	String tempo;
	String tempoAtendimento;
	private boolean flag;

	public LiberacaoBean() throws Exception {
		liberacao = new Liberacao();
		dataFinal = HoraDaInternet.getHora();
		dataInicial = HoraDaInternet.getHora();
		liberacaoDao = new LiberacaoDao();
		liberacoes = new ArrayList<>();
		tipoConsulta = "EMPRESA";
		flag = true;
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
			liberacoes = liberacaoDao.consultarPeloNomeDoPrestador(dataInicial, dataFinal, consulta);
			if(liberacoes.isEmpty()) {
				Messages.addGlobalError("Não foram encontrados registros com este nome.");
			}
		} catch (Exception ex) {
			Messages.addGlobalInfo(ex.getCause().getMessage());
		}
	}
	
	public void consultaPelaPlacaVeiculo() {
		try {
			liberacoes = liberacaoDao.consultarPelaPlacaVeiculo(dataInicial, dataFinal, consulta);
			if(liberacoes.isEmpty()) {
				Messages.addGlobalError("Não foram encontrados registros com este número de placa.");
			}
		}catch(Exception e) {
			Messages.addGlobalError(e.getMessage());
		}
	}

	public void consultaPorEmpresa() {
		try {
			liberacoes = liberacaoDao.consultarPeloNomeDaEmpresa(dataInicial, dataFinal, consulta);
			if(liberacoes.isEmpty()) {
				Messages.addGlobalError("Não foram encontrados registros.");
			}
		} catch (Exception ex) {
			Messages.addGlobalInfo(ex.getCause().getMessage());// http://localhost:8080/ms/pages/liberacao.xhtml#
		}
	}

	public void consultaPorCategoria() {
		try {
			liberacoes = liberacaoDao.consultarPorCategoria(dataInicial, dataFinal, consulta);
			if(liberacoes.isEmpty()) {
				Messages.addGlobalError("Não foram encontrados registros para esta data.");
			}
		} catch (Exception ex) {
			Messages.addGlobalInfo(ex.getCause().getMessage());
		}
	}
	
	public void teste() {
		System.out.println("ok");
	}

	public void consultaPorNf() {
		try {
			if (flag) {
				liberacoes = liberacaoDao.consultarPeloNumeroNfEntrada(consulta);
				if(liberacoes.isEmpty()) {
					Messages.addGlobalError("Não foram encontrados registros com este número de nota.");
				}
			} else {
				liberacoes = liberacaoDao.consultarPeloNumeroNfSaida(consulta);
				if(liberacoes.isEmpty()) {
					Messages.addGlobalError("Não foram encontrados registros com este número de nota.");
				}
			}
		} catch (Exception ex) {
			Messages.addGlobalInfo(ex.getCause().getMessage());
		}
	}

	public void liberacaoSelecionada(ActionEvent event) {
		Liberacao lib = new Liberacao();
		lib = (Liberacao) event.getComponent().getAttributes().get("liberacaoSelecionada");
		liberacao = liberacaoDao.consultaPorId(lib.getId());
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
}
