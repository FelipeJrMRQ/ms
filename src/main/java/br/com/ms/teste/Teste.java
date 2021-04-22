package br.com.ms.teste;

import java.util.ArrayList;
import java.util.List;

import br.com.ms.integracao.LeituraArquivo;

public class Teste {

	public static void main(String[] args) {
		LeituraArquivo la = new LeituraArquivo();
		List<String> lista = new ArrayList<>();
		lista = la.ler("\\\\125.67.2.242\\supersmart\\Supervisorio\\EXP20210420.TXT");
		for (String string : lista) {
			String[] obj = string.split(";");
				if(obj[0].equals("03")) {					
					System.out.println(obj[3]);
					System.out.println(obj[7]);
					System.out.println(obj[4]);
					System.out.println(obj[5]);
					System.out.println(obj[12]);
					System.out.println(obj[11]);
					System.out.println(obj[8]);
			}
		 System.out.println("----------------------");	
		}
	}

}
