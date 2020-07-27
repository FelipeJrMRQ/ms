package br.com.controller;

import java.util.Calendar;
import java.util.List;

import br.com.ms.dao.RegistroDao;
import br.com.ms.model.Atendimento;
import br.com.ms.model.Empresa;
import br.com.ms.model.Liberacao;
import br.com.ms.model.NotaRegistro;
import br.com.ms.model.Registro;
import br.com.ms.model.Visitante;
import br.com.ms.util.HoraDaInternet;
import br.com.ms.util.PermissoesUsuarios;

public class RegistroController {
	
	private RegistroDao registroDao;

	/**
	 * Realiza um verificao no banco para verificar o se o regitro não possui
	 * vinculos e pode ser excluido.
	 * 
	 * @param r
	 * @return
	 * @throws Exception
	 */
	private void verificaPermissaoExclusao(Registro r) throws Exception {
		getMotivoController().excluirMotivoPorRegistro(r.getId());
		r = registroDao.consultaRegistroPeloId(r.getId());
		if (r.getStatus().equals("ABERTO")) {
			registroDao.excluir(r);
		} else {
			throw new Exception("Não é possivel excluir o registro porque ele está em atendimento!");
		}
	}

	public RegistroController() {
		//System.out.println("Registro Controller");
		registroDao = new RegistroDao();
	}

	public Registro salvarRegistro(Registro registro) {
		try {
			return registroDao.salvar(registro);
		} catch (Exception e) {
			throw e;
		}
	}

	public void excluirRegistro(Registro registro) throws Exception {
		verificaPermissaoExclusao(registro);
	}

	public AtendimentoController getAtendimentoController() {
		AtendimentoController controller = new AtendimentoController();
		return controller;
	}

	/**
	 * Faz e recusa de uma registro de liberação excluindo os seguintes registros
	 * 
	 * Liberacao
	 * Motivo de edicao de registro se houver
	 * Registro de saida
	 * 
	 * 
	 * @param registro
	 */
	public synchronized void recusarLiberacaoSaida(Registro registro) {
		Liberacao lib = getLiberacaoController().consultarPeloIdDeSaida(registro.getId());
		Registro entrada = registroDao.consultaRegistroPeloId(lib.getEntrada().getId());
		try {
			alterarAtendimentoRecusaLiberacao(getAtendimentoController().consultaAtendimentoPorId(lib.getAtendimento().getId()));
			getMotivoController().excluirMotivoPorRegistro(registro.getId());
			getLiberacaoController().excluirLiberacaoSaida(lib);
			alteraRegistroDeRecusaLiberacao("INICIADO", entrada);
		} catch (Exception e) {
			throw e;
		}
	}

	public synchronized void registrarSaida(Registro registro, List<NotaRegistro> notas, String placaVeiculo, String tipoRegistro) {
		try {
			registro.setData(HoraDaInternet.getHora());
			registro.setUsuario(PermissoesUsuarios.getUsuario());
			registro.setTipo(tipoRegistro);
			registro.setPlacaVeiculo(placaVeiculo);
			registro.setnotas(notas);
			salvarRegistro(registro);
		} catch (Exception e) {
			throw e;
		}
	}

	public synchronized Registro geradorDeRegistro(String tipo, String status, List<NotaRegistro> notas, String placaVeiculo, Empresa empresa, Visitante visitante) {
		try {
			Registro r = new Registro();
			r.setPlacaVeiculo(placaVeiculo);
			r.setEmpresa(empresa);
			r.setTipo(tipo);
			r.setStatus(status);
			r.setUsuario(PermissoesUsuarios.getUsuario());
			r.setData(Calendar.getInstance().getTime());
			r.setPrestadorDeServico(visitante);
			r.setnotas(notas);
			return r;
		} catch (Exception e) {
			throw e;
		}
	}

	public Registro consultarRegistroPorId(Long id) {
		try {
			return registroDao.consultaRegistroPeloId(id);
		} catch (Exception e) {
			throw e;
		}
	}

	public List<Registro> consultaPessoasPresentes() {
		try {
			return registroDao.quantidadePresentes();
		} catch (Exception e) {
			throw e;
		}
	}

	public List<Registro> consultaPessoasEmAtendimento() {
		try {
			return registroDao.quantidadeAtendimentos();
		} catch (Exception e) {
			throw e;
		}
	}

	public List<Registro> consultaEntradaPessoas() {
		try {
			return registroDao.quantidadeEntradas();
		} catch (Exception e) {
			throw e;
		}
	}

	public List<Registro> consultaSaidaPessoas() {
		try {
			return registroDao.quantidadeSaidas();
		} catch (Exception e) {
			throw e;
		}
	}

	public synchronized boolean gerarRegistroSaida(Atendimento atendimento, List<NotaRegistro> notas) throws Exception {
		Registro rg = registroDao.consultaRegistroPeloId(atendimento.getRegistro().getId());
		try {
			Registro registro = new Registro();
			registro.setData(HoraDaInternet.getHora());
			registro.setEmpresa(rg.getEmpresa());
			registro.setnotas(notas);
			registro.setPlacaVeiculo(rg.getPlacaVeiculo());
			registro.setPrestadorDeServico(rg.getPrestadorDeServico());
			registro.setStatus("FINALIZADO");
			registro.setTipo("LIBERADO");
			registro.setUsuario(PermissoesUsuarios.getUsuario());
			Registro regLiberacao = new Registro();
			regLiberacao = registroDao.salvar(registro);
			/**
			 * O Erro da liberação duplicada parti daqui
			 */
			getLiberacaoController().gerarRegistroDeLiberacao(atendimento, regLiberacao);
			return true;
		} catch (Exception ex) {
			throw new Exception("Erro ao tentar gerar registro de saída!");
		}
	}

	public List<Registro> consultarLiberadosSaida() {
		try {
			return registroDao.consultaLiberadosSaida();
		} catch (Exception e) {
			throw e;
		}
	}

	public List<Registro> consultaRegistrosAguardandoAtendimento() {
		try {
			return registroDao.consultarRegistroPeloNomeDaEmpresa("");
		} catch (Exception e) {
			throw e;
		}
	}

	public List<Registro> consultaUltimosRegistro() {
		try {
			return registroDao.consultaUltimosRegistros();
		} catch (Exception e) {
			throw e;
		}
	}

	/****************** Métodos privados a classe ********************/

	private void alteraRegistroDeRecusaLiberacao(String status, Registro registro) {
		registro.setStatus(status);
		registroDao.salvar(registro);
	}

	private void alterarAtendimentoRecusaLiberacao(Atendimento atendimento) {
		atendimento.setStatus("INICIADO");
		atendimento.setUsuario_fim(null);
		atendimento.setData_fim(null);
		getAtendimentoController().alterarAtendimento(atendimento);
	}

	private LiberacaoController getLiberacaoController() {
		LiberacaoController controller = new LiberacaoController();
		return controller;
	}

	private MotivoEdicaoRegistroController getMotivoController() {
		MotivoEdicaoRegistroController controller = new MotivoEdicaoRegistroController();
		return controller;
	}
}
