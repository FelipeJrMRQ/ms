package br.com.ms.agendador;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.omnifaces.util.Messages;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import br.com.ms.dao.NotaResgitroDao;
import br.com.ms.model.NotaRegistro;
import br.com.ms.nfe.MontaRegistroNfe;
import br.com.swconsultoria.certificado.exception.CertificadoException;
import br.com.swconsultoria.nfe.exception.NfeException;

/**
 * 
 * @author admin
 * 
 * Com esta classe é possível definir as terefas que serão executadas periodicamento
 * Para isso basta incluir as funções desejadas no metodos execute()
 *
 */
public class Tarefa implements Job {

	Date instanteAtual;
	private List<NotaRegistro> notas;
	private NotaResgitroDao nDao;
	private NotaRegistro novaNota;
	private Date dataAtual;
	private static final String SAIDA = "SAIDA";
	private static final String ENTRADA = "ENTRADA";

	public Tarefa() {
		notas = new ArrayList<NotaRegistro>();
		nDao = new NotaResgitroDao();
		novaNota = new NotaRegistro();
		dataAtual = Calendar.getInstance().getTime();
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			atualizarNotasDeEntrada();
			atualizaNotasDeSaida();
		} catch (Exception e) {
			throw e;
		}
	}

	public void atualizarNotasDeEntrada() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		notas = nDao.consultaNotasEntradaNaoSincronizada(sdf.format(dataAtual), ENTRADA);
		for (NotaRegistro nrg : notas) {
			if (nrg.getCnpj() == null) {
				try {
					novaNota = MontaRegistroNfe.getNfe(nrg.getChave().trim(), nrg.getRegistro());
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
	}

	public void atualizaNotasDeSaida() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		notas = nDao.consultaNotasEntradaNaoSincronizada(sdf.format(dataAtual), SAIDA);
		for (NotaRegistro nrg : notas) {
			if (nrg.getCnpj() == null) {
				try {
					MontaRegistroNfe montaNota = new MontaRegistroNfe();
					novaNota = montaNota.getNfe(nrg.getRegistro(), nrg.getChave());
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
	}
}
