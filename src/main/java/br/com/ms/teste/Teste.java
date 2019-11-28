package br.com.ms.teste;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.omnifaces.util.Messages;

import br.com.ms.dao.NotaResgitroDao;
import br.com.ms.model.NotaRegistro;
import br.com.ms.nfe.MontaRegistroNfe;
import br.com.swconsultoria.certificado.exception.CertificadoException;
import br.com.swconsultoria.nfe.exception.NfeException;

public class Teste {
	public static void main(String[] args) {
		long ini = Calendar.getInstance().getTimeInMillis();
		NotaRegistro novaNota = new NotaRegistro();
		NotaResgitroDao nDao = new NotaResgitroDao();
		List<NotaRegistro> nr = new ArrayList<NotaRegistro>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date data = null;
		nr = nDao.consultaNotasEntradaNaoSincronizada(sdf.format(data), "ENTRADA");
		
		for (NotaRegistro nrg : nr) {
			if (nrg.getCnpj() == null) {
				try {
					novaNota = MontaRegistroNfe.getNfe(nrg.getChave().trim(), nrg.getRegistro());
					System.out.println(nrg.getId());
					nrg.setNome(novaNota.getNome());
					nrg.setNumeroNfe(novaNota.getNumeroNfe());
					nrg.setCnpj(novaNota.getCnpj());
					nrg.setEmissao(novaNota.getEmissao());
					nrg.setValor(novaNota.getValor());
					nDao.alterar(nrg);
				} catch (FileNotFoundException e) {
					Messages.addGlobalFatal("Não atualizado tente novamente");
				} catch (IOException e) {
					Messages.addGlobalFatal("Não atualizado tente novamente");
				} catch (NfeException e) {
					Messages.addGlobalFatal("Não atualizado tente novamente");
				} catch (CertificadoException e) {
					Messages.addGlobalFatal("Não atualizado tente novamente");
				} catch (ParseException e) {
					Messages.addGlobalFatal("Não atualizado tente novamente");
				} catch (Exception e) {
					Messages.addGlobalFatal(e.getMessage());
				}
			}
		}
		long fim = Calendar.getInstance().getTimeInMillis();
		System.out.println("Tempo: " + (fim - ini) + " ms");

	}
}
