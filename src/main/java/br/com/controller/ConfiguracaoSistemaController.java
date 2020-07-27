package br.com.controller;

import java.io.Serializable;
import br.com.ms.dao.ConfiguracaoSistemaDao;
import br.com.ms.model.ConfiguracaoSistema;
import br.com.ms.util.ScheduleUtil;

public class ConfiguracaoSistemaController implements Serializable {

	private static final long serialVersionUID = 145018023148714228L;
	private ConfiguracaoSistemaDao configDao;
	private static boolean status = true;

	public ConfiguracaoSistemaController() {
		configDao = new ConfiguracaoSistemaDao();
	}

	public void iniciarAtualizacaoDeNotas() {
		if (status) {
			try {
				ScheduleUtil.start();
				status = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public ConfiguracaoSistema consultarConfiguracao() {
		return configDao.consultarConfiguracoes(1L);
	}

	public ConfiguracaoSistema salvarConfiguracao(ConfiguracaoSistema config) {
		try {
			return configDao.salvarConfig(config);
		} catch (Exception e) {
			throw e;
		}
	}
}
