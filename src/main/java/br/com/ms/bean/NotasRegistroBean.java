package br.com.ms.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;

import br.com.ms.dao.NotaResgitroDao;
import br.com.ms.model.NotaRegistro;

@ManagedBean
@ViewScoped
public class NotasRegistroBean implements Serializable {

	private static final long serialVersionUID = 3564742235126759861L;

	
	private NotaRegistro notaRegistro;
	private NotaResgitroDao notaResgitroDao;

	public NotasRegistroBean() {
		notaRegistro = new NotaRegistro();
		notaResgitroDao = new NotaResgitroDao();
	}
	
	public void excluirNotasRegistro(ActionEvent event) {
		try {
			notaRegistro = (NotaRegistro) event.getComponent().getAttributes().get("notaSelecionada");
			notaResgitroDao.excluirNotasEntrada(notaRegistro);
		}catch(RuntimeException erro) {
			Messages.addGlobalError(erro.getCause().getMessage());
		}
	}
	
	public NotaRegistro getNotasRegistroEntrada() {
		return notaRegistro;
	}

	public void setNotasRegistroEntrada(NotaRegistro notaRegistro) {
		this.notaRegistro = notaRegistro;
	}
	
	
}
