package br.com.ms.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import br.com.ms.model.LiberacaoDeUso;
import br.com.ms.util.DAO;

public class LiberacaoDeUsoDao {
	private Transaction transaction;
	private Session session;
	
	private Session getSession() {
		return DAO.getSession();
	}
	public void salvarLiberacao(LiberacaoDeUso lb) {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			session.merge(lb);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<LiberacaoDeUso> consultaLiberacaoDeUso(){
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(LiberacaoDeUso.class);
			consulta.add(Restrictions.eq("id", 1));
			return consulta.list();
		}catch(Exception e) {
			throw e;
		}
	}
}
