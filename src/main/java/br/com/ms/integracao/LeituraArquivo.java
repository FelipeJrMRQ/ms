package br.com.ms.integracao;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import br.com.ms.dao.IntegracaoDao;
import br.com.ms.model.Integracao;
import br.com.ms.model.LogIntegracao;

/**
 * Classe reponsavel por realizar a abertura e leitura do arquivo txt
 * @author admin
 *
 */
public class LeituraArquivo implements Job {

	private List<String> linhas;
	private Integracao integracao;
	private IntegracaoDao dao;
	private List<String> status;
	private LogIntegracao logIntegracao;
	
	
	public LeituraArquivo() {
		status = new ArrayList<>();
		integracao = new Integracao();
		dao = new IntegracaoDao();
		integracao = dao.consultaParametros(1);
		logIntegracao = new LogIntegracao();
	}
	
	public List<String> ler(String localArquivo){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date data = Calendar.getInstance().getTime();
		String nomeArquivo = "EXP"+sdf.format(data).replaceAll("-","")+".TXT";
		linhas = new ArrayList<>();
		/*
		 * Realiza a leitura do arquivo TXT linha a linha e adiciona esta linha a uma lista de string	
		 */
		try (Stream<String> stream = Files.lines(Paths.get(localArquivo+nomeArquivo), StandardCharsets.ISO_8859_1)) {
			stream.forEach((String line) -> {
				this.linhas.add(line);
			});
		} catch (IOException e) {
			logIntegracao.setLog(e.getMessage());
			logIntegracao.setStatushttp("HTTP/1.1 500 BAD REQUEST" );
			logIntegracao.setData(Calendar.getInstance().getTime());
			dao.salvarLog(logIntegracao);
		}
		
		return this.linhas;
	}



	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("Teste");
		lerArquivo();
	}
	
	public void lerArquivo() {
		this.status = new ArrayList<>();
		List<String> dados = ler(integracao.getLocalArquivo());
		/**
		 * Realiza a l
		 */
		for (String string : dados) {
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
}
