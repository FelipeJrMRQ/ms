package br.com.controller;

import java.util.Date;
import java.util.List;

import org.omnifaces.util.Messages;

import br.com.ms.bean.SharedListBean;
import br.com.ms.dao.AtendimentoDao;
import br.com.ms.dao.RegistroDao;
import br.com.ms.model.Atendimento;
import br.com.ms.model.NotaRegistro;
import br.com.ms.model.Registro;
import br.com.ms.util.CalculaIntervadoDatas;
import br.com.ms.util.HoraDaInternet;
import br.com.ms.util.PermissoesUsuarios;

public class AtendimentoController {
	
	
	private static final String INICIADO = "INICIADO";
	private static final String ABERTO = "ABERTO";
	private static final String FINALIZADO = "FINALIZADO";
	//private static final String LIBERADO = "LIBERADO";
	private AtendimentoDao atendimentoDao;
	private RegistroDao registroDao;
	private Atendimento atendimento;
	
	
	public void consultaTabelasCompartilhadas() {
		SharedListBean.consultaRegistrosAguardando();
		SharedListBean.consultaAtendimentosIni();
	}
	
	public AtendimentoController() {
		atendimentoDao = new AtendimentoDao();
		atendimento = new Atendimento();
		registroDao = new RegistroDao();
	}
	
	public RegistroController getRegistroControler() {
		RegistroController controller = new RegistroController();
		return controller;
	}
	
	public List<Atendimento> consultarAtendimentoPorEmpresa(Date dataInicial, Date dataFinal, String nomeEmpresa) {
		try {
			return atendimentoDao.consultarAtentimentoPorEmpresa(dataInicial, dataFinal, nomeEmpresa);
		}catch(Exception ex) {
			throw ex;
		}
	}
	
	public List<Atendimento> consultarAtendimento(String status){
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
	
	public String calculaIntervalo(Date dt1, Date dt2){
		if(dt2 != null) {
			return CalculaIntervadoDatas.intevalo(dt1, dt2);
		}else {
			return CalculaIntervadoDatas.intevalo(dt1, HoraDaInternet.getHora());
		}
	}
	
	public Atendimento atendimentoSelecionado(Atendimento atendimento) {
		try {
			return atendimentoDao.consultarAtentimentoPorId(atendimento.getId());
		}catch(Exception e) {
			throw new IllegalArgumentException("Falha ao consultar atendimento");
		}
	}
	
	public void iniciarAtendimento(Registro registro, String status) {
		try {
			atendimentoDao.consultarAtendimentoPorRegistro(registro.getId());
			registro = registroDao.consultaRegistroPeloId(registro.getId());
			atendimento.setRegistro(registro);
			atendimento.setData_inicio(HoraDaInternet.getHora());
			atendimento.setData_fim(null);
			atendimento.setStatus(INICIADO);
			atendimento.setRegistro(registro);
			atendimento.setUsuario_inicio(PermissoesUsuarios.getUsuario());
			registro.setStatus(INICIADO);
			atendimentoDao.salvarAtendimento(atendimento, registro);
			consultaTabelasCompartilhadas();
		}catch (Exception e) {
			throw e;
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
	public synchronized void finalizaAtendimento(Atendimento at, List<NotaRegistro> notas) {
		try {
			if (getRegistroControler().gerarRegistroSaida(at, notas)) {
				at.setData_fim(HoraDaInternet.getHora());
				at.setStatus(FINALIZADO);
				at.setUsuario_fim(PermissoesUsuarios.getUsuario());
				atendimentoDao.alterarAtendimento(at, at.getRegistro());
				SharedListBean.consultaLiberadosSaida();
				consultaTabelasCompartilhadas();
			}
		} catch (Exception erro) {
			Messages.addGlobalError(erro.getMessage());
		}
	}
	
	public void desfazerAtendimento(Atendimento atendimento, Registro registro) {
		try {
			atendimento = atendimentoDao.consultarAtentimentoPorId(atendimento.getId());
			registro = registroDao.consultaRegistroPeloId(atendimento.getRegistro().getId());
			atendimentoDao.excluirAtendimento(atendimento);
			registro.setStatus(ABERTO);
			getRegistroControler().salvarRegistro(registro);
			consultaTabelasCompartilhadas();
		}catch(Exception e) {
			throw e;
		}
	}
	
	public void alterarAtendimento(Atendimento atendimento) {
		try {
			atendimentoDao.alterarAtendimento(atendimento);
		} catch (Exception e) {
			throw e;
		}
	}
}
