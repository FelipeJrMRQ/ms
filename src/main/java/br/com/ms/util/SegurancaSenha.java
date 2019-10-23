package br.com.ms.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SegurancaSenha {

	public static String getMD5(String senha) throws NoSuchAlgorithmException {
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(senha.getBytes(), 0, senha.length());
			return (new BigInteger(1, m.digest()).toString(16).toUpperCase());
		} catch (NoSuchAlgorithmException e) {
			throw e;
		}

	}
}
