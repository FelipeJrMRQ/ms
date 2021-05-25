package br.com.ms.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;

import br.com.controller.ConfiguracaoSistemaController;
import br.com.controller.LiberacaoVisitanteController;
import br.com.controller.MotivoEdicaoRegistroController;
import br.com.controller.NotasFiscaisController;
import br.com.controller.RegistroController;
import br.com.controller.VisitanteController;
import br.com.ms.model.Empresa;
import br.com.ms.model.MotivoEdicaoRegistro;
import br.com.ms.model.NotaRegistro;
import br.com.ms.model.Registro;
import br.com.ms.model.Visitante;
import br.com.ms.repository.AtendimentoRepository;
import br.com.ms.repository.LiberacaoRepository;
import br.com.ms.util.CalculaIntervadoDatas;
import br.com.ms.util.HoraDaInternet;
import br.com.ms.util.PermissoesUsuarios;

@ManagedBean
@ViewScoped
public class RegistroBean implements Serializable {
	private static final long serialVersionUID = 3263697279725734307L;
	private String cpf;
	private String nome;
	private String consulta;
	private MotivoEdicaoRegistro motivo;
	private Registro registro;
	private Visitante visitante;
	private String nfe;
	private String tipoDeConsulta;
	private String placa;
	private String qtdNotas;
	private List<Empresa> empresas;
	private List<Registro> registros;
	private List<Registro> liberados;
	private List<Visitante> visitantesNome;
	private List<NotaRegistro> listNfe;
	private List<Registro> quantidadeEntradas;
	private List<Registro> quantidadeAtentimentos;
	private List<Registro> quantidadeSaidas;
	private List<Registro> quantidadePresentes;
	private boolean monitorado;
	private static final String ABERTO = "ABERTO", LIBERADO = "LIBERADO", FINALIZADO = "FINALIZADO", ENTRADA = "ENTRADA", SAIDA = "SAIDA";
	private String id;
	private VisitanteController visitanteController;

	///////////////

	private NotasFiscaisController notasFiscaisController;
	private RegistroController registroController;
	private MotivoEdicaoRegistroController motivoController;
	private LiberacaoVisitanteController liberacaoVisitanteController;
	private ConfiguracaoSistemaController configController;

	public RegistroBean() {
		registro = new Registro();
		visitante = new Visitante();
		empresas = new ArrayList<>();
		listNfe = new ArrayList<>();
		visitantesNome = new ArrayList<>();
		liberados = new ArrayList<>();
		quantidadeEntradas = new ArrayList<>();
		quantidadeAtentimentos = new ArrayList<>();
		quantidadeSaidas = new ArrayList<>();
		quantidadePresentes = new ArrayList<>();
		visitanteController = new VisitanteController();
		notasFiscaisController = new NotasFiscaisController();
		registroController = new RegistroController();
		notasFiscaisController = new NotasFiscaisController();
		motivoController = new MotivoEdicaoRegistroController();
		liberacaoVisitanteController = new LiberacaoVisitanteController();
		configController = new ConfiguracaoSistemaController();
	}

	@PostConstruct
	private void iniciar() {
		tipoDeConsulta = "NOME";
		consultaUtimosRegistros();
		SharedListBean.consultaLiberadosSaida();
		calculaPessoasPresentes();
		qtdNotas = "0";
		configController.iniciarAgendamentos();
	}

	/**
	 * Atualiza a tela principal do sistema mantendo as informações sobre Pessoas
	 * presentes Entradas Atendimentos Saidas As informações são pertinentes do dia
	 * em questão
	 */
	public void atualizaTela() {
		calculaPessoasPresentes();
	}

	public void abrirModal() {
		System.out.println("executei");

	}

	/**
	 * Realiza a consulta e mantem as informações carregadas para atualização de
	 * tela. Isso permite que o usuário tenha uma visão clara da movimentação de
	 * pessoas na empresa.
	 */
	public void calculaPessoasPresentes() {
		try {
			quantidadePresentes = registroController.consultaPessoasPresentes();
			quantidadeEntradas = registroController.consultaEntradaPessoas();
			quantidadeAtentimentos = registroController.consultaPessoasEmAtendimento();
			quantidadeSaidas = registroController.consultaSaidaPessoas();
		} catch (Exception e) {
			Messages.addGlobalError(e.getMessage());
		}
	}

	/**
	 * Realiza a consulta do prestador de serviço ou visitante pelo seu numero de
	 * cpf
	 */
	public void consultarPrestadorPeloCpf() {
		try {
			visitante = visitanteController.consultaPorCPF(cpf).get();
			this.empresas = visitante.getEmpresas();
		} catch (Exception e) {
			Messages.addGlobalError("CPF não encontrado!");
		}
	}

	public void consultaPrestadorPorId() {
		try {
			Long id = Long.parseLong(consulta);
			this.visitante = visitanteController.consultaPorId(id).get();
			this.empresas = visitante.getEmpresas();
		}catch (Exception e) {
			Messages.addGlobalError("Visitante não encontrado!");
		}
		
	}

	public void consultaPrestadorPeloRg() {
		try {
			visitantesNome = visitanteController.consultaPorRg(consulta);
		} catch (Exception ex) {
			Messages.addGlobalError(ex.getMessage());
		}
	}

	public void consultaPrestadorPorNome() {
		try {
			visitantesNome = visitanteController.consultaPorNome(this.nome);
			PrimeFaces.current().executeScript("PF('dlgVisitantePrestador').show()");
		} catch (Exception erro) {
			Messages.addGlobalError(erro.getMessage());
		}
	}

	/**
	 * Executa a gravação de um registro de saida
	 */
	public void registrarSaida() {
		try {
			registroController.registrarSaida(registro, listNfe, this.placa, SAIDA);
			SharedListBean.consultaLiberadosSaida();
			consultaUtimosRegistros();
			Messages.addGlobalInfo("Saída registrada com sucesso!");
			limpar();
		} catch (Exception ex) {
			Messages.addGlobalError(ex.getMessage());
		}
	}

	/**
	 * Este método permite que a portaria retorne a liberação de saida para ao
	 * estado de atendimento;
	 * 
	 * @throws Exception
	 * 
	 */
	public void recusaLiberacao() throws Exception {
		try {
			registroController.recusarLiberacaoSaida(registro);
			SharedListBean.consultaLiberadosSaida();
			SharedListBean.consultaAtendimentosIni();
			Messages.addGlobalInfo("Liberação recusada com sucesso!");
			limpar();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Faz uma verificação se a pessoa possui liberação de acesso as dependencias da
	 * empresa
	 * 
	 * @param v
	 */
	public void prestadorSelecionado(Visitante v) {
		try {
			this.visitante = visitanteController.verificaPermissaoDeAcesso(v);
			this.empresas = this.visitante.getEmpresas();
		} catch (Exception e) {
			Messages.addGlobalWarn(e.getMessage());
		}
	}

	public synchronized void addNotasFiscais() {
		try {
			listNfe.add(notasFiscaisController.addNota(nfe, registro));
			qtdNotas = String.valueOf(listNfe.size());
		} catch (Exception e) {
			Messages.addGlobalError(e.getMessage());
		} finally {
			nfe = "";
		}
	}

	public void removeNotasFiscais(ActionEvent event) {
		NotaRegistro nfe = (NotaRegistro) event.getComponent().getAttributes().get("notaSelecionada");
		if (listNfe.contains(nfe)) {
			listNfe.remove(nfe);
		}
		qtdNotas = String.valueOf(listNfe.size());
	}

	/**
	 * Sempre que um registro novo é salvo ele deve ser salvo com o status ABERTO
	 * isso para registros do tipo ENTRADA Quando o registro for do tipo SAIDA seu
	 * status deve ser FINALIZADO
	 * 
	 * @throws Exception
	 */
	public synchronized void salvarRegistroEntrada() throws Exception {
		if (visitante.getNome() == null) {
			Messages.addGlobalError("Visitante não selecionado!");
		} else {
			if (visitante.getTipo().equals("PRESTADOR") && !visitante.isNaoMonitorado()) {
				salvarRegistroPrestador();
			} else if (visitante.getTipo().equals("VISITANTE")) {
				salvarRegistroVisitante();
			} else if (visitante.getTipo().equals("PRESTADOR") && visitante.isNaoMonitorado()) {
				salvarRegistroPrestadorNaoMonitorado();
			}
		}
		limpar();
		consultaUtimosRegistros();
		SharedListBean.consultaLiberadosSaida();
		SharedListBean.consultaRegistrosAguardando();
	}

	/**
	 * Caso o usuário cometa erros na liberação de saída será possível retorna-la a
	 * um estado anterior, no caso o estado de liberado deste modo o operador ficará
	 * livre para registrar a saída novamente. Este método também realizará a
	 * exclusão das notas fiscais vinculadas ao registro de saída
	 */
	public void desfazerSaida() {
		try {
			registro.setTipo("LIBERADO");
			registroController.salvarRegistro(registro);
			limpar();
			consultaUtimosRegistros();
			SharedListBean.consultaLiberadosSaida();
			Messages.addGlobalInfo("Saída desfeita com sucesso!");
		} catch (Exception e) {
			Messages.addGlobalError("Falha ao desfazer saida! ");
		}
	}

	public void registroSelecionado(ActionEvent event) {
		registro = (Registro) event.getComponent().getAttributes().get("registroSelecionado");
		consultarMotivo();
		visitante = registro.getPrestadorDeServico();
		listNfe = registro.getNotas();
		placa = registro.getPlacaVeiculo();
		qtdNotas = String.valueOf(registro.getNotas().size());
	}

	public void consultarMotivo() {
		motivo = motivoController.consultarMotivo(registro.getId());
	}

	/*
	 * s Realiza a alteração do resgitro de entrada para VISITANTES esta lógica
	 * segue o mesmo padrão do salvarRegistro
	 */
	public void alterarRegistro() {
		try {
			if (registro.getStatus().equals("ABERTO")) {
				registro.setUsuario(PermissoesUsuarios.getUsuario());
				registro.setPrestadorDeServico(visitante);
				registro.setnotas(listNfe);
				registro.setPlacaVeiculo(placa);
				registroController.salvarRegistro(registro);
				Messages.addGlobalInfo("Registro alterado com sucesso!");
				consultaUtimosRegistros();
				limpar();
			} else {
				Messages.addGlobalError("Este registro não pode ser alterado!");
			}
		} catch (Exception e) {
			Messages.addGlobalError(e.getMessage());
		}
	}

	/**
	 * Realiza a exclusao de um regitro de entrada quando não vinculado a um
	 * atendimento ou liberação
	 * 
	 * @param r
	 */
	public void excluirRegistroVisitante(Registro r) {
		try {
			registroController.excluirRegistro(r);
			registros = registroController.consultaUltimosRegistro();
			Messages.addGlobalInfo("Registro excluido com sucesso!");
			SharedListBean.consultaRegistrosAguardando();
		} catch (Exception e) {
			Messages.addGlobalWarn(e.getMessage());
		}
	}

	/*
	 * Este método permite a exclusão de um registro de entrada do tipo visitante
	 * Como um visitante não passa pelo processo de atendimento Houve a necessidade
	 * de criar uma lógica que permitisse a exclusão No momento em que fosse ser
	 * registrada a saida do visitante
	 */
	public void exluirLiberacao(Long id, String tipo) {
		try {
			liberacaoVisitanteController.excluirLiberacao(id, tipo);
			Messages.addGlobalInfo("Registro excluido com sucesso!");
			limpar();
			consultaUtimosRegistros();
			SharedListBean.consultaLiberadosSaida();
		} catch (Exception e) {
			e.printStackTrace();
			Messages.addGlobalError("Erro ao excluir registro");
		}
	}

	public String tempoDeEspera(Date entrada) throws Exception {
		return CalculaIntervadoDatas.intevalo(entrada, HoraDaInternet.getHora());
	}

	/**
	 * Sempre que houver uma alteração de qualquer tipo este metodo deve ser chamado
	 * e no xhtml deve ser feito um update na tabela principal para manter os dados
	 * atualizados.
	 */
	public void consultaUtimosRegistros() {
		try {
			registros = registroController.consultaUltimosRegistro();
		} catch (Exception e) {
			throw e;
		}
	}

	public void tipoDeConsulta() {
		cpf = "";
		nome = "";
		consulta = "";
		placa = "";
	}

	public void clean() {
		limpar();
		consultaUtimosRegistros();
	}

	private void limpar() {
		registro = new Registro();
		empresas = new ArrayList<>();
		visitante = new Visitante();
		listNfe = new ArrayList<>();
		qtdNotas = "0";
		cpf = "";
		nome = "";
		placa = "";
	}

	/**
	 * Utilizado exclusivamente para visitantes e não para prestadores de serviço
	 */
	private void salvarRegistroEntradaVisitante() {
		try {
			Registro r1 = new Registro();
			r1.setTipo(ENTRADA);
			r1.setStatus(FINALIZADO);
			r1.setUsuario(PermissoesUsuarios.getUsuario());
			r1.setData(HoraDaInternet.getHora());
			r1.setPrestadorDeServico(visitante);
			r1.setnotas(listNfe);
			r1.setPlacaVeiculo(registro.getPlacaVeiculo());
			r1.setEmpresa(registro.getEmpresa());
			r1 = registroController.salvarRegistro(r1);
			salvarRegistroSaidaVisitante(r1);
		} catch (Exception e) {
			Messages.addGlobalError("Erro ao salvar entrada do visitante!");
		}
	}

	/**
	 * Utilizado exclusivamente para visitantes e não para prestadores de serviço
	 */
	private void salvarRegistroSaidaVisitante(Registro r) {
		try {
			Registro r1 = new Registro();
			r1.setEmpresa(r.getEmpresa());
			r1.setTipo(LIBERADO);
			r1.setStatus(FINALIZADO);
			r1.setData(HoraDaInternet.getHora());
			r1.setnotas(r.getNotas());
			r1.setPlacaVeiculo(r.getPlacaVeiculo());
			r1.setPrestadorDeServico(r.getPrestadorDeServico());
			r1.setUsuario(PermissoesUsuarios.getUsuario());
			r1 = registroController.salvarRegistro(r1);
			liberacaoVisitanteController.salvarLibercaoVisitante(r, r1);
		} catch (Exception e) {
			Messages.addGlobalError("Erro ao salvar saída do visitante!");
		}
	}

	private void salvarRegistroPrestador() {
		try {
			registroController.salvarRegistro(registroController.geradorDeRegistro(ENTRADA, ABERTO, listNfe, registro.getPlacaVeiculo(), registro.getEmpresa(), visitante));
			Messages.addGlobalInfo("Entrada cadastrado com sucesso!");
		} catch (Exception e) {
			Messages.addGlobalError(e.getMessage());
		}
	}

	private void salvarRegistroPrestadorNaoMonitorado() {
		try {
			Registro entrada = new Registro();
			Registro saida = new Registro();
			entrada = registroController.salvarRegistro(registroController.geradorDeRegistro(ENTRADA, FINALIZADO, listNfe, registro.getPlacaVeiculo(), registro.getEmpresa(), visitante));
			saida = registroController.salvarRegistro(registroController.geradorDeRegistro(LIBERADO, FINALIZADO, new ArrayList<>(), registro.getPlacaVeiculo(), registro.getEmpresa(), visitante));
			new LiberacaoRepository(entrada, saida, new AtendimentoRepository(entrada, FINALIZADO, PermissoesUsuarios.getUsuario()).save(), PermissoesUsuarios.getUsuario());
			Messages.addGlobalInfo("Entrada cadastrado com sucesso!");
		} catch (Exception e) {
			Messages.addGlobalError(e.getMessage());
		}
	}

	private void salvarRegistroVisitante() {
		try {
			salvarRegistroEntradaVisitante();
			Messages.addGlobalInfo("Entrada cadastrado com sucesso!");
		} catch (Exception e) {
			Messages.addGlobalError("Erro ao salvar registro!");
		}
	}

	/* ################### GETS AND SETS ######################## */

	public String getCpf() {
		return cpf;
	}

	public String getConsulta() {
		return consulta;
	}

	public void setConsulta(String consulta) {
		this.consulta = consulta;
	}

	public String getTipoDeConsulta() {
		return tipoDeConsulta;
	}

	public void setTipoDeConsulta(String tipoDeConsulta) {
		this.tipoDeConsulta = tipoDeConsulta;
	}

	public Registro getRegistro() {
		return registro;
	}

	public void setRegistro(Registro registro) {
		this.registro = registro;
	}

	public Visitante getPrestadorDeServico() {
		return visitante;
	}

	public void setPrestadorDeServico(Visitante visitante) {
		this.visitante = visitante;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf.replaceAll("[.-]", "");
	}

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	public List<Registro> getRegistros() {
		return registros;
	}

	public void setRegistros(List<Registro> registros) {
		this.registros = registros;
	}

	public List<NotaRegistro> getListNfe() {
		return listNfe;
	}

	public void setListNfe(List<NotaRegistro> listNfe) {
		this.listNfe = listNfe;
	}

	public String getNfe() {
		return nfe;
	}

	/**
	 * Utiliza a função ReplaceAll para remover espacos em branco
	 * 
	 * @param nfe
	 */
	public void setNfe(String nfe) {
		this.nfe = nfe.replaceAll(" ", "");
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome.toUpperCase();
	}

	public List<Visitante> getVisitantesNome() {
		return visitantesNome;
	}

	public void setVisitantesNome(List<Visitante> visitantesNome) {
		this.visitantesNome = visitantesNome;
	}

	public List<Registro> getLiberados() {
		return liberados;
	}

	public void setLiberados(List<Registro> liberados) {
		this.liberados = liberados;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getQtdNotas() {
		return qtdNotas;
	}

	public void setQtdNotas(String qtdNotas) {
		this.qtdNotas = qtdNotas;
	}

	public List<Registro> getQuantidadeEntradas() {
		return quantidadeEntradas;
	}

	public void setQuantidadeEntradas(List<Registro> quantidadeEntradas) {
		this.quantidadeEntradas = quantidadeEntradas;
	}

	public List<Registro> getQuantidadeAtentimentos() {
		return quantidadeAtentimentos;
	}

	public void setQuantidadeAtentimentos(List<Registro> quantidadeAtentimentos) {
		this.quantidadeAtentimentos = quantidadeAtentimentos;
	}

	public List<Registro> getQuantidadeSaidas() {
		return quantidadeSaidas;
	}

	public void setQuantidadeSaidas(List<Registro> quantidadeSaidas) {
		this.quantidadeSaidas = quantidadeSaidas;
	}

	public List<Registro> getQuantidadePresentes() {
		return quantidadePresentes;
	}

	public void setQuantidadePresentes(List<Registro> quantidadePresentes) {
		this.quantidadePresentes = quantidadePresentes;
	}

	public boolean isMonitorado() {
		return monitorado;
	}

	public void setMonitorado(boolean monitorado) {
		this.monitorado = monitorado;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MotivoEdicaoRegistro getMotivo() {
		return motivo;
	}

	public void setMotivo(MotivoEdicaoRegistro motivo) {
		this.motivo = motivo;
	}
}