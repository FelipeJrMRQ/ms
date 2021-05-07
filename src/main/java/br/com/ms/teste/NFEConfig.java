package br.com.ms.teste;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import com.fincatto.documentofiscal.DFUnidadeFederativa;
import com.fincatto.documentofiscal.nfe.NFeConfig;

public class NFEConfig extends NFeConfig{

	 private KeyStore keyStoreCertificado = null;
	 private KeyStore keyStoreCadeia = null;
	
	
	@Override
	public DFUnidadeFederativa getCUF() {
		return DFUnidadeFederativa.SP;
	}

	@Override
	public KeyStore getCertificadoKeyStore() throws KeyStoreException {
		 if (this.keyStoreCertificado == null) {
	            this.keyStoreCertificado = KeyStore.getInstance("PKCS12");
	            try (InputStream certificadoStream = new FileInputStream("C:\\temp\\2021_eCNPJ.pfx")) {
	                this.keyStoreCertificado.load(certificadoStream, this.getCertificadoSenha().toCharArray());
	            } catch (CertificateException | NoSuchAlgorithmException | IOException e) {
	                this.keyStoreCadeia = null;
	                throw new KeyStoreException("Nao foi possibel montar o KeyStore com a cadeia de certificados", e);
	            }
	        }
	        return this.keyStoreCertificado;
	}

	@Override
	public String getCertificadoSenha() {
		return "12345678";
	}

	@Override
	public KeyStore getCadeiaCertificadosKeyStore() throws KeyStoreException {
		  if (this.keyStoreCadeia == null) {
	            this.keyStoreCadeia = KeyStore.getInstance("JKS");
	            try (InputStream cadeia = new FileInputStream("C:\\temp\\certificado.jks")) {
	                this.keyStoreCadeia.load(cadeia, this.getCadeiaCertificadosSenha().toCharArray());
	            } catch (CertificateException | NoSuchAlgorithmException | IOException e) {
	                this.keyStoreCadeia = null;
	                throw new KeyStoreException("Nao foi possibel montar o KeyStore com o certificado", e);
	            }
	        }
	        return this.keyStoreCadeia;
	}

	@Override
	public String getCadeiaCertificadosSenha() {
		return "12345678";
	}

}
