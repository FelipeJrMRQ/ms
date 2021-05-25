package br.com.ms.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;

import br.com.controller.ConfiguracaoSistemaController;
import br.com.ms.dao.IntegracaoDao;
import br.com.ms.integracao.HttpPostApi;
import br.com.ms.integracao.LeituraArquivo;
import br.com.ms.model.Integracao;
import br.com.ms.model.LogIntegracao;
import br.com.ms.util.ScheduleUtil;

@ManagedBean
@ViewScoped
public class IntegracaoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5964079029066059882L;

	private List<String> status;
	private static int controle = 0;
	private Integracao integracao;
	private IntegracaoDao dao;
	private LogIntegracao logIntegracao;
	private ConfiguracaoSistemaController config;
	
	public IntegracaoBean() {
		logIntegracao = new LogIntegracao();
		integracao = new Integracao();
		dao = new IntegracaoDao();
		integracao = dao.consultaParametros(1);
	}
	
	
	public void shutdownCron() {
		if(!integracao.isStatus()) {
			ScheduleUtil.shutdown();
		}else {
			config.iniciarAgendamentos();
		}
	}
	
	public void ativar() {
		try {
			dao.ativarIntegracao(integracao);
			Messages.addGlobalInfo("Serviço ativado com sucesso!");
		}catch (Exception e) {
			Messages.addGlobalError("Falha ao salvar dados de integração!");
		}
	}
	
	public void consultarIntegrcao() {
		integracao = dao.consultaParametros(1);
	}

	public void executarIntegracao() {
		LeituraArquivo la = new LeituraArquivo();
		List<String> lista = la.ler("\\\\125.67.2.242\\supersmart\\supervisorio\\");
		List<String[]> clientes = new ArrayList<>();
		List<String[]> produtos = new ArrayList<>();
		List<String[]> beneficiamentos = new ArrayList<>();
		/**
		 * Realiza a l
		 */
		for (String string : lista) {
			try {
				String[] obj = string.split(";");
				if (obj[0].equals("03")) {
					//Codigo 3 representa o Produto
					produtos.add(obj);
				}
				if (obj[0].equals("01")) {
					clientes.add(obj);
				}
				if (obj[0].equals("02")) {
					beneficiamentos.add(obj);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		verificaListagem(clientes, produtos, beneficiamentos);
	}
	
	private void verificaListagem(List<String[]> clientes,List<String[]> produtos,List<String[]> beneficiamentos) {
		if(!clientes.isEmpty()) {
			postCliente(clientes);
		}
		if(!produtos.isEmpty()) {
			postProduto(produtos);
		}
		if(!beneficiamentos.isEmpty()) {
			postBeneficiamento(beneficiamentos);
		}
		
	}
	
	private void postCliente(List<String[]> clientes) {
		for(String[] cliente: clientes) {
			HttpPostApi pos = new HttpPostApi();
			adicionarAlista(pos.insertCliente(cliente, integracao.getHostHttp()).getStatusLine().toString(), cliente);
		}
	}
	
	private void postProduto(List<String[]> produtos) {
		for(String[] produto: produtos) {
			HttpPostApi pos = new HttpPostApi();
			adicionarAlista(pos.insertProduto(produto, integracao.getHostHttp()).getStatusLine().toString(), produto);
		}
	}
	
	private void postBeneficiamento(List<String[]> beneficiamentos) {
		for(String[] beneficiamento: beneficiamentos) {
			HttpPostApi pos = new HttpPostApi();
			adicionarAlista(pos.insertBeneficiamento(beneficiamento, integracao.getHostHttp()).getStatusLine().toString(), beneficiamento);
		}
	}

	public void adicionarAlista(String status, String[] obj) {
		this.status = new ArrayList<>();
		this.status.add(status);
		salvarLog(obj, status);
		PrimeFaces.current().ajax().update(":tbStatus");
	}
	
	private void salvarLog(String[] obj, String httpStatus) {
		try {
			if(obj[0].equals("01")) {
				logIntegracao.setLog("CLIENTE COD: "+obj[3]+ " NOME: "+obj[4].replaceAll("\"", ""));
				logIntegracao.setStatushttp(httpStatus);
				logIntegracao.setData(Calendar.getInstance().getTime());
				dao.salvarLog(logIntegracao);
			}
			if(obj[0].equals("02")) {
				logIntegracao.setLog("BENEFICIAMENTO COD: "+obj[3]+ " NOME: "+obj[4].replaceAll("\"", ""));
				logIntegracao.setStatushttp(httpStatus);
				logIntegracao.setData(Calendar.getInstance().getTime());
				dao.salvarLog(logIntegracao);
			}
			if(obj[0].equals("03")) {
				logIntegracao.setLog("PRODUTO COD: "+obj[3]+ " NOME: "+obj[7].replaceAll("\"", ""));
				logIntegracao.setStatushttp(httpStatus);
				logIntegracao.setData(Calendar.getInstance().getTime());
				dao.salvarLog(logIntegracao);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void openModal() {
		PrimeFaces.current().executeScript("PF('dlgIntegracao').show()");
	}

	public List<String> getStatus() {
		return status;
	}

	public void setStatus(List<String> status) {
		this.status = status;
	}

	public static int getControle() {
		return controle;
	}

	public static void setControle(int controle) {
		IntegracaoBean.controle = controle;
	}

	public Integracao getIntegracao() {
		return integracao;
	}

	public void setIntegracao(Integracao integracao) {
		this.integracao = integracao;
	}

}
