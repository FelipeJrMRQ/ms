package br.com.ms.teste;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.List;

import br.com.ms.integracao.LeituraArquivo;

public class Teste{
	
	public static void main(String[] args) throws KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, Exception{
		List<String> lista = new ArrayList<>();
		String[] obj= null;
		List<String[]> produtos = new ArrayList<>();
		List<String[]> clientes = new ArrayList<>();
		List<String[]> beneficiamento = new ArrayList<>();
		LeituraArquivo la = new LeituraArquivo();
		
		lista= la.ler("\\\\125.67.2.242\\supersmart\\supervisorio\\");
				
				/*
				 * Arrays.asList(
				"03;1;06052021072530;12542;38;120;1;\"PROT1601SB- 24040876 / PVR 24040872\";0,0000;0,00;0,00;1,4500;0,1960;AÇO;", 
				"03;1;06052021093447;9121;1281;1;1;\"ES000007F000219PI#A- CJ. TUBO DE AÇO DO FREIO H - \";2,7100;20,00;25,00;0,4840;0,0580;;", 
				"03;1;06052021090211;12549;1054;25;1;\"10000004050020\";0,0000;0,00;0,00;0,0000;0,0000;;", 
				"03;1;06052021073526;12547;1054;25;1;\"87000032940030 870270329400 SUPORTE DO MOTOR LD\";0,0000;0,00;0,00;0,0000;0,0000;;", 
				"03;1;06052021094400;8848;1281;1;136;\"ES000007F000272PI#D- TUBE/A, RHS LOWER - 87647619\";0,0000;20,00;25,00;2,2180;0,1500;;", 
				"01;1;06052021095258;17;\"LEPE INDÚSTRIA E COMÉRCIO LTDA\";60678471000185",
				"01;1;06052021095459;92;\"COM. DE TINTAS MACHADO LTDA\";49444870000149",
				"01;1;06052021100828;93;\"TOSCO FERRAGENS E FERRAMENTAS LTDA-ME\";04280155000180",
				"03;1;06052021101019;8850;1281;1;1;\"ES000007F000268PI#C- TUBE/A LHS LOWER FORMED - 876\";0,0000;20,00;25,00;1,3220;0,0850;;",
				"03;1;06052021101114;12549;1054;25;1;\"10000004050020- 10027004050030- PANEL ASM-COWL FRT\";0,0000;0,00;0,00;2,3380;0,7800;ZINCO;",
				"03;1;06052021101712;9245;1281;1;10;\"ES000007F000304PI#C- TUBE, HYD. W. LIFT ARM LOW - \";0,0000;20,00;25,00;1,9480;0,2000;;");
				 */
		
		
		
		@SuppressWarnings("unused")
		int i =0;
		
		for (String string : lista) {
			i++;
			obj = string.split(";");
			if(obj[0].equals("03")) {
				produtos.add(obj);
			}else if(obj[0].equals("01")) {
				clientes.add(obj);
			}else if(obj[0].equals("02")) {
				beneficiamento.add(obj);
			}
		}
		
		
		System.out.println("-------------------------------------------------");
		System.out.println("CLIENTE");
		
		
		for (String[] strings : clientes) {
			System.out.println("-------------------------------------------------");
			System.out.println("ID: "+strings[3]);
			System.out.println("NOME: "+strings[4].replaceAll("\"", ""));
		}
		
		System.out.println("-------------------------------------------------");
		System.out.println("BENEFICIAMENTO");
	
		
		for (String[] strings : beneficiamento) {
			System.out.println("-------------------------------------------------");
			System.out.println(strings[3]);
			System.out.println(strings[4].replaceAll("\"", ""));
		}
		
		System.out.println("-------------------------------------------------");
		System.out.println("PRODUTO");
	
		
		for (String[] strings : produtos) {
			System.out.println("-------------------------------------------------");
			System.out.println(strings[3]);
			System.out.println(strings[7].replaceAll("\"", ""));
			System.out.println(strings[4]);
			System.out.println(strings[5]);
			System.out.println(strings[12]);
			System.out.println(strings[11]);
			System.out.println(strings[8]);
		}
	}
}
