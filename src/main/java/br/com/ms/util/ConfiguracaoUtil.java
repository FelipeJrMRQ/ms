package br.com.ms.util;

import br.com.controller.ConfiguracaoSistemaController;
import br.com.ms.model.ConfiguracaoSistema;

public class ConfiguracaoUtil {
	
	private static ConfiguracaoSistemaController configController;
	
	public ConfiguracaoUtil() {
		configController = new ConfiguracaoSistemaController();
	}
	
	public static ConfiguracaoSistema consultaConfiguracao() {
		return configController.consultarConfiguracao();
	}
}
