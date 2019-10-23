package br.com.ms.teste;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.swconsultoria.certificado.exception.CertificadoException;
import br.com.swconsultoria.nfe.exception.NfeException;

public class Teste {

	public static void main(String[] args) throws FileNotFoundException, IOException, NfeException, CertificadoException, ParseException, InterruptedException {
//		List<NotaRegistro> lista = new ArrayList<>();
//		NotaResgitroDao dao = new NotaResgitroDao();
//		RegistroDao rDao = new RegistroDao();
//		MontaRegistroNfe registroNfe = new MontaRegistroNfe();
//
//		lista = dao.consultaNotasNaoSincrozizadas("2019-09-26 10:00:00");
//		List<NotaRegistro> novaLista = new ArrayList<>();
//	
//		
//		System.out.println("Lista 1 "+lista.size());
//		
//		for (NotaRegistro notaRegistro : lista) {
//			//Thread.sleep(20*1000);
//			if(!notaRegistro.getChave().isEmpty()&& notaRegistro.getChave().length() == 44 && notaRegistro.getRegistro().getTipo().equals("ENTRADA")) {
//				System.out.println(notaRegistro.getId() + " - " + notaRegistro.getRegistro().getId() + " - " + notaRegistro.getRegistro().getData() + " - "+ notaRegistro.getChave());
//				try {
//					NotaRegistro nota = new NotaRegistro();
//					nota = MontaRegistroNfe.getNfe(notaRegistro.getChave(), notaRegistro.getRegistro());
//					novaLista.add(nota);
//					Thread.sleep(10*1000);
//				}catch(Exception e) {
//					System.out.println(e);
//				}
//			}
//		}
//		
//		for (NotaRegistro notaRegistro : novaLista) {
//			Registro r = new Registro();
//			r = notaRegistro.getRegistro();
//			rDao.alterarRegistro(r);
//			System.out.println("FIM");
//		}
//		
		
		Date data = Calendar.getInstance().getTime();
		
		System.out.println(data);
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		System.out.println(format.format(data));
		
	}
}
