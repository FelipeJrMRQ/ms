package br.com.controller;

import br.com.ms.dao.LiberacaoVisitanteDao;
import br.com.ms.model.Liberacao;
import br.com.ms.model.LiberacaoVisitante;
import br.com.ms.model.Registro;

public class EdicaoRegistroController {


	public EdicaoRegistroController() {
	
	}
	

	private RegistroController getRegistroController() {
		RegistroController controller = new RegistroController();
		return controller;
	}
	
	private LiberacaoController getLiberacaoController() {
		LiberacaoController controller = new LiberacaoController();
		return controller;
	}
	

	/**
	 * Neste metodo é atribuito de regitro de tipo oposto a variavel principal de
	 * classe. Ex: se a variavel registro for do tipo SAIDA será realizada uma
	 * consulta do registro oposto seja ele ENTRADA ou LIBERAÇÃO, em seguida este
	 * valor será atribuido
	 * 
	 * @param registro
	 */
	public Registro verificaTipoDeRegistroEpessoa(Registro registro) {
			registro = getRegistroController().consultarRegistroPorId(registro.getId());
		try {
			if (registro.getTipo().equals("SAIDA") && registro.getPrestadorDeServico().getTipo().equals("PRESTADOR")) {
				return consultarLiberacao(registro.getId(), "SAIDA").getEntrada();
			} else if (registro.getTipo().equals("ENTRADA") && registro.getPrestadorDeServico().getTipo().equals("PRESTADOR")) {
				return consultarLiberacao(registro.getId(), "ENTRADA").getSaida();
			} else if (registro.getTipo().equals("LIBERADO") && registro.getPrestadorDeServico().getTipo().equals("PRESTADOR")) {
				return consultarLiberacao(registro.getId(), "LIBERADO").getEntrada();
			} else if (registro.getTipo().equals("SAIDA") && registro.getPrestadorDeServico().getTipo().equals("VISITANTE")) {
				return consultaLibareacaoVisitante(registro.getId(), "SAIDA").getEntrada();
			} else if (registro.getTipo().equals("ENTRADA") && registro.getPrestadorDeServico().getTipo().equals("VISITANTE")) {
				return consultaLibareacaoVisitante(registro.getId(), "ENTRADA").getSaida();
			} else if (registro.getTipo().equals("LIBERADO") && registro.getPrestadorDeServico().getTipo().equals("VISITANTE")) {
				return consultaLibareacaoVisitante(registro.getId(), "LIBERADO").getEntrada();
			} else {
				return null;
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private Liberacao consultarLiberacao(Long id, String tipo) {
		try {
			return getLiberacaoController().consultarLiberacao(id, tipo);
		} catch (Exception e) {
			throw e;
		}
	}

	private LiberacaoVisitante consultaLibareacaoVisitante(long id, String tipo) {
		LiberacaoVisitanteDao libDao = new LiberacaoVisitanteDao();
		if (tipo.equals("SAIDA") || tipo.equals("LIBERADO")) {
			return libDao.consultarLiberacaoPorIdSaida(id);
		} else {
			return libDao.consultarLiberacaoPorIdEntrada(id);
		}
	}

}
