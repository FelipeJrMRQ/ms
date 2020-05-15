package br.com.controller;

import java.util.List;

import br.com.ms.dao.RegistroDao;
import br.com.ms.model.Atendimento;
import br.com.ms.model.NotaRegistro;
import br.com.ms.model.Registro;
import br.com.ms.util.HoraDaInternet;
import br.com.ms.util.PermissoesUsuarios;

public class RegistroController {
	
	private RegistroDao registroDao;
	private LiberacaoController liberacaoController; 
	
	
	public RegistroController() {
		registroDao = new RegistroDao();
		liberacaoController = new LiberacaoController();
	}
	
	
	public void salvarRegistroDeEntrada(Registro registro) {
		
	}
	
	public Registro consultarRegistroPorId(Long id) {
		try {
			return registroDao.consultaRegistroPeloId(id);
		}catch(Exception e) {
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
			if (liberacaoController.gerarRegistroDeLiberacao(atendimento, regLiberacao)) {
				return true;
			} else {
				registroDao.excluir(regLiberacao);
				return false;
			}
		} catch (Exception ex) {
			throw new Exception("Erro ao tentar gerar registro de saída!");
		}
	}
	
	public void excluirRegistro(Registro registro) {
		try {
			registroDao.excluir(registro);
		}catch(Exception e) {
			throw e;
		}
	}
	
	public void aterarRegistro(Registro registro) {
		try {
			registroDao.alterarRegistro(registro);
		}catch(Exception e) {
			throw e;
		}
	}
}
