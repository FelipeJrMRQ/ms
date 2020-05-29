package br.com.ms.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import br.com.controller.AtendimentoController;
import br.com.controller.RegistroController;
import br.com.ms.model.Atendimento;
import br.com.ms.model.Registro;

@ManagedBean
@ApplicationScoped
public class SharedListBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1187269388550791163L;

	private static List<Atendimento> atendimentos = new ArrayList<>();
	private static List<Registro> registros = new ArrayList<>();
	private static List<Registro> liberados = new ArrayList<>();
	private static RegistroController registroController = new RegistroController();
	private static AtendimentoController atendimentoController = new AtendimentoController();

	public SharedListBean() {

	}

	/**
	 * Em cada alteração no banco de dados um usuário ficará encarregado de
	 * atualizar as informações da aplicacao. Este metodo realiza uma consulta em
	 * todos os atendimentos iniciados e compartilha a todos usuários conectados a
	 * aplicacao
	 */
	public static synchronized void consultaAtendimentosIni() {
		String status = "INICIADO";
		try {
			atendimentos = atendimentoController.consultarAtendimento(status);
		} catch (Exception erro) {
			throw erro;
		}
	}

	/**
	 * Em cada alteração no banco de dados um usuário ficará encarregado de
	 * atualizar as informações da aplicacao. Este metodo realiza uma consulta em
	 * todos os registros que estao aguardando atendimento e compartilha a todos
	 * usuários conectados a aplicacao
	 */
	public static synchronized void consultaRegistrosAguardando() {
		try {
			registros = registroController.consultaRegistrosAguardandoAtendimento();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Em cada alteração no banco de dados um usuário ficará encarregado de
	 * atualizar as informações da aplicacao. Este metodo realiza uma consulta em
	 * todos os registros liberados para saida e compartilha a todos usuários
	 * conectados a aplicacao.
	 */
	public static synchronized void consultaLiberadosSaida() {
		try {
			liberados = registroController.consultarLiberadosSaida();
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Retorna a lista de pessoas que estão aguardando atendimento
	 * @return
	 */
	public static List<Registro> aguardandoAtendimento() {
		return registros;
	}

	/**
	 * Retorna a lista de atendimentos iniciados
	 * @return
	 */
	public static List<Atendimento> atendimentoIniciado() {
		return atendimentos;
	}

	/**
	 * Retorna a lista de pessoas liberadas para a saida porem ainda presentes.
	 * @return
	 */
	public static List<Registro> liberadosSaida() {
		return liberados;
	}
}
