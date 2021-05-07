package br.com.ms.nfe;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import br.com.ms.util.Seguranca;
import br.com.swconsultoria.certificado.Certificado;
import br.com.swconsultoria.certificado.CertificadoService;
import br.com.swconsultoria.certificado.exception.CertificadoException;
import br.com.swconsultoria.nfe.Nfe;
import br.com.swconsultoria.nfe.dom.ConfiguracoesNfe;
import br.com.swconsultoria.nfe.dom.enuns.AmbienteEnum;
import br.com.swconsultoria.nfe.dom.enuns.ConsultaDFeEnum;
import br.com.swconsultoria.nfe.dom.enuns.DocumentoEnum;
import br.com.swconsultoria.nfe.dom.enuns.EstadosEnum;
import br.com.swconsultoria.nfe.dom.enuns.PessoaEnum;
import br.com.swconsultoria.nfe.exception.NfeException;
import br.com.swconsultoria.nfe.schema.retdistdfeint.RetDistDFeInt;
import br.com.swconsultoria.nfe.schema.retdistdfeint.RetDistDFeInt.LoteDistDFeInt.DocZip;
import br.com.swconsultoria.nfe.schema_4.retConsSitNFe.TRetConsSitNFe;
import br.com.swconsultoria.nfe.util.XmlNfeUtil;

public class ConsultaNFeChaveDeAcesso {

	public ConsultaNFeChaveDeAcesso() {
	
	}

	private ConfiguracoesNfe iniciaConfig() throws FileNotFoundException, CertificadoException {
		Certificado certificado = certifidoA1Pfx();
		return ConfiguracoesNfe.criarConfiguracoes(EstadosEnum.SP, AmbienteEnum.PRODUCAO, certificado, "/temp/schemas");
	}

	private static Certificado certifidoA1Pfx() throws CertificadoException, FileNotFoundException {
		String caminhoCertificado = Seguranca.getConfig().getCaminhoCertificado();
		String senha = Seguranca.decrypt(Seguranca.getConfig().getSenhaCertificado(), Seguranca.getConfig().getChaveEncrypt());
		return CertificadoService.certificadoPfx(caminhoCertificado, senha);
	}

	public RetDistDFeInt RealizaConsulta(String chave) throws FileNotFoundException, NfeException, CertificadoException {
		return (Nfe.distribuicaoDfe(iniciaConfig(), PessoaEnum.JURIDICA, "07347306000320", ConsultaDFeEnum.CHAVE, chave));
	}
	
	public TRetConsSitNFe consultaXML(String chave) throws FileNotFoundException, NfeException, CertificadoException {
		return (Nfe.consultaXml(iniciaConfig(), chave, DocumentoEnum.NFE));
	}

	public String converteParaXml(RetDistDFeInt retorno) throws IOException {
		if (retorno.getXMotivo().equals("Documento localizado")) {
			String xml = "";
			List<DocZip> list = retorno.getLoteDistDFeInt().getDocZip();
			for (DocZip docZip : list) {
				xml = XmlNfeUtil.gZipToXml(docZip.getValue());
			}
			return xml;
		} else {
			return "";
		}
	}
	
	public String converteParaXml(TRetConsSitNFe retorno) throws IOException {
		return retorno.toString();
	}
}
