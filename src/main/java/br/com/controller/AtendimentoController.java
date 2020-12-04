package br.com.controller;

import java.util.Date;
import java.util.List;

import br.com.ms.bean.SharedListBean;
import br.com.ms.dao.AtendimentoDao;
import br.com.ms.dao.RegistroDao;
import br.com.ms.model.Atendimento;
import br.com.ms.model.NotaRegistro;
import br.com.ms.model.Registro;
import br.com.ms.model.Usuario;
import br.com.ms.util.CalculaIntervadoDatas;
import br.com.ms.util.HoraDaInternet;
import br.com.ms.util.PermissoesUsuarios;
import br.com.ms.util.Seguranca;

public class AtendimentoController {

	private static final String INICIADO = "INICIADO";
	private static final String ABERTO = "ABERTO";
	private static final String FINALIZADO = "FINALIZADO";
	// private static final String LIBERADO = "LIBERADO";
	private AtendimentoDao atendimentoDao;
	private RegistroDao registroDao;
	private Atendimento atendimento;
	private UsuarioController usuarioController;
	private ConfiguracaoSistemaController configController;

	public void consultaTabelasCompartilhadas() {
		SharedListBean.consultaRegistrosAguardando();
		SharedListBean.consultaAtendimentosIni();
	}

	public AtendimentoController() {
		atendimentoDao = new AtendimentoDao();
		atendimento = new Atendimento();
		registroDao = new RegistroDao();
		usuarioController = new UsuarioController();
		configController = new ConfiguracaoSistemaController();
	}

	private Usuario consultarProgramadorPorCodigo(String codigoProg) throws Exception {
		try {
			return usuarioController.consultaProgramadorPeloCodigo(codigoProg);
		} catch (Exception e) {
			throw e;
		}
	}

	public RegistroController getRegistroControler() {
		RegistroController controller = new RegistroController();
		return controller;
	}

	public List<Atendimento> consultarAtendimentoPorEmpresa(Date dataInicial, Date dataFinal, String nomeEmpresa) {
		try {
			return atendimentoDao.consultarAtentimentoPorEmpresa(dataInicial, dataFinal, nomeEmpresa);
		} catch (Exception ex) {
			throw ex;
		}
	}

	public List<Atendimento> consultarAtendimento(String status) {
		try {
			return atendimentoDao.consultarAtentimento(status);
		} catch (Exception e) {
			throw e;
		}
	}

	public Atendimento consultaAtendimentoPorId(Long id) {
		try {
			return atendimentoDao.consultarAtentimentoPorId(id);
		} catch (Exception e) {
			throw e;
		}
	}

	public String calculaIntervalo(Date dt1, Date dt2) {
		if (dt2 != null) {
			return CalculaIntervadoDatas.intevalo(dt1, dt2);
		} else {
			return CalculaIntervadoDatas.intevalo(dt1, HoraDaInternet.getHora());
		}
	}

	public Atendimento atendimentoSelecionado(Atendimento atendimento) {
		try {
			return atendimentoDao.consultarAtentimentoPorId(atendimento.getId());
		} catch (Exception e) {
			throw new IllegalArgumentException("Falha ao consultar atendimento");
		}
	}

	public synchronized void iniciarAtendimento(Registro registro, String status, String codigoProg) throws Exception {
		try {
			atendimentoDao.consultarAtendimentoPorRegistro(registro.getId());
			registro = registroDao.consultaRegistroPeloId(registro.getId());
			atendimento.setRegistro(registro);
			atendimento.setData_inicio(HoraDaInternet.getHora());
			atendimento.setData_fim(null);
			atendimento.setStatus(INICIADO);
			atendimento.setRegistro(registro);
			if (configController.consultarConfiguracao().isAtivarProgramador()) {
				if (PermissoesUsuarios.getUsuario().getPermissoes().isProgramador()) {
					atendimento.setUsuario_inicio(consultarProgramadorPorCodigo(codigoProg));
				}else {
					atendimento.setUsuario_inicio(PermissoesUsuarios.getUsuario());
				}
			} else {
				atendimento.setUsuario_inicio(PermissoesUsuarios.getUsuario());
			}
			registro.setStatus(INICIADO);
			atendimentoDao.salvarAtendimento(atendimento, registro);
			consultaTabelasCompartilhadas();
		} catch (Exception e) {
			throw e;
		} finally {
			limpar();
		}
	}

	/**
	 * Finaliza o periodo de atendimento a um prestador de servico A ideia do
	 * synchronized e bloquear este trecho de codigo evitando concorrencia e
	 * possiveis problemas.
	 * 
	 * @param event
	 * @throws Exception
	 */
	public synchronized void finalizaAtendimento(Atendimento at, List<NotaRegistro> notas, String codigoProg) throws Exception {
		try {
			if (Seguranca.getConfig().isAtivarProgramador()) {
				if (PermissoesUsuarios.getUsuario().getPermissoes().isProgramador()) {
					at.setUsuario_fim(consultarProgramadorPorCodigo(codigoProg));
				}else {
					at.setUsuario_fim(PermissoesUsuarios.getUsuario());
				}
			} else {
				at.setUsuario_fim(PermissoesUsuarios.getUsuario());
			}
			if (getRegistroControler().gerarRegistroSaida(at, notas)) {
				at.setData_fim(HoraDaInternet.getHora());
				at.setStatus(FINALIZADO);
				atendimentoDao.alterarAtendimento(at, at.getRegistro());
			}
			SharedListBean.consultaLiberadosSaida();
			consultaTabelasCompartilhadas();
		} catch (Exception erro) {
			limpar();
			throw erro;
		} finally {
			limpar();
		}
	}

	public synchronized void desfazerAtendimento(Atendimento atendimento, Registro registro) {
		try {
			atendimento = atendimentoDao.consultarAtentimentoPorId(atendimento.getId());
			registro = registroDao.consultaRegistroPeloId(atendimento.getRegistro().getId());
			atendimentoDao.excluirAtendimento(atendimento);
			registro.setStatus(ABERTO);
			getRegistroControler().salvarRegistro(registro);
			consultaTabelasCompartilhadas();
		} catch (Exception e) {
			throw e;
		} finally {
			limpar();
		}
	}

	public synchronized void alterarAtendimento(Atendimento atendimento) {
		try {
			atendimentoDao.alterarAtendimento(atendimento);
		} catch (Exception e) {
			throw e;
		}
	}

	public void limpar() {
		atendimento = new Atendimento();
	}

}
