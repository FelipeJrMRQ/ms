package br.com.ms.nfe;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.ms.model.NotaRegistro;
import br.com.ms.model.Registro;
import br.com.ms.util.ConverteChaveDeAcesso;
import br.com.swconsultoria.certificado.exception.CertificadoException;
import br.com.swconsultoria.nfe.exception.NfeException;

public class MontaRegistroNfe {
	private static final String CNPJ = "CNPJ";
	private static final String NOME = "xNome";
	private static final String VALOR = "vNF";
	private static final String DATA = "dhEmi";
	private static final String SERIE = "serie";
	private static final String NFNUMERO = "nNF";

	/**
	 * Com base na chave de acesso realiza uma consulta na sefaz e monta um objeto
	 * do tipo NotaRegistro Apenas o registro deve ser atribuido manualmente;
	 * 
	 * @param chaveDeAcesso
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws NfeException
	 * @throws CertificadoException
	 * @throws ParseException
	 */
	public synchronized static NotaRegistro getNfe(String chaveDeAcesso, Registro registro) throws FileNotFoundException, IOException, NfeException, CertificadoException, ParseException {
		NotaRegistro nota = new NotaRegistro();
		String xml = "";
		if (chaveDeAcesso.length() == 44) {
			ConsultaNFeChaveDeAcesso consulta = new ConsultaNFeChaveDeAcesso();
			xml = consulta.converteParaXml(consulta.RealizaConsulta(chaveDeAcesso));
		}
		if (xml.length() > 1) {
			System.out.println(xml);
			String ixNome = "<xNome>";
			String idhEmi = "<dhEmi>";
			String fdhEmi = "</dhEmi>";
			String fxNome = "</xNome>";
			String iCNPJ = "<CNPJ>";
			String fCNPJ = "</CNPJ>";
			String viNF = "<vNF>";
			String vfNF = "</vNF>";
			String valor = xml.substring(xml.toLowerCase().indexOf(viNF.toLowerCase()) + 5, xml.toLowerCase().indexOf(vfNF.toLowerCase()));
			String cnpj = xml.substring(xml.toLowerCase().indexOf(iCNPJ.toLowerCase()) + 6, xml.toLowerCase().indexOf(fCNPJ.toLowerCase()));
			String nome = xml.substring(xml.toLowerCase().indexOf(ixNome.toLowerCase()) + 7, xml.toLowerCase().indexOf(fxNome.toLowerCase()));
			String dataCompleta = xml.substring(xml.toLowerCase().indexOf(idhEmi.toLowerCase()) + 7, xml.toLowerCase().indexOf(fdhEmi.toLowerCase()));
			String data = dataCompleta.substring(0, 10) + " " + dataCompleta.substring(11, 19);
			nota.setChave(chaveDeAcesso);
			nota.setCnpj(cnpj);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:m:s");
			nota.setEmissao(format.parse(data));
			nota.setNome(nome);
			nota.setNumeroNfe(ConverteChaveDeAcesso.getNumeroNfe(chaveDeAcesso));
			nota.setRegistro(registro);
			BigDecimal big = new BigDecimal(valor);
			nota.setValor(big);
			return nota;
		} else {
			nota.setNumeroNfe(ConverteChaveDeAcesso.getNumeroNfe(chaveDeAcesso));
			nota.setRegistro(registro);
			return nota;
		}
	}
	
	/**
	 * Realiza a montagem de um objeto NotaRegistro com base em um consulta ao arquivo de XML
	 * Localizado no servidor na pasta T:/sistema/nfe/xml
	 * @param registro
	 * @param chaveAcesso
	 * @return
	 * @throws Exception
	 */
	public synchronized NotaRegistro getNfe(Registro registro, String chaveAcesso) throws Exception {
		try {
			LeitorXmlNfe leitor = new LeitorXmlNfe();
			NotaRegistro nf = new NotaRegistro();
			File xml =  localizaArquivo(chaveAcesso);
			nf.setChave(chaveAcesso);
			nf.setCnpj(leitor.localizaTg(CNPJ, xml));
			nf.setNome(leitor.localizaTg(NOME, xml));
			String strData = (leitor.localizaTg(DATA, xml));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:m:s");
			nf.setEmissao(format.parse(strData.substring(0,10)+" "+strData.substring(11,19)));
			nf.setNumeroNfe((leitor.localizaTg(SERIE, xml)+"-"+leitor.localizaTg(NFNUMERO, xml)));
			nf.setRegistro(registro);
			BigDecimal big = new BigDecimal(leitor.localizaTg(VALOR, xml));
			nf.setValor(big);
			return nf;
		}catch(Exception e) {
			throw e;
		}
	}	

	/**
	 * Realiza a busca do arquivo nos diretorios dos ultimos dois meses do ano;
	 * @param chave
	 * @return
	 */
	private synchronized File localizaArquivo(String chave) {
		Calendar cal = Calendar.getInstance();
		try {
			if(consultaArquivo(chave, cal.get(Calendar.MONTH)+1) != null) {
				return consultaArquivo(chave, cal.get(Calendar.MONTH)+1);
			}else{
				return consultaArquivo(chave, (cal.get(Calendar.MONTH)));
			}
		}catch(Exception e) {
			throw e;
		}
	}
	
	
	/**
	 * Realiza a busca pelo arquivo no diretorio especificado
	 * @param chave
	 * @param mes
	 * @return
	 */
	private File consultaArquivo(String chave, int mes) {
		File diretorio = new File(montaPath(mes));
		File[] listFiles = diretorio.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().contains(chave); // apenas arquivos que começam com a letra "a"
			}
		});
		File f = null;
		for (File file : listFiles) {
			f = file;
		}
		return f;
	}
	
	/**
	 * Realiza a montagem do path que contem o arquivo de XML para consulta
	 * @param m
	 * @return
	 */
	private String montaPath(int m) {
		String mes = "";
		switch (m) {
		case 1:
			mes = "01";
			break;
		case 2:
			mes = "02";
			break;
		case 3:
			mes = "03";
			break;
		case 4:
			mes = "04";
			break;
		case 5:
			mes = "05";
			break;
		case 6:
			mes = "06";
			break;
		case 7:
			mes = "07";
			break;
		case 8:
			mes = "08";
			break;
		case 9:
			mes = "09";
			break;
		case 10:
			mes = "10";
			break;
		case 11:
			mes = "11";
			break;
		case 12:
			mes = "12";
			break;
		default:
			break;
		}
		Calendar cal = Calendar.getInstance();
		String path = "//125.67.2.242/SUPERSMART/nfe/XML/" + cal.get(Calendar.YEAR) + "_" + mes + "";
		return path;
		
	}
}
