package br.com.ms.repository;

import java.util.Calendar;

import br.com.ms.dao.AtendimentoDao;
import br.com.ms.dao.LiberacaoDao;
import br.com.ms.dao.RegistroDao;
import br.com.ms.model.Atendimento;
import br.com.ms.model.Liberacao;
import br.com.ms.model.Registro;
import br.com.ms.model.Usuario;

public class LiberacaoRepository {

	private LiberacaoDao dao;
	private Liberacao lib;

	public LiberacaoRepository() {
		lib = new Liberacao();
		dao = new LiberacaoDao();
	}

	/**
	 * Realiza o salvamento de um registro de liberacao;
	 * 
	 * @param entrada
	 * @param saida
	 * @param ate
	 * @param user
	 */
	public LiberacaoRepository(Registro entrada, Registro saida, Atendimento atendimento, Usuario usuario) {
		lib = new Liberacao();
		dao = new LiberacaoDao();
		lib.setDataLiberacao(Calendar.getInstance().getTime());
		lib.setEntrada(entrada);
		lib.setSaida(saida);
		lib.setAtendimento(atendimento);
		lib.setUsuario(usuario);
		save();
	}

	/**
	 * Realiza a exclusao de um registro de saida para uma prestador de serviço do
	 * tipo não monitorado; É possivel perceber no método que não há exclusão para
	 * registro de saída esta condição se da pelo data de o relacionamento com a
	 * classe de liberacao; se do tipo cascade all sendo assim ao exluir uma
	 * Liberação automaticamente o registro de saída será excluido.
	 * 
	 * @param saida
	 */
	public void excluirLiberacaoNaoMonitorada(Registro saida) {
		Registro entrada = new Registro();
		RegistroDao rdao = new RegistroDao();
		Atendimento atendimento = new Atendimento();
		AtendimentoDao atDao = new AtendimentoDao();
		try {
			lib = dao.consultarPorIdDoRegistroDeSaida(saida.getId());
			entrada = rdao.consultaRegistroPeloId(lib.getEntrada().getId());
			atendimento = atDao.consultarAtentimentoPorId(lib.getAtendimento().getId());
			dao.excluirLiberacao(lib, atendimento, entrada);
		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * Para que este método funcione é necessário que sejam passados os parametros
	 * no construto da classe.
	 * 
	 * @param Entrada Atedimento Saida Usuario
	 * @return boolean
	 */
	private boolean save() {
		try {
			dao.salvarLiberacao(lib);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

}
