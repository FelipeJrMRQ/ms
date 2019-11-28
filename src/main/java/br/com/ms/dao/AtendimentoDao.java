package br.com.ms.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.ms.model.Atendimento;
import br.com.ms.model.Registro;
import br.com.ms.util.HibernateUtil;

public class AtendimentoDao {
	private Transaction transaction;
	private Session session;

	private Session getSession() {
		return HibernateUtil.getFrabricadeSessoes().openSession();
	}

	private void getSize(List<Atendimento> lista) {
		for (Atendimento atendimento : lista) {
			atendimento.getRegistro().getNotas().size();
		}
	}

	public AtendimentoDao() {

	}

	public void salvarAtendimento(Atendimento atendimento) throws Exception {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			session.save(atendimento);
			transaction.commit();
		} catch (Exception erro) {
			transaction.rollback();
			throw erro;
		} finally {
			session.close();
		}
	}

	/**
	 * Realiza o salvamento de um inicio de atendimento e realiza a alteração do
	 * status do registro
	 * 
	 * @param atendimento
	 * @param r
	 * @throws Exception
	 */
	public void salvarAtendimento(Atendimento atendimento, Registro r) throws Exception {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			session.save(atendimento);
			session.merge(r);
			transaction.commit();
		} catch (Exception erro) {
			transaction.rollback();
			throw erro;
		} finally {
			session.close();
		}
	}

	public Atendimento alterarAtendimento(Atendimento atendimento) {
		Atendimento at = new Atendimento();
		session = getSession();
		try {
			transaction = session.beginTransaction();
			at = (Atendimento) session.merge(atendimento);
			transaction.commit();
			return at;
		} catch (Exception erro) {
			transaction.rollback();
			throw erro;
		} finally {
			session.close();
		}
	}

	/**
	 * Realiza a alteração do atendimento junto com a alteração do registro
	 * 
	 * @param atendimento
	 * @param r
	 * @return
	 */
	public Atendimento alterarAtendimento(Atendimento atendimento, Registro r) {
		Atendimento at = new Atendimento();
		session = getSession();
		try {
			transaction = session.beginTransaction();
			at = (Atendimento) session.merge(atendimento);
			r.setStatus("FINALIZADO");
			session.merge(r);
			transaction.commit();
			return at;
		} catch (Exception erro) {
			transaction.rollback();
			throw erro;
		} finally {
			session.close();
		}
	}

	public void updateAtendimento(Atendimento atendimento) {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			session.merge(atendimento);
			transaction.commit();
		} catch (Exception erro) {
			transaction.rollback();
			throw erro;
		} finally {
			session.close();
		}
	}

	public void excluirAtendimento(Atendimento atendimento) {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			session.delete(atendimento);
			transaction.commit();
		} catch (Exception erro) {
			transaction.rollback();
			throw erro;
		} finally {
			session.close();
		}
	}

	/**
	 * Realiza a exclusão de um atendimento e a alteração do registro para p estado
	 * de aberto registro
	 * 
	 * @param atendimento
	 * @param r
	 */
	public void excluirAtendimento(Atendimento atendimento, Registro r) {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			session.delete(atendimento);
			session.merge(r);
			transaction.commit();
		} catch (Exception erro) {
			transaction.rollback();
			throw erro;
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Atendimento> consultarAtentimento(String status) {
		session = getSession();
		Atendimento atendimento = new Atendimento();
		List<Atendimento> atendimentos = new ArrayList<>();
		try {
			Criteria consulta = session.createCriteria(atendimento.getClass());
			consulta.add(Restrictions.eq("status", status));
			atendimentos = consulta.list();
			getSize(atendimentos);
			return atendimentos;
		} catch (Exception erro) {
			throw erro;
		} finally {
			session.close();
		}
	}

	public Atendimento consultarAtentimentoPorId() {
		session = getSession();
		Atendimento at = new Atendimento();
		try {
			Criteria consulta = session.createCriteria(Atendimento.class);
			consulta.setMaxResults(1).addOrder(Order.desc("id"));
			at = (Atendimento) consulta.uniqueResult();
			at.getRegistro().getNotas().size();
			return at;
		} catch (Exception erro) {
			throw erro;
		} finally {
			session.close();
		}
	}

	public Atendimento consultarAtentimentoPorId(long id) {
		session = getSession();
		Atendimento at = new Atendimento();
		try {
			Criteria consulta = session.createCriteria(Atendimento.class);
			consulta.add(Restrictions.eq("id", id));
			at = (Atendimento) consulta.uniqueResult();
			at.getRegistro().getNotas().size();
			return at;
		} catch (Exception erro) {
			throw erro;
		} finally {
			session.close();
		}
	}

	/**
	 * Este método é utilizado para evitar duplicidade no inicio de atendimentos
	 * onde é feita uma consulta na tabela de atendimentos para verificar se aquele
	 * registro em questão já está presente na tabela de atendimentos se ele já
	 * existir será retornada uma mensagem informando ao usuário sobre o problema
	 * 
	 * @param id
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void consultarAtendimentoPorRegistro(long id) throws Exception {
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(Atendimento.class);
			consulta.add(Restrictions.eq("registro.id", id));
			List<Atendimento> ls = consulta.list();
			if (!ls.isEmpty()) {
				throw new Exception("Este atendimento já foi iniciado");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}

	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Atendimento> consultarAtentimento(String status, Date data) {
		session = getSession();
		Atendimento atendimento = new Atendimento();
		Date dFim = new Date();
		dFim.setDate(data.getDate());
		dFim.setMonth(data.getMonth());
		dFim.setYear(data.getYear());
		dFim.setHours(23);
		dFim.setMinutes(59);
		dFim.setSeconds(59);
		List<Atendimento> atendimentos = new ArrayList<>();
		try {
			Criteria consulta = session.createCriteria(atendimento.getClass());
			consulta.add(Restrictions.and(Restrictions.eq("status", status), Restrictions.ge("data_inicio", data), Restrictions.le("data_inicio", dFim)));
			atendimentos = consulta.list();
			getSize(atendimentos);
			return atendimentos;
		} catch (Exception erro) {
			throw erro;
		} finally {
			session.close();
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Atendimento> consultarAtentimento(String status, Date data, Date dFim) {
		session = getSession();
		Atendimento atendimento = new Atendimento();
		dFim.setHours(23);
		dFim.setMinutes(59);
		dFim.setSeconds(59);
		List<Atendimento> atendimentos = new ArrayList<>();
		try {
			Criteria consulta = session.createCriteria(atendimento.getClass());
			consulta.add(Restrictions.and(Restrictions.eq("status", status), Restrictions.ge("data_inicio", data), Restrictions.le("data_inicio", dFim))).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			atendimentos = consulta.list();
			getSize(atendimentos);
			return atendimentos;
		} catch (Exception erro) {
			throw erro;
		} finally {
			session.close();
		}
	}
}
