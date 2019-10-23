package br.com.ms.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import br.com.ms.model.Liberacao;
import br.com.ms.util.DAO;

public class LiberacaoDao {
	private Transaction transaction;
	private Session session;
	
	private Session getSession() {
		return DAO.getSession();
	}

	public void salvarLiberacao(Liberacao liberacao) {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			session.merge(liberacao);
			session.flush();
			transaction.commit();
		} catch (Exception ex) {
			transaction.rollback();
			throw ex;
		}
	}
	
	public void excluirLiberacao(Liberacao liberacao) {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			session.delete(liberacao);
			session.flush();
			transaction.commit();
		} catch (Exception ex) {
			transaction.rollback();
			throw ex;
		}
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<Liberacao> consultarPeloNomeDoPrestador(Date dataInicial, Date DataFinal, String nome) {
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(Liberacao.class);
			consulta.createAlias("entrada.visitante", "e");
			Date dFim = new Date();
			dFim.setDate(DataFinal.getDate());
			dFim.setMonth(DataFinal.getMonth());
			dFim.setYear(DataFinal.getYear());
			dFim.setHours(23);
			dFim.setMinutes(59);
			dFim.setSeconds(59);
			dataInicial.setHours(00);
			dataInicial.setMinutes(00);
			dataInicial.setSeconds(00);
			consulta.add(Restrictions.ge("dataLiberacao", dataInicial));
			consulta.add(Restrictions.le("dataLiberacao", dFim));
			consulta.add(Restrictions.like("e.nome", "%" + nome + "%")).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			List<Liberacao> lista = new ArrayList<>();
			lista = consulta.list();
			return lista;
		} catch (Exception ex) {
			throw ex;
		}
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<Liberacao> consultarPeloNomeDaEmpresa(Date dataInicial, Date DataFinal, String nome) {
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(Liberacao.class);
			consulta.createAlias("entrada.empresa", "e");
			Date dFim = new Date();
			dFim.setDate(DataFinal.getDate());
			dFim.setMonth(DataFinal.getMonth());
			dFim.setYear(DataFinal.getYear());
			dFim.setHours(23);
			dFim.setMinutes(59);
			dFim.setSeconds(59);
			dataInicial.setHours(00);
			dataInicial.setMinutes(00);
			dataInicial.setSeconds(00);
			consulta.add(Restrictions.ge("dataLiberacao", dataInicial));
			consulta.add(Restrictions.le("dataLiberacao", dFim));
			consulta.add(Restrictions.like("e.nome", "%" + nome + "%")).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			List<Liberacao> lista = new ArrayList<>();
			lista = consulta.list();
			return lista;
		} catch (Exception ex) {
			throw ex;
		} 
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<Liberacao> consultarPorCategoria(Date dataInicial, Date dataFinal, String nome) {
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(Liberacao.class);
			consulta.createAlias("entrada.empresa", "e");
			Date dFim = new Date();
			dFim.setDate(dataFinal.getDate());
			dFim.setMonth(dataFinal.getMonth());
			dFim.setYear(dataFinal.getYear());
			dFim.setHours(23);
			dFim.setMinutes(59);
			dFim.setSeconds(59);
			dataInicial.setHours(00);
			dataInicial.setMinutes(00);
			dataInicial.setSeconds(00);
			consulta.add(Restrictions.ge("dataLiberacao", dataInicial));
			consulta.add(Restrictions.le("dataLiberacao", dFim));
			consulta.add(Restrictions.eq("e.categoria", Integer.parseInt(nome))).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			List<Liberacao> lista = new ArrayList<>();
			lista = consulta.list();
			return lista;
		} catch (Exception ex) {
			throw ex;
		} 
	}

	@SuppressWarnings("unchecked")
	public List<Liberacao> consultarPeloNumeroNfEntrada(String nf) {
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(Liberacao.class);
			consulta.createAlias("entrada.notas", "nota");
			consulta.add(Restrictions.like("nota.numeroNfe", "%" + nf + "%")).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			List<Liberacao> lista = new ArrayList<>();
			lista = consulta.list();
			return lista;
		} catch (Exception ex) {
			throw ex;
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Liberacao> consultarPelaPlacaVeiculo(Date dataInicial, Date dataFinal, String placa) {
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(Liberacao.class);
			consulta.createAlias("entrada", "e");
			Date dFim = new Date();
			dFim.setDate(dataFinal.getDate());
			dFim.setMonth(dataFinal.getMonth());
			dFim.setYear(dataFinal.getYear());
			dFim.setHours(23);
			dFim.setMinutes(59);
			dFim.setSeconds(59);
			dataInicial.setHours(00);
			dataInicial.setMinutes(00);
			dataInicial.setSeconds(00);
			consulta.add(Restrictions.ge("dataLiberacao", dataInicial));
			consulta.add(Restrictions.le("dataLiberacao", dFim));
			consulta.add(Restrictions.like("e.placaVeiculo", "%" + placa + "%")).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			List<Liberacao> lista = new ArrayList<>();
			lista = consulta.list();
			return lista;
		} catch (Exception ex) {
			throw ex;
		} 
	}

	@SuppressWarnings("unchecked")
	public List<Liberacao> consultarPeloNumeroNfSaida(String nf) {
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(Liberacao.class);
			consulta.createAlias("saida.notas", "nota");
			consulta.add(Restrictions.like("nota.numeroNfe", "%" + nf + "%")).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			List<Liberacao> lista = new ArrayList<>();
			lista = consulta.list();
			return lista;
		} catch (Exception ex) {
			throw ex;
		}
	}

	@SuppressWarnings("unchecked")
	public boolean consultarPorId(Long id) {
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(Liberacao.class);
			consulta.createAlias("atendimento", "a");
			consulta.add(Restrictions.eq("a.id", id)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			List<Liberacao> lista = new ArrayList<>();
			lista = consulta.list();
			if (lista.isEmpty()) {
				return false;
			} else {
				return true;
			}
		} catch (Exception ex) {
			throw ex;
		} 
	}
	
	public Liberacao consultaPorId(Long id) {
		session = getSession();
		Liberacao lib = new Liberacao();
		try {
			Criteria consulta = session.createCriteria(Liberacao.class);
			consulta.createAlias("atendimento", "a");
			consulta.add(Restrictions.eq("id", id)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			lib = (Liberacao) consulta.uniqueResult();
			return lib;
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * Reliza uma consulta na tabela de liberação pelo id do registro de saída
	 * retorna a liberação encontrada ao metodo que a chamou
	 * 
	 * @param id
	 * @return
	 * 
	 *         Liberacao
	 */
	public Liberacao consultarPorIdDoRegistroDeSaida(Long id) {
		Liberacao lib = new Liberacao();
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(Liberacao.class);
			consulta.createAlias("saida", "s");
			consulta.add(Restrictions.eq("s.id", id)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			lib = (Liberacao) consulta.uniqueResult();
			return lib;
		} catch (Exception ex) {
			throw ex;
		} 
	}

	public Liberacao consultarPorIdDoRegistroDeEntrada(Long id) {
		Liberacao lib = new Liberacao();
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(Liberacao.class);
			consulta.createAlias("entrada", "e");
			consulta.add(Restrictions.eq("e.id", id)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			lib = (Liberacao) consulta.uniqueResult();
			return lib;
		} catch (Exception ex) {
			throw ex;
		}
	}
}
