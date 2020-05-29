package br.com.ms.bean;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;

import org.omnifaces.util.Messages;

import br.com.controller.MotivoEdicaoRegistroController;
import br.com.ms.model.MotivoEdicaoRegistro;

@ManagedBean
@RequestScoped
public class MotivoEdicaoRegistroBean implements Serializable {
	private static final long serialVersionUID = -5461084474997228795L;
	private MotivoEdicaoRegistro motivo;
	private MotivoEdicaoRegistroController motivoController;
	
	public MotivoEdicaoRegistroBean() {
		motivo = new MotivoEdicaoRegistro();

	}
	
	public void salvar() {
		try {
			motivoController.salvar(motivo);
		}catch(Exception e ) {
			Messages.addGlobalError(e.getMessage());
		}
	}

	public MotivoEdicaoRegistro getMotivo() {
		return motivo;
	}

	public void setMotivo(MotivoEdicaoRegistro motivo) {
		this.motivo = motivo;
	}
	
}
