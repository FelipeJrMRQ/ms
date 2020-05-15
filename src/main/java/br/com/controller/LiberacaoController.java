package br.com.controller;

import org.omnifaces.util.Messages;

import br.com.ms.dao.RegistroDao;
import br.com.ms.model.Atendimento;
import br.com.ms.model.Registro;
import br.com.ms.repository.LiberacaoRepository;
import br.com.ms.util.PermissoesUsuarios;

public class LiberacaoController {
	
	private RegistroDao registroDao;
	private Registro entrada;
	private Registro saida;
	
	public LiberacaoController() {
		registroDao = new RegistroDao();
	}
	
	/**
	 * Gera uma liberação de saida para que posteriormente seja realizada um
	 * conferencia e saida definitiva
	 * 
	 * @param a
	 * @param r
	 * @throws Exception
	 */
	public synchronized boolean gerarRegistroDeLiberacao(Atendimento atendimento, Registro saida) throws Exception {
		try {
			this.entrada = registroDao.consultaRegistroPeloId(atendimento.getRegistro().getId());
			this.saida = registroDao.consultaRegistroPeloId(saida.getId());
			new LiberacaoRepository(this.entrada, this.saida, atendimento, PermissoesUsuarios.getUsuario());
			return true;
		} catch (Exception ex) {
			Messages.addGlobalError(ex.getMessage());
			throw ex;
		}
	}
}
