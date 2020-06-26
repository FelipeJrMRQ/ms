package br.com.ms.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.omnifaces.util.Messages;

import br.com.controller.LiberacaoVisitanteController;
import br.com.ms.model.LiberacaoVisitante;
import br.com.ms.util.CalculaIntervadoDatas;
import br.com.ms.util.HoraDaInternet;

@ManagedBean
@ViewScoped
public class LiberacaoVisitanteBean implements Serializable {

	private static final long serialVersionUID = 392917479878041320L;

	private LiberacaoVisitante libVisi;
	private LiberacaoVisitanteController libVisController;
	private List<LiberacaoVisitante> liberacoes;
	private String nome;
	private Date dataInicial;
	private Date dataFinal;

	public LiberacaoVisitanteBean() throws Exception {
		libVisi = new LiberacaoVisitante();
		liberacoes = new ArrayList<>();
		dataInicial = HoraDaInternet.getHora();
		dataFinal = HoraDaInternet.getHora();
		libVisController = new LiberacaoVisitanteController();
	}

	public void consultarLibercaoes() {
		try {
			liberacoes = libVisController.consultarLiberacoes(dataInicial, dataFinal, nome);
			if (liberacoes.size() <= 0) {
				Messages.addGlobalError("Não foram encontradas liberações neste período!");
			}
		} catch (Exception e) {
			Messages.addGlobalError("Erro ao realizar consulta!");
		}
	}

	/**
	 * Calcula o tempor de permanecia com base na data e hora dos registros e
	 * entrada e saída
	 * 
	 * @param dtIni
	 * @param dtFim
	 * @return
	 */
	public String tempoDePermanencia(Date dtIni, Date dtFim) {
		return CalculaIntervadoDatas.intevalo(dtIni, dtFim);
	}

	/*
	 * Calcula a quantidade de registros de uma lista de liberacões
	 */
	public String quantidadeDeRegistros() {
		return String.valueOf(liberacoes.size());
	}

	public LiberacaoVisitante getLibVisi() {
		return libVisi;
	}

	public void setLibVisi(LiberacaoVisitante libVisi) {
		this.libVisi = libVisi;
	}

	public List<LiberacaoVisitante> getLiberacoes() {
		return liberacoes;
	}

	public void setLiberacoes(List<LiberacaoVisitante> liberacoes) {
		this.liberacoes = liberacoes;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome.toUpperCase();
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
}
