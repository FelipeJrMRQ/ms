package br.com.ms.repository;

import java.util.Calendar;
import java.util.Date;

import br.com.ms.dao.AtendimentoDao;
import br.com.ms.model.Atendimento;
import br.com.ms.model.Registro;
import br.com.ms.model.Usuario;

public class AtendimentoRepository {
	private Atendimento at;
	private AtendimentoDao dao;
	
	
	public AtendimentoRepository(Registro registro, String status, Usuario user) {
		at = new Atendimento();
		dao = new AtendimentoDao();
		Date data = Calendar.getInstance().getTime();
		at.setData_inicio(data);
		at.setData_fim(data);
		at.setRegistro(registro);
		at.setStatus(status);
		at.setUsuario_fim(user);
		at.setUsuario_inicio(user);
	}
	
	public Atendimento save() {
		try {
			return dao.alterarAtendimento(at);
		}catch(Exception e) {
			throw e;
		}
	}
}
