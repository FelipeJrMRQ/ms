package br.com.ms.util;

public class ConverteChaveDeAcesso {
	private static String ch;

	public static String getNumeroNfe(String chave) {
		return validaChaveDeAcesso(chave);
		
	}

	private static String validaChaveDeAcesso(String chave) throws NumberFormatException {
		if(chave.length() == 44) {
			int n = 0;
			int s = 0;
			String serie = chave.substring(22,25);
			String nf = chave.substring(25, 34);
			try {
				s = Integer.parseInt(serie);
				n = Integer.parseInt(nf);
			}catch(NumberFormatException e) {
				throw e;
			}
			
		 ch = String.valueOf(s +"-"+ n );
		}else {
			ch = chave;
		}
		
		return ConverteChaveDeAcesso.ch;
	}
}
