package br.com.controller;

import br.com.ms.dao.MotivoEdicaoRegistroDao;
import br.com.ms.model.MotivoEdicaoRegistro;

public class MotivoEdicaoRegistroController {

	private MotivoEdicaoRegistroDao motivoEdicaoRegistroDao;
	private MotivoEdicaoRegistro motivo;

	public MotivoEdicaoRegistroController() {
		//System.out.println("Motivo edição registro controller");
		motivoEdicaoRegistroDao = new MotivoEdicaoRegistroDao();
		motivo = new MotivoEdicaoRegistro();
	}

	public void salvar(MotivoEdicaoRegistro motivo) {
		try {
			motivoEdicaoRegistroDao.salvar(motivo);
		} catch (Exception e) {
			throw e;
		}
	}

	public MotivoEdicaoRegistro consultarMotivo(Long id) {
		try {
			return motivoEdicaoRegistroDao.consultar(id);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Realiza a consulta pelo ai do registro selecionado
	 * @param id
	 */
	public void excluirMotivoPorRegistro(Long id) {
		try {
			motivo = motivoEdicaoRegistroDao.consultar(id);
			if (motivo != null) {
				motivoEdicaoRegistroDao.excluir(motivo);
			}
		} catch (Exception e) {
			throw e;
		}
	}

}
