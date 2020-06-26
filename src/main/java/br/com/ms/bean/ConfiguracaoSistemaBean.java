package br.com.ms.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.omnifaces.util.Messages;
import org.quartz.SchedulerException;

import br.com.controller.ConfiguracaoSistemaController;
import br.com.ms.model.ConfiguracaoSistema;

@ManagedBean
@ViewScoped
public class ConfiguracaoSistemaBean implements Serializable {

	private ConfiguracaoSistema config;
	private ConfiguracaoSistemaController controller;
	private static final long serialVersionUID = 4138601772155392087L;

	
	
	public ConfiguracaoSistemaBean() throws SchedulerException {
		config = new ConfiguracaoSistema();
		controller = new ConfiguracaoSistemaController();
		consultarConfiguracao();
	}
	
	public void salvarConfiguracao() {
		try {
			config = controller.salvarConfiguracao(config);
			Messages.addGlobalInfo("Alteração realizada com sucesso!");
		} catch (Exception e) {
			Messages.addGlobalError(e.getMessage());
		}
	}

	public void consultarConfiguracao() {
		try {
			config = controller.consultarConfiguracao();
		} catch (Exception e) {
			Messages.addGlobalError(e.getMessage());
		}
	}

	public ConfiguracaoSistema getConfig() {
		return config;
	}

	public void setConfig(ConfiguracaoSistema config) {
		this.config = config;
	}

}
