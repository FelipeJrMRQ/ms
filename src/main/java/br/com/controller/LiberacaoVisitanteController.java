package br.com.controller;

import java.util.Date;
import java.util.List;

import br.com.ms.dao.LiberacaoVisitanteDao;
import br.com.ms.model.LiberacaoVisitante;
import br.com.ms.model.Registro;
import br.com.ms.repository.LiberacaoRepository;
import br.com.ms.util.HoraDaInternet;
import br.com.ms.util.PermissoesUsuarios;

public class LiberacaoVisitanteController {

	private LiberacaoVisitanteDao libDao;
	private LiberacaoVisitante liberacaoVisitante;
	
	
	
	private RegistroController getRegistroController() {
		RegistroController controller = new RegistroController();
		return controller;
	}
	
	public LiberacaoVisitanteController() {
		//System.out.println("Liberacao Visitante Controller");
		libDao = new LiberacaoVisitanteDao();
	}
	
	public List<LiberacaoVisitante> consultarLiberacoes(Date dataInicial, Date dataFinal, String nome){
		try {
			return libDao.consultarLiberacaoVisitante(dataInicial, dataFinal, nome);
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	public LiberacaoVisitante consultaLibareacaoVisitante(long id, String tipo) {
		if (tipo.equals("SAIDA") || tipo.equals("LIBERADO")) {
			return libDao.consultarLiberacaoPorIdSaida(id);
		} else {
			return libDao.consultarLiberacaoPorIdEntrada(id);
		}
	}
	
	public void excluirLiberacao(Long id, String tipo) throws Exception {
		if(tipo.equals("VISITANTE")) {
			LiberacaoVisitante libVis = libDao.consultarLiberacaoPorIdSaida(id);
			libDao.excluirLiberacaoVisitante(libVis, libVis.getEntrada(),libVis.getSaida());
		}else if(tipo.equals("PRESTADOR")) {
			new LiberacaoRepository().excluirLiberacaoNaoMonitorada(getRegistroController().consultarRegistroPorId(id));
		}else {
			throw new Exception("Não foi possível excluir esta liberação");
		}
	}
	

	/**
	 * Utilizado exclusivamente para visitantes e não para prestadores de serviço
	 * Quando houver a entrada de um visitante automaticamente ele já estará
	 * liberado para saida.
	 */
	public void salvarLibercaoVisitante(Registro entrada, Registro saida) {
		try {
			liberacaoVisitante = new LiberacaoVisitante();
			liberacaoVisitante.setDataLiberacao(HoraDaInternet.getHora());
			liberacaoVisitante.setEntrada(entrada);
			liberacaoVisitante.setSaida(saida);
			liberacaoVisitante.setUsuario(PermissoesUsuarios.getUsuario());
			libDao.salvar(liberacaoVisitante);
		} catch (Exception e) {
			throw e;
		}
	}
}
