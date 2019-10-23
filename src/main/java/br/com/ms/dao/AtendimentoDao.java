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
import br.com.ms.util.DAO;

public class AtendimentoDao {
	private Transaction transaction;
	private Session session;
	
	private Session getSession() {
		return DAO.getSession();
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
		}
	}

	public Atendimento alterarAtendimento(Atendimento atendimento) {
		Atendimento at = new Atendimento();
		session = getSession();
		try {
			transaction = session.beginTransaction();
			at = (Atendimento) session.merge(atendimento);
			session.flush();
			transaction.commit();
			return at;
		} catch (Exception erro) {
			transaction.rollback();
			throw erro;
		}
	}

	public void updateAtendimento(Atendimento atendimento) {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			session.merge(atendimento);
			session.flush();
			transaction.commit();
		} catch (Exception erro) {
			transaction.rollback();
			throw erro;
		}
	}

	public void excluirAtendimento(Atendimento atendimento) {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			session.delete(atendimento);
			session.flush();
			transaction.commit();
		} catch (Exception erro) {
			transaction.rollback();
			throw erro;
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
			return atendimentos;
		} catch (Exception erro) {
			throw erro;
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
		}
	}

	public Atendimento consultarAtentimentoPorId(long id) {
		session = getSession();
		Atendimento at = new Atendimento();
		try {
			Criteria consulta = session.createCriteria(Atendimento.class);
			consulta.add(Restrictions.eq("id", id));
			at = (Atendimento) consulta.uniqueResult();
			return at;
		} catch (Exception erro) {
			throw erro;
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
			Criteria consulta = session.createCriteria(Atendimento.class);
			consulta.add(Restrictions.eq("registro.id", id));
			List<Atendimento> ls = consulta.list();
			if (!ls.isEmpty()) {
				throw new Exception("Este atendimento já foi iniciado");
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
			return atendimentos;
		} catch (Exception erro) {
			throw erro;
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
			return atendimentos;
		} catch (Exception erro) {
			throw erro;
		}
	}
}
