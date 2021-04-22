package br.com.controller;

import java.io.Serializable;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;

import br.com.ms.agendador.Tarefa;
import br.com.ms.dao.ConfiguracaoSistemaDao;
import br.com.ms.dao.IntegracaoDao;
import br.com.ms.integracao.LeituraArquivo;
import br.com.ms.model.ConfiguracaoSistema;
import br.com.ms.model.Integracao;
import br.com.ms.util.ScheduleUtil;

/**
 * 
 * @author admin Permite o carregamento de configuração do sistema bem como o
 *         agendamento de terafas para execuções periodidas
 *
 */
public class ConfiguracaoSistemaController implements Serializable {

	private static final long serialVersionUID = 145018023148714228L;
	private ConfiguracaoSistemaDao configDao;
	private IntegracaoDao integraDao;
	private static boolean status = true;

	public ConfiguracaoSistemaController() {
		configDao = new ConfiguracaoSistemaDao();
		integraDao = new IntegracaoDao();
	}

	/**
	 * Inicia o agendamento das tarefas que serão periodicamente executadas conforme
	 * os parametros passados.
	 */
	public void iniciarAgendamentos() {
		Integracao params = new Integracao();
		params = consultaConfigIntegracao();
		if (status) {
			try {
				JobDetail jb = JobBuilder.newJob(Tarefa.class).withIdentity("NFE").build();
				ScheduleUtil.start(jb, "0 0/30 * * * ?", "g1", "triggerNfe");

				JobDetail jb1 = JobBuilder.newJob(LeituraArquivo.class).withDescription("integracao").build();
				ScheduleUtil.start(jb1, params.getCronParametros(), "g2", "triggerInteg");
				status = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public ConfiguracaoSistema consultarConfiguracao() {
		return configDao.consultarConfiguracoes(1L);
	}

	public Integracao consultaConfigIntegracao() {
		return integraDao.consultaParametros(1);
	}

	public ConfiguracaoSistema salvarConfiguracao(ConfiguracaoSistema config) {
		try {
			return configDao.salvarConfig(config);
		} catch (Exception e) {
			throw e;
		}
	}
}
