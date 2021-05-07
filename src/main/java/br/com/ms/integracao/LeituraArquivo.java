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
 * 
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

	/**
	 * Recebe o caminho onde o arquivo se encontra no servidor verifica se o arquivo
	 * existe em caso de verdadeiro e retonada uma lista com todos os dados contidos
	 * no arquivo TXT
	 * 
	 * Em caso de erro o sistema fará o registro de uma bad request com status 500
	 * no banco de dados na tabela de logintegração
	 * 
	 * @param localArquivo
	 * @return
	 */
	public List<String> ler(String localArquivo) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date data = Calendar.getInstance().getTime();
		String nomeArquivo = "EXP" + sdf.format(data).replaceAll("-", "") + ".TXT";
		linhas = new ArrayList<>();
		/*
		 * Realiza a leitura do arquivo TXT linha a linha e adiciona esta linha a uma
		 * lista de string
		 */
		try (Stream<String> stream = Files.lines(Paths.get(localArquivo + nomeArquivo), StandardCharsets.ISO_8859_1)) {
			stream.forEach((String line) -> {
				this.linhas.add(line);
			});
		} catch (IOException e) {
			logIntegracao.setLog(e.getMessage());
			logIntegracao.setStatushttp("HTTP/1.1 500 BAD REQUEST");
			logIntegracao.setData(Calendar.getInstance().getTime());
			dao.salvarLog(logIntegracao);
		}
		return this.linhas;
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		lerArquivo();
	}

	/**
	 * Executa e leitura do arquivo TXT localizado na pasta do sistema
	 * Separa os objetos de acordo com o código contido na posição [0] do array
	 * 
	 * Os códigos representam as seguintes categorias
	 * 
	 * [01] - CLIENTE
	 * [02] - BENEFICIAMENTO
	 * [03] - PRODUTO
	 */
	public void lerArquivo() {
		LeituraArquivo la = new LeituraArquivo();
		List<String> lista = la.ler(integracao.getLocalArquivo());
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

	
	/**
	 * Realiza e verificação da listagem e faz a chamada dos métodos de integração.
	 *  
	 * @param clientes
	 * @param produtos
	 * @param beneficiamentos
	 */
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
	
	/**
	 * Salva o status do processamento no banco de dados para 
	 * futuras consultas.
	 * 
	 * @param status
	 * @param obj
	 */
	public void adicionarAlista(String status, String[] obj) {
		this.status.add(status);
		salvarLog(obj, status);
	}
	
	/**
	 * Transmite os dados do cliente para integração;
	 * 
	 * @param clientes
	 */
	private void postCliente(List<String[]> clientes) {
		for(String[] cliente: clientes) {
			HttpPostApi pos = new HttpPostApi();
			adicionarAlista(pos.insertCliente(cliente, integracao.getHostHttp()).getStatusLine().toString(), cliente);
		}
	}
	
	/**
	 * Transmite os dados do produto para integração
	 * @param produtos
	 */
	private void postProduto(List<String[]> produtos) {
		for(String[] produto: produtos) {
			HttpPostApi pos = new HttpPostApi();
			adicionarAlista(pos.insertProduto(produto, integracao.getHostHttp()).getStatusLine().toString(), produto);
		}
	}
	
	/**
	 * Transmite os dados beneficiamento para integrção
	 * 
	 * @param beneficiamentos
	 */
	private void postBeneficiamento(List<String[]> beneficiamentos) {
		for(String[] beneficiamento: beneficiamentos) {
			HttpPostApi pos = new HttpPostApi();
			adicionarAlista(pos.insertBeneficiamento(beneficiamento, integracao.getHostHttp()).getStatusLine().toString(), beneficiamento);
		}
	}
	
	/**
	 * Mantem uma tabela no banco de dados com o status das requisições sendo elas:
	 * SUCESSO = 200 OK
	 * FALHA = 500 BAD REQUEST
	 * 
	 * @param obj
	 * @param httpStatus
	 */
	private void salvarLog(String[] obj, String httpStatus) {
		try {
			if (obj[0].equals("01")) {
				logIntegracao.setLog("CLIENTE COD: " + obj[3] + " NOME: " + obj[4].replaceAll("\"", ""));
				logIntegracao.setStatushttp(httpStatus);
				logIntegracao.setData(Calendar.getInstance().getTime());
				dao.salvarLog(logIntegracao);
			}
			if (obj[0].equals("02")) {
				logIntegracao.setLog("BENEFICIAMENTO COD: " + obj[3] + " NOME: " + obj[4].replaceAll("\"", ""));
				logIntegracao.setStatushttp(httpStatus);
				logIntegracao.setData(Calendar.getInstance().getTime());
				dao.salvarLog(logIntegracao);
			}
			if (obj[0].equals("03")) {
				logIntegracao.setLog("PRODUTO COD: " + obj[3] + " NOME: " + obj[7].replaceAll("\"", ""));
				logIntegracao.setStatushttp(httpStatus);
				logIntegracao.setData(Calendar.getInstance().getTime());
				dao.salvarLog(logIntegracao);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
