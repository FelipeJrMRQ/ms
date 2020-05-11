package br.com.ms.repository;

import java.util.Date;
import java.util.Set;

import br.com.ms.dao.RegistroDao;
import br.com.ms.model.Empresa;
import br.com.ms.model.NotaRegistro;
import br.com.ms.model.Registro;
import br.com.ms.model.Usuario;
import br.com.ms.model.Visitante;

public class RegistroRepository {
	private Registro registro = new Registro();
	private RegistroDao registroDao;

	public RegistroRepository(Date data, Empresa empresa, Set<NotaRegistro> notas, String placaVeiculo, Visitante visitante, String status, String tipo, Usuario usuario) {
		registro = new Registro();
		registroDao = new RegistroDao();
		this.registro.setData(data);
		this.registro.setEmpresa(empresa);
		this.registro.setnotas(notas);
		this.registro.setPlacaVeiculo(placaVeiculo);
		this.registro.setPrestadorDeServico(visitante);
		this.registro.setStatus(status);
		this.registro.setTipo(tipo);;
		this.registro.setUsuario(usuario);
		//save();
	}
	
	public Registro save() {
		System.out.println(registro);
		Registro reg = new Registro();
		reg =  registroDao.alterarRegistro(registro);
		return reg;
	}
}
