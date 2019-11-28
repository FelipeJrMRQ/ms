package br.com.ms.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;

import br.com.ms.dao.AtendimentoDao;
import br.com.ms.dao.LiberacaoDao;
import br.com.ms.dao.LiberacaoVisitanteDao;
import br.com.ms.dao.MotivoEdicaoRegistroDao;
import br.com.ms.dao.RegistroDao;
import br.com.ms.dao.VisitanteDao;
import br.com.ms.model.Atendimento;
import br.com.ms.model.Empresa;
import br.com.ms.model.Liberacao;
import br.com.ms.model.LiberacaoVisitante;
import br.com.ms.model.MotivoEdicaoRegistro;
import br.com.ms.model.NotaRegistro;
import br.com.ms.model.Registro;
import br.com.ms.model.Visitante;
import br.com.ms.repository.AtendimentoRepository;
import br.com.ms.repository.LiberacaoRepository;
import br.com.ms.util.CalculaIntervadoDatas;
import br.com.ms.util.ConverteChaveDeAcesso;
import br.com.ms.util.HoraDaInternet;
import br.com.ms.util.PermissoesUsuarios;

@ManagedBean
@ViewScoped
public class RegistroBean implements Serializable {
	private static final long serialVersionUID = 3263697279725734307L;
	private String cpf;
	private String nome;
	private String consulta;
	private String emp;
	private MotivoEdicaoRegistro motivo;
	private MotivoEdicaoRegistroDao motivoDao;
	private Registro registro;
	private RegistroDao registroDao;
	private Visitante visitante;
	private VisitanteDao visitanteDao;
	private NotaRegistro notaRegistro;
	private String nfe;
	private String tipoDeConsulta;
	private LiberacaoVisitanteDao libVisiDao;
	private LiberacaoDao libDao;
	private LiberacaoVisitante liberacaoVisitante;
	private String placa;
	private String qtdNotas;
	private AtendimentoDao atendimentoDao;
	private List<Empresa> empresas;
	private List<Registro> registros;
	private List<Registro> liberados;
	private List<Visitante> visitantesNome;
	private List<NotaRegistro> listNfe;
	private List<Registro> quantidadeEntradas;
	private List<Registro> quantidadeAtentimentos;
	private List<Registro> quantidadeSaidas;
	private List<Registro> quantidadePresentes;
	private List<String> numeroNotas;
	private boolean monitorado;
	private static final String ABERTO = "ABERTO", LIBERADO = "LIBERADO", FINALIZADO = "FINALIZADO", ENTRADA = "ENTRADA", SAIDA = "SAIDA";
	private String id;

	public RegistroBean() {
		motivo = new MotivoEdicaoRegistro();
		motivoDao = new MotivoEdicaoRegistroDao();
		registro = new Registro();
		registroDao = new RegistroDao();
		visitante = new Visitante();
		visitanteDao = new VisitanteDao();
		notaRegistro = new NotaRegistro();
		empresas = new ArrayList<>();
		registros = new ArrayList<>();
		listNfe = new ArrayList<>();
		visitantesNome = new ArrayList<>();
		liberados = new ArrayList<>();
		libVisiDao = new LiberacaoVisitanteDao();
		libDao = new LiberacaoDao();
		liberacaoVisitante = new LiberacaoVisitante();
		numeroNotas = new ArrayList<>();
		atendimentoDao = new AtendimentoDao();
		quantidadeEntradas = new ArrayList<>();
		quantidadeAtentimentos = new ArrayList<>();
		quantidadeSaidas = new ArrayList<>();
		quantidadePresentes = new ArrayList<>();
	}

	@PostConstruct
	private void iniciar() {
		//long ti = Calendar.getInstance().getTimeInMillis();
		tipoDeConsulta = "NOME";
		consultaUtimosRegistros();
		SharedListBean.consultaLiberadosSaida();
		calculaPessoasPresentes();
		//long tf = Calendar.getInstance().getTimeInMillis();
		//System.out.println("Tempo gasto: " + (tf - ti));
	}

	/**
	 * Atualiza a tela principal do sistema mantendo as informações sobre Pessoas
	 * presentes Entradas Atendimentos Saidas As informações são pertinentes do dia
	 * em questão
	 */
	public void atualizaTela() {
		//long ti = Calendar.getInstance().getTimeInMillis();
		calculaPessoasPresentes();
		//long tf = Calendar.getInstance().getTimeInMillis();
		//System.out.println("Tempo gasto: " + (tf - ti));
	}

	/**
	 * Realiza a consulta e mantem as informações carregadas para atualização de
	 * tela. Isso permite que o usuário tenha uma visão clara da movimentação de
	 * pessoas na empresa.
	 */
	public void calculaPessoasPresentes() {
		try {
			quantidadePresentes = registroDao.quantidadePresentes();
			quantidadeEntradas = registroDao.quantidadeEntradas();
			quantidadeAtentimentos = registroDao.quantidadeAtendimentos();
			quantidadeSaidas = registroDao.quantidadeSaidas();
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
			visitante = visitanteDao.consultaPrestadorPeloCpf(cpf);
			if (visitante != null) {
				if (visitante.getStatus()) {
					empresas = visitante.getEmpresas();
				} else {
					Messages.addGlobalError("Entrada bloqueada, consulte o setor responsável!");
					visitante = new Visitante();
				}
			} else {
				Messages.addGlobalError("Numero de CPF não encontrado!");
			}
		} catch (Exception e) {
			Messages.addGlobalError(e.getMessage());
		}
	}

	public void consultaPrestadorPorId() {
		try {
			long id = Long.parseLong(consulta);
			visitante = visitanteDao.consultaVisitantePorId(id);
			empresas = visitante.getEmpresas();
		} catch (Exception ex) {
			Messages.addGlobalError("Código não encontrado!");
		}
	}

	public void consultaPrestadorPeloRg() {
		try {
			visitantesNome = visitanteDao.consultaPrestadorPeloRg(consulta);
			if (visitantesNome.isEmpty()) {
				Messages.addGlobalError("Número de RG não encontrado!");
				return;
			}
		} catch (RuntimeException ex) {
			Messages.addGlobalError(ex.getMessage());
		}
	}

	public void consultaPrestadorPorNome() {
		try {
			visitantesNome = visitanteDao.consultaPrestadorDeServicoPeloNome(this.nome);
			if (visitantesNome.isEmpty()) {
				Messages.addGlobalError("Nome não encontrado!");
				return;
			}
		} catch (RuntimeException erro) {
			Messages.addGlobalError(erro.getCause().getMessage());
		}
	}

	/**
	 * Executa a gravação de um registro de saida
	 */
	public void registrarSaida() {
		try {
			registro.setData(HoraDaInternet.getHora());
			registro.setUsuario(PermissoesUsuarios.getUsuario());
			registro.setTipo(SAIDA);
			registro.setPlacaVeiculo(placa);
			registroDao.salvar(registro);
			SharedListBean.consultaLiberadosSaida();
			consultaUtimosRegistros();
			Messages.addGlobalInfo("Saída registrada com sucesso!");
			limpar();
		} catch (Exception ex) {
			Messages.addGlobalError(ex.getCause().getMessage());
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
		Liberacao lib = new Liberacao();
		Atendimento atdm = new Atendimento();
		Registro rgEntrada = new Registro();
		lib = libDao.consultarPorIdDoRegistroDeSaida(registro.getId());
		rgEntrada = lib.getEntrada();
		try {
			rgEntrada.setStatus("INICIADO");
			registroDao.alterarRegistro(rgEntrada);
			atdm = atendimentoDao.consultarAtentimentoPorId(lib.getAtendimento().getId());
			atdm.setStatus("INICIADO");
			atdm.setUsuario_fim(null);
			atdm.setData_fim(null);
			atendimentoDao.alterarAtendimento(atdm);
			libDao.excluirLiberacao(lib);
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
			if (!v.getStatus()) {
				Messages.addGlobalWarn("Entrada bloqueada, consulte o setor responsável!");
				return;
			} else if (v.getStatus()) {
				visitante = v;
				empresas = v.getEmpresas();
			}
		} catch (Exception e) {
			Messages.addGlobalError(e.getMessage());
		}
	}

	public synchronized void addNotasFiscais() {
		carregarNotas();
		String nf = ConverteChaveDeAcesso.getNumeroNfe(nfe);
		if (!numeroNotas.contains(nf) && !nf.isEmpty()) {
			try {
				// listNfe.add(MontaRegistroNfe.getNfe(nfe, registro));
				NotaRegistro n = new NotaRegistro();
				n.setChave(nfe);
				n.setRegistro(registro);
				n.setNumeroNfe(nf);
				listNfe.add(n);
				qtdNotas = String.valueOf(listNfe.size());
				numeroNotas.add(nf);
				nfe = "";
				return;
			} catch (Exception e) {
				Messages.addGlobalFatal("Falha ao adicionar nota");
			}
		} else {
			nfe = "";
		}
	}

//	/**
//	 * Método temporario no futuro substituir pelo addNotas
//	 */
//	public void addNFEdicaRegistro() {
//		carregarNotas();
//		String nf = ConverteChaveDeAcesso.getNumeroNfe(nfe);
//		if (!numeroNotas.contains(nf) && !nf.isEmpty()) {
//			numeroNotas.add(nf);
//			NotaRegistro nota = new NotaRegistro();
//			nota.setChave(nfe);
//			nota.setNumeroNfe(nf);
//			nota.setRegistro(registro);
//			listNfe.add(nota);
//			nfe = "";
//			qtdNotas = String.valueOf(listNfe.size());
//			return;
//		} else {
//			nfe = "";
//		}
//	}

	/**
	 * Caso hajam notas fiscais já cadastradas no registro este método as converte
	 * para uma lista de String com o numero da nota que posteriormente será
	 * vinculado ao registro juntos as demais notas.
	 */
	private void carregarNotas() {
		try {
			for (NotaRegistro n : registro.getNotas()) {
				numeroNotas.add(n.getNumeroNfe());
			}
		} catch (Exception e) {

		}
	}

	public void removeNotasFiscais(ActionEvent event) {
		NotaRegistro nfe = (NotaRegistro) event.getComponent().getAttributes().get("notaSelecionada");
		if (listNfe.contains(nfe)) {
			listNfe.remove(nfe);
			numeroNotas.remove(nfe.getNumeroNfe());
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
		try {
			try {
				if (visitante.getNome() == null) {
					Messages.addGlobalError("Visitante não selecionado!");
				} else {
					if (visitante.getTipo().equals("PRESTADOR") && !visitante.isNaoMonitorado()) {
						registroDao.salvar(gerarRegistro(ENTRADA, ABERTO, listNfe));
						Messages.addGlobalInfo("Entrada cadastrado com sucesso!");
					} else if (visitante.getTipo().equals("VISITANTE")) {
						try {
							salvarRegistroEntradaVisitante();
							Messages.addGlobalInfo("Entrada cadastrado com sucesso!");
						} catch (Exception e) {
							Messages.addGlobalError("Erro ao salvar registro!");
						}
					} else if (visitante.getTipo().equals("PRESTADOR") && visitante.isNaoMonitorado()) {
						Registro entrada = new Registro();
						Registro saida = new Registro();
						entrada = registroDao.salvar(gerarRegistro(ENTRADA, FINALIZADO, listNfe));
						saida = registroDao.salvar(gerarRegistro(LIBERADO, FINALIZADO, new ArrayList<>()));
						new LiberacaoRepository(entrada, saida, new AtendimentoRepository(entrada, FINALIZADO, PermissoesUsuarios.getUsuario()).save(), PermissoesUsuarios.getUsuario());
						Messages.addGlobalInfo("Entrada cadastrado com sucesso!");
					}
				}
				limpar();
				consultaUtimosRegistros();
				SharedListBean.consultaLiberadosSaida();
				SharedListBean.consultaRegistrosAguardando();
			} catch (NullPointerException e) {
				e.printStackTrace();
				Messages.addGlobalError("Erro ao salvar registro!");
			}
		} catch (RuntimeException e) {
			Messages.addGlobalError(e.getCause().getMessage());
		}
	}

	private Registro gerarRegistro(String tipo, String status, List<NotaRegistro> notas) {
		Registro r = new Registro();
		r.setPlacaVeiculo(registro.getPlacaVeiculo());
		r.setEmpresa(registro.getEmpresa());
		r.setTipo(tipo);
		r.setStatus(status);
		r.setUsuario(PermissoesUsuarios.getUsuario());
		r.setData(Calendar.getInstance().getTime());
		r.setPrestadorDeServico(visitante);
		r.setnotas(notas);
		return r;
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
			registroDao.desfazerSaida(registro);
			limpar();
			consultaUtimosRegistros();
			SharedListBean.consultaLiberadosSaida();
			Messages.addGlobalInfo("Saída desfeita com sucesso!");
		} catch (Exception e) {
			Messages.addGlobalError("Falha ao desfazer saida! ");
		}
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
			r1 = registroDao.salvar(r1);
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
			r1 = registroDao.salvar(r1);
			salvarLibercaoVisitante(r, r1);
		} catch (Exception e) {
			Messages.addGlobalError("Erro ao salvar saída do visitante!");
		}
	}

	/**
	 * Utilizado exclusivamente para visitantes e não para prestadores de serviço
	 * Quando houver a entrada de um visitante automaticamente ele já estará
	 * liberado para saida.
	 */
	private void salvarLibercaoVisitante(Registro entrada, Registro saida) {
		try {
			liberacaoVisitante.setDataLiberacao(HoraDaInternet.getHora());
			liberacaoVisitante.setEntrada(entrada);
			liberacaoVisitante.setSaida(saida);
			liberacaoVisitante.setUsuario(PermissoesUsuarios.getUsuario());
			libVisiDao.salvar(liberacaoVisitante);
		} catch (Exception e) {
			Messages.addGlobalError("Erro ao salvar liberação!");
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
		motivo = motivoDao.consultar(registro.getId());
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
				registroDao.alterarRegistro(registro);
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
			if (verificaPermissaoExclusao(r)) {
				registroDao.excluir(r);
				Messages.addGlobalInfo("Registro excluído com sucesso!");
				consultaUtimosRegistros();
				SharedListBean.consultaRegistrosAguardando();
				return;
			} else {
				Messages.addGlobalWarn("Não é possível excluir este registro por que já possui vínculos.");
				return;
			}
		} catch (Exception e) {
			Messages.addGlobalWarn("Não é possível excluir este registro por que já possui vínculos.");
		}
	}

	/*
	 * Este método permite a exclusão de um registro de entrada do tipo visitante
	 * Como um visitante não passa pelo processo de atendimento Houve a necessidade
	 * de criar uma lógica que permitisse a exclusão No momento em que fosse ser
	 * registrada a saida do visitante
	 */
	public void exluirLiberacao(long id, String tipo) {
		try {
			if (tipo.equals("VISITANTE")) {
				LiberacaoVisitante lb = new LiberacaoVisitante();
				LiberacaoVisitanteDao libDao = new LiberacaoVisitanteDao();
				if (id > 0) {
					lb = libDao.consultarLiberacaoPorIdSaida(id);
					libDao.excluirLiberacaoVisitante(lb, lb.getEntrada(), lb.getSaida());
					consultaUtimosRegistros();
					SharedListBean.consultaLiberadosSaida();
					Messages.addGlobalInfo("Registro excluido com sucesso!");
				} else {
					Messages.addGlobalError("Não foi possível encontrar um liberação com esta ID");
				}
			} else if (tipo.equals("PRESTADOR")) {
				new LiberacaoRepository().excluirLiberacaoNaoMonitorada(registroDao.consultaRegistroPeloId(id));
				Messages.addGlobalInfo("Registro excluido com sucesso!");
			}
			limpar();
			consultaUtimosRegistros();
			SharedListBean.consultaLiberadosSaida();
		} catch (Exception e) {
			e.printStackTrace();
			Messages.addGlobalError("Erro ao excluir registro");
		}
	}

	/**
	 * Realiza um verificao no banco para verificar o se o regitro não possui
	 * vinculos e pode ser excluido.
	 * 
	 * @param r
	 * @return
	 */
	private boolean verificaPermissaoExclusao(Registro r) {
		boolean confirm = false;
		Registro reg = new Registro();
		reg = registroDao.consultaRegistroPeloId(r.getId());
		if (reg.getStatus().equals("ABERTO")) {
			confirm = true;
		}
		return confirm;
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
			registros = registroDao.consultaUltimosRegistros();
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
		numeroNotas = new ArrayList<>();
		cpf = "";
		nome = "";
		placa = "";
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

	public String getEmp() {
		return emp;
	}

	public void setEmp(String emp) {
		this.emp = emp;
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

	public NotaRegistro getNotasRegistroEntrada() {
		return notaRegistro;
	}

	public void setNotasRegistroEntrada(NotaRegistro notaRegistro) {
		this.notaRegistro = notaRegistro;
	}

	public String getNfe() {
		return nfe;
	}
	
	/**
	 * Utiliza a função ReplaceAll para remover espacos em branco
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

	public List<String> getNumeroNotas() {
		return numeroNotas;
	}

	public void setNumeroNotas(List<String> numeroNotas) {
		this.numeroNotas = numeroNotas;
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