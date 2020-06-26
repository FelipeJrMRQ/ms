package br.com.ms.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import br.com.controller.ConfiguracaoSistemaController;
import br.com.ms.model.ConfiguracaoSistema;

public class Seguranca {

	private static ConfiguracaoSistemaController controller = new ConfiguracaoSistemaController();
	private static ConfiguracaoSistema configuracaoSistema = new ConfiguracaoSistema();
	private static SecretKeySpec chaveSecreta;
	private static byte[] chave;

	public Seguranca() {
	}

	private static void setKey(String minhaChave) {
		MessageDigest sha = null;
		try {
			chave = minhaChave.getBytes("UTF-8");
			sha = MessageDigest.getInstance("SHA-1");
			chave = sha.digest(chave);
			chave = Arrays.copyOf(chave, 16);
			chaveSecreta = new SecretKeySpec(chave, "AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static String encrypt(String texto, String chave) {
		try {
			setKey(chave);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, chaveSecreta);
			return Base64.getEncoder().encodeToString(cipher.doFinal(texto.getBytes("UTF-8")));
		} catch (Exception e) {
			System.out.println("Error while encrypting: " + e.toString());
		}
		return null;
	}

	public static String decrypt(String strToDecrypt, String secret) {
		try {
			setKey(secret);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, chaveSecreta);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		} catch (Exception e) {
			System.out.println("Error while decrypting: " + e.toString());
		}
		return null;
	}

	public static ConfiguracaoSistema getConfig() {
		configuracaoSistema = controller.consultarConfiguracao();
		return configuracaoSistema;
	}
}
