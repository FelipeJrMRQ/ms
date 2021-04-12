package br.com.ms.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import br.com.ms.model.Integracao;
import br.com.ms.model.LogIntegracao;
import br.com.ms.util.HibernateUtil;

public class IntegracaoDao {
	private Transaction transaction;
	private Session session;
	
	private Session getSession() {
		return HibernateUtil.getFrabricadeSessoes().openSession();
	}
	
	public void ativarIntegracao(Integracao integracao) {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			session.merge(integracao);
			transaction.commit();
		} catch (Exception erro) {
			throw erro;
		}finally {
			session.close();
		}
	}
	
	public Integracao consultaParametros(Integer id) {
		Integracao integracao = new Integracao();
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(integracao.getClass());
			consulta.add(Restrictions.eq("id", id));
			integracao = (Integracao) consulta.uniqueResult();
			return integracao;
		} catch (RuntimeException erro) {
			throw erro;
		}finally {
			session.close();
		}
	}
	
	public void salvarLog(LogIntegracao log) {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			session.merge(log);
			transaction.commit();
		} catch (Exception erro) {
			throw erro;
		}finally {
			session.close();
		}
	}
}
