package br.com.ms.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;

import br.com.ms.dao.RegistroDao;
import br.com.ms.model.Registro;

@ManagedBean
@ViewScoped
public class ConsultaRegistroBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Registro registroEntrada;
	private RegistroDao registroDao;
	private String consulta;
	private List<Registro> registros;
	private int opcaoConsulta;
	private boolean empresa;
	private boolean prestador;
	private boolean cpf;
	private boolean nfe;
	private boolean status;
	private String qtdNotas;

	public ConsultaRegistroBean() {
		registroEntrada = new Registro();
		registroDao = new RegistroDao();
		registros = new ArrayList<>();
		consulta = "";
	}

	/**
	 * Realiza a consulta dos prestadores de serviço que estão aguardando
	 * atendimento
	 */
	public void consultaPorEmpresa() {
		try {
			registros = registroDao.consultarRegistroPeloNomeDaEmpresa(consulta);
			if (registros.isEmpty()) {
				Messages.addGlobalError("Não foram encontrados registros!");
			}
		} catch (RuntimeException erro) {
			Messages.addGlobalError(erro.getCause().getMessage());
		}
	}

	/**
	 * Toda vez que um atendimento for iniciado haverá a necessidade de atualizar os
	 * dados para que seja possível visualizar de maneira dinamica os prestadores
	 * que estão aguardando atendimento o metodo abaixo fará isso automaticamente
	 */
	private void consultarPorEmpresaAtualizacao() {
		try {
			registros = registroDao.consultarRegistroPeloNomeDaEmpresa(consulta);
		} catch (RuntimeException erro) {
			Messages.addGlobalError(erro.getCause().getMessage());
		}
	}

	public void registroSelecionado(ActionEvent event) {
		registroEntrada = new Registro();
		registroEntrada = (Registro) event.getComponent().getAttributes().get("registroSelecionado");
		qtdNotas = String.valueOf(registroEntrada.getNotas().size());
	}

	public void limpar() {
		registroEntrada = new Registro();
		consulta = "";
		consultarPorEmpresaAtualizacao();
	}

	// ####################### gets and sets ###########################//

	public Registro getRegistroEntrada() {
		return registroEntrada;
	}

	public void setRegistroEntrada(Registro registroEntrada) {
		this.registroEntrada = registroEntrada;
	}

	public String getConsulta() {
		return consulta;
	}

	public void setConsulta(String consulta) {
		this.consulta = consulta.toUpperCase().replaceAll("[.-]", "");
	}

	public List<Registro> getRegistros() {
		return registros;
	}

	public void setRegistros(List<Registro> registros) {
		this.registros = registros;
	}

	public int getOpcaoConsulta() {
		return opcaoConsulta;
	}

	public void setOpcaoConsulta(int opcaoConsulta) {
		this.opcaoConsulta = opcaoConsulta;
	}

	public boolean isEmpresa() {
		return empresa;
	}

	public void setEmpresa(boolean empresa) {
		this.empresa = empresa;
	}

	public boolean isPrestador() {
		return prestador;
	}

	public void setPrestador(boolean prestador) {
		this.prestador = prestador;
	}

	public boolean isCpf() {
		return cpf;
	}

	public void setCpf(boolean cpf) {
		this.cpf = cpf;
	}

	public boolean isNfe() {
		return nfe;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public void setNfe(boolean nfe) {
		this.nfe = nfe;
	}

	public String getQtdNotas() {
		return qtdNotas;
	}

	public void setQtdNotas(String qtdNotas) {
		this.qtdNotas = qtdNotas;
	}

}