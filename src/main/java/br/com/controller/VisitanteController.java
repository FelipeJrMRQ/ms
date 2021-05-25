package br.com.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.ms.dao.VisitanteDao;
import br.com.ms.model.Empresa;
import br.com.ms.model.Visitante;
import br.com.ms.validacao.ValidaCpf;

@Controller
public class VisitanteController implements Serializable {

	@Autowired
	private VisitanteDao visitanteDao;

	@Autowired
	private Visitante visitante;

	private List<Visitante> visitantes;

	private static final long serialVersionUID = 1L;

	public VisitanteController() {
		// System.out.println("Visitante Controller");
		visitanteDao = new VisitanteDao();
		visitante = new Visitante();
		visitantes = new ArrayList<>();
	}

	public void salvar(Visitante visitante, List<Empresa> empresasEscolhidas, String tipoVisitante) throws Exception {
		visitante.setStatus(true);
		validaTamhoNome(visitante.getNome());
		validaCPF(visitante.getCpf());
		verificaExistenciaCPF(visitante);
		verificaExistenciaRg(visitante);
		verificaTipo(visitante, empresasEscolhidas, tipoVisitante);
	}

	public void alterar(Visitante visitante, List<Empresa> empresasEscolhidas, String tipoVisitante) throws Exception {
		validaTamhoNome(visitante.getNome());
		validaCPF(visitante.getCpf());
		verificaTipo(visitante, empresasEscolhidas, tipoVisitante);
	}

	public void exluir(Visitante visitante) throws Exception {
		try {
			visitante = visitanteDao.consultaVisitantePorId(visitante.getId());
			validaExclusao(visitante);
			visitanteDao.excluirVisitante(visitanteDao.consultaVisitantePorId(visitante.getId()));
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * Verifica se o prestador de serviço possuir permissão de acesso as
	 * dependencias da empresa true = Acesso liberado false = Acesso negado
	 * 
	 * @param visitante
	 * @return
	 */
	public Visitante verificaPermissaoDeAcesso(Visitante visitante) {
		this.visitante = visitanteDao.consultaVisitantePorId(visitante.getId());
		if (!visitante.getStatus()) {
			throw new IllegalArgumentException("Entrada bloqueada consulte o setor responsavel.");
		} else {
			return this.visitante;
		}
	}

	/**
	 * Realiza a consulta do prestadore de serviço pelo seu CPF verificando seu
	 * status
	 * 
	 * @param cpf
	 * @return
	 */
	public Optional<Visitante> consultaPorCPF(String cpf) {
		return Optional.ofNullable(verificaPermissaoDeAcesso(visitanteDao.consultaVisitantePeloCpf(cpf)));
	}

	public List<Visitante> consultaPorNome(String nomeVisitante) throws Exception {
		try {
			visitantes = visitanteDao.consultaVisitantePeloNome(nomeVisitante);
			if (visitantes.isEmpty()) {
				throw new Exception("Nome não encontrado!");
			}
			return visitantes;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public Optional<Visitante> consultaPorId(Long id) {
		return Optional.ofNullable(visitanteDao.consultaVisitantePorId(id));
	}

	public List<Visitante> consultaPorRg(String Rg) throws Exception {
		try {
			visitantes = visitanteDao.consultaVisitantePeloRg(Rg);
			if (visitantes.isEmpty()) {
				throw new Exception("Númemro de RG não encontrado!");
			}
			return visitantes;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public List<Visitante> consultaPeloRg(String consulta) {
		return visitantes;
	}

	private void verificaTipo(Visitante visitante, List<Empresa> empresasEscolhidas, String tipoVisitante) throws Exception {
		try {
			if (tipoVisitante != null) {
				if (tipoVisitante.equals("VISITANTE")) {
					salvarVisitante(visitante, empresasEscolhidas);
				} else if (tipoVisitante.equals("PRESTADOR")) {
					salvarPrestador(visitante, empresasEscolhidas);
				}
			}
		} catch (Exception ex) {
			throw ex;
		}
	}

	private void salvarVisitante(Visitante visitante, List<Empresa> empresasEscolhidas) {
		try {
			empresasEscolhidas = new ArrayList<>();
			visitante.setEmpresas(empresasEscolhidas);
			visitante.setTipo("VISITANTE");
			visitanteDao.salvarVisitante(visitante);
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * Salva um simples visitante
	 * 
	 * @param visitante
	 * @param empresasEscolhidas
	 * @throws Exception
	 */
	private void salvarPrestador(Visitante visitante, List<Empresa> empresasEscolhidas) throws Exception {
		try {
			visitante.setTipo("PRESTADOR");
			visitante.setEmpresas(empresasEscolhidas);
			validaEmpresas(visitante);
			visitanteDao.salvarVisitante(visitante);
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * Se o visitante for um prestador de serviço será necessário ter um vinculo com
	 * ao menos uma empresa selecionada
	 * 
	 * @param visitante
	 * @throws Exception
	 */
	private void validaEmpresas(Visitante visitante) throws Exception {
		if (visitante.getTipo().equals("PRESTADOR")) {
			if (visitante.getEmpresas() == null || visitante.getEmpresas().isEmpty()) {
				throw new Exception("Selecione ao menos uma empresa!");
			}
		}
	}

	/**
	 * Verifica se o CPF digitado é valido
	 * 
	 * @param cpf
	 * @return
	 * @throws Exception
	 */
	private boolean validaCPF(String cpf) throws Exception {
		if (cpf.isEmpty() || ValidaCpf.isCPF(cpf)) {
			return true;
		} else {
			throw new Exception("O CPF digitado não é válido");
		}
	}

	/**
	 * Exige o que o nome do visitante tenha no minimo 10 caracteres
	 * 
	 * @param nome
	 * @return
	 * @throws Exception
	 */
	private boolean validaTamhoNome(String nome) throws Exception {
		if (nome.length() >= 10) {
			return true;
		} else {
			throw new Exception("Preencha o nome completo!");
		}
	}

	/**
	 * Realiza consulta no banco de dados para verificar se o CPF já esta cadastrado
	 *
	 * @param visitante
	 * @throws Exception
	 */
	private void verificaExistenciaCPF(Visitante visitante) throws Exception {
		if (!visitante.getCpf().isEmpty()) {
			Visitante vis = visitanteDao.validaExistenciaCpf(visitante);
			if (vis != null) {
				throw new Exception("Este CPF já está cadastrado no sistema");
			}
		}
	}

	/**
	 * Realiza um consulta no banco de dados para verificar seu este numero de rg já
	 * esta cadastrado
	 * 
	 * @param visitante
	 * @throws Exception
	 */
	private void verificaExistenciaRg(Visitante visitante) throws Exception {
		if (!visitante.getRg().isEmpty()) {
			Visitante vis = visitanteDao.validaExistenciaRg(visitante);
			if (vis != null) {
				throw new Exception("Este RG já está cadastrado no sistema");
			}
		} else if (visitante.getCpf().isEmpty()) {
			throw new Exception("É necessário preencher o RG ou CPF!");
		}
	}

	/**
	 * Verifica se o visitante cadastrado esta com vinculo há algum outro registro
	 * que impossibilite sua exclusao
	 * 
	 * @param v
	 * @throws Exception
	 */
	private void validaExclusao(Visitante v) throws Exception {
		if (visitanteDao.consultaRegistroVisitantePorId(v.getId()) != null) {
			throw new Exception("Este registro possui vínculos que impossibilitam sua exclusão!");
		}
	}

}
