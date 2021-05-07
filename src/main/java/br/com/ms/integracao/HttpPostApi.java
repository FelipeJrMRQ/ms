package br.com.ms.integracao;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;


/**
 * Classe utilizada para integração Supesmart/Bitqualy 
 * Servidor de destino http://www.cromart.bitqualy.tech/*
 * 
 * @author admin
 *
 */
public class HttpPostApi {

	private URI uri; 
	private HttpResponse httpResponse;
	private HttpGet httpGet;
	private HttpClient httpClient;
	
	/**
	 * Realiza a inserção dos dados da cadastro de produtos obtidos através de um
	 * arquivo TXT exportado pelo supersmart
	 * 
	 * @param produtos
	 * @param host
	 * @return
	 */
	public HttpResponse insertProduto(String[] produtos,String host) {
		try {
			byte[] b = produtos[7].replaceAll("\"", "").getBytes("ISO-8859-1");
			String p =  new String(b, "ISO-8859-1");
			uri = new URIBuilder()
					.setScheme("http")
					.setHost("cromart.bitqualy.tech")
					.setPath("/ws_insert_cad_produto.php")
					.addParameter("cod_produto", produtos[3])
					.addParameter("nome_produto", p)
					.addParameter("cod_cliente", produtos[4])
					.addParameter("cod_benefic", produtos[5])
					.addParameter("area_produto", produtos[12].replaceAll(",", "."))
					.addParameter("peso_produto", produtos[11].replaceAll(",", "."))
					.addParameter("valor_produto", produtos[8].replaceAll(",", "."))
					.build();
					httpClient = HttpClients.createDefault();
					httpGet = new HttpGet(uri);
					this.httpResponse = httpClient.execute(httpGet);
		} catch (URISyntaxException   | IOException e) {
			e.printStackTrace();
		}
		return httpResponse;
	}
	
	/**
	 * Realiza a inserção dos dados de cadastro de clientes obtidos através de um 
	 * arquivo TXT exportado pelo sistema de ERP supersmart
	 * @param clientes
	 * @param host
	 * @return
	 */
	public HttpResponse insertCliente(String[] clientes,String host) {
		try {
			byte[] b = clientes[4].replaceAll("\"", "").getBytes("ISO-8859-1");
			String t =  new String(b, "ISO-8859-1");
			uri = new URIBuilder()
					.setScheme("http")
					.setHost("cromart.bitqualy.tech")
					.setPath("/ws_insert_cad_cliente.php")
					.addParameter("cod_cliente", clientes[3])
					.addParameter("nome_cliente", t)
					.build();
					httpClient = HttpClients.createDefault();
					httpGet = new HttpGet(uri);
					this.httpResponse = httpClient.execute(httpGet);	
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
		return httpResponse;
	}
	
	/**
	 * Realiza a inserção dos dados de cadastro de beneficiamento obtidos de um
	 * arquivo TXT exportado pelo sistema ERP supersmart 
	 * @param beneficiamento
	 * @param host
	 * @return
	 */
	public HttpResponse insertBeneficiamento(String[] beneficiamento,String host) {
		try {
			byte[] b = beneficiamento[4].replaceAll("\"", "").getBytes("ISO-8859-1");
			String t =  new String(b, "ISO-8859-1");
			uri = new URIBuilder()
					.setScheme("http")
					.setHost("cromart.bitqualy.tech")
					.setPath("/ws_insert_cad_benefic.php")
					.addParameter("cod_benefic", beneficiamento[3])
					.addParameter("nome_benefic", t)
					.build();
					httpClient = HttpClients.createDefault();
					httpGet = new HttpGet(uri);
					this.httpResponse = httpClient.execute(httpGet);
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
		return this.httpResponse;
	}
	 
}
