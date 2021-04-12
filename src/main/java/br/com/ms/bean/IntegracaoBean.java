package br.com.ms.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;

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

	
	private void iniciaAgendamento(String cron) {
		try {
			if (controle == 0) {
				JobDetail jb = JobBuilder.newJob(LeituraArquivo.class).withIdentity("Integracao").build();
				ScheduleUtil.start(jb, cron , "g2", "Integra");
			}
			IntegracaoBean.controle = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public IntegracaoBean() {
		logIntegracao = new LogIntegracao();
		integracao = new Integracao();
		dao = new IntegracaoDao();
		integracao = dao.consultaParametros(1);
	}
	
	public void ativar() {
		try {
			dao.ativarIntegracao(integracao);
			iniciaAgendamento(integracao.getCronParametros());
			Messages.addGlobalInfo("Serviço ativado com sucesso!");
		}catch (Exception e) {
			Messages.addGlobalError("Falha ao salvar dados de integração!");
		}
	}
	
	public void consultarIntegrcao() {
		integracao = dao.consultaParametros(1);
	}

	public void executarIntegracao() {
		this.status = new ArrayList<>();
		LeituraArquivo la = new LeituraArquivo();
		/**
		 * Realiza a l
		 */
		for (String string : la.ler(integracao.getLocalArquivo())) {
			try {
				String[] obj = string.split(";");
				if (obj[0].equals("03")) {
					HttpPostApi pos = new HttpPostApi();
					adicionarAlista(pos.insertProduto(obj, integracao.getHostHttp()).getStatusLine().toString(), obj);
				}
				if (obj[0].equals("01")) {
					HttpPostApi pos = new HttpPostApi();
					adicionarAlista(pos.insertCliente(obj, integracao.getHostHttp()).getStatusLine().toString(), obj);
				}
				if (obj[0].equals("02")) {
					HttpPostApi pos = new HttpPostApi();
					adicionarAlista(pos.insertBeneficiamento(obj, integracao.getHostHttp()).getStatusLine().toString(), obj);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void adicionarAlista(String status, String[] obj) {
		this.status.add(status);
		salvarLog(obj, status);
		PrimeFaces.current().ajax().update(":tbStatus");
	}
	
	private void salvarLog(String[] obj, String httpStatus) {
		System.out.println("Salvou um log");
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
	
	public void teste() {
		Messages.addGlobalError("Teste");
		PrimeFaces.current().ajax().update(":mensagem");
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
