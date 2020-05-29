package br.com.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.ms.dao.LiberacaoDao;
import br.com.ms.dao.RegistroDao;
import br.com.ms.model.Atendimento;
import br.com.ms.model.Liberacao;
import br.com.ms.model.Registro;
import br.com.ms.repository.LiberacaoRepository;
import br.com.ms.util.PermissoesUsuarios;

public class LiberacaoController {

	private RegistroDao registroDao;
	private Registro entrada;
	private Registro saida;
	private LiberacaoDao liberacaoDao;
	private List<Liberacao> liberacoes;

	public LiberacaoController() {
		registroDao = new RegistroDao();
		liberacaoDao = new LiberacaoDao();
		liberacoes = new ArrayList<>();
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
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void excluirLiberacaoSaida(Liberacao liberacao) {
		try {
			liberacaoDao.excluirLiberacao(liberacao);
		} catch (Exception e) {
			throw e;
		}
	}

	public List<Liberacao> consultaPorNome(Date dataInicial, Date dataFinal, String consulta) throws Exception {
		try {
			liberacoes = liberacaoDao.consultarPeloNomeDoPrestador(dataInicial, dataFinal, consulta);
			if(liberacoes.isEmpty()) {
				throw new Exception("Não foi possível encontrar registros com este nome");
			}
			return liberacoes;
		} catch (Exception e) {
			throw e;
		}
	}

	public Liberacao consultaPorId(Long id) {
		try {
			return liberacaoDao.consultaPorId(id);
		} catch (Exception e) {
			throw e;
		}
	}

	public List<Liberacao> consultaPelaPlacaVeiculo(Date dataInicial, Date dataFinal, String consulta) throws Exception {
		try {
			liberacoes = liberacaoDao.consultarPelaPlacaVeiculo(dataInicial, dataFinal, consulta);
			if(liberacoes.isEmpty()) {
				throw new Exception("Não foi possível encontrar registros com este número de placa!");
			}
			return liberacoes;
		} catch (Exception e) {
			throw e;
		}
	}

	public List<Liberacao> consultaPorEmpresa(Date dataInicial, Date dataFinal, String consulta) throws Exception {
		try {
			liberacoes = liberacaoDao.consultarPeloNomeDaEmpresa(dataInicial, dataFinal, consulta);
			if(liberacoes.isEmpty()) {
				throw new Exception("Não foi possível encontrar registros com este nome de empresa!");
			}
			return liberacoes;
		} catch (Exception e) {
			throw e;
		}
	}

	public List<Liberacao> consultaPorCategoria(Date dataInicial, Date dataFinal, String consulta) throws Exception {
		try {
			liberacoes = liberacaoDao.consultarPorCategoria(dataInicial, dataFinal, consulta);
			if(liberacoes.isEmpty()) {
				throw new Exception("Não foi possível encontrar registros!");
			}
			return liberacoes;
		} catch (Exception e) {
			throw e;
		}
	}

	public List<Liberacao> consultaPelaNotaDeEntrada(String numeroNF) throws Exception {
		try {
			liberacoes = liberacaoDao.consultarPeloNumeroNfEntrada(numeroNF);
			if(liberacoes.isEmpty()) {
				throw new Exception("Não foi possível encontrar registros com este número de nota!");
			}
			return liberacoes;
		} catch (Exception e) {
			throw e;
		}
	}

	public List<Liberacao> consultaPelaNotaDeSaida(String numeroNF) throws Exception {
		try {
			liberacoes = liberacaoDao.consultarPeloNumeroNfSaida(numeroNF);
			if(liberacoes.isEmpty()) {
				throw new Exception("Não foi possível encontrar registros com este número de notas!");
			}
			return liberacoes;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public Liberacao consultarPeloIdDeSaida(Long id) {
		try {
			return liberacaoDao.consultarPorIdDoRegistroDeSaida(id);
		} catch (Exception e) {
			throw e;
		}
	}

	public Liberacao consultarLiberacao(Long id, String tipo) {
		try {
			if (tipo.equals("SAIDA") || tipo.equals("LIBERADO")) {
				return liberacaoDao.consultarPorIdDoRegistroDeSaida(id);
			} else {
				return liberacaoDao.consultarPorIdDoRegistroDeEntrada(id);
			}
		} catch (Exception e) {
			throw e;
		}
	}

}
