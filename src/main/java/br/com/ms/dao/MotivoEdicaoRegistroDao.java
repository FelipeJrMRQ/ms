package br.com.ms.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import br.com.ms.model.MotivoEdicaoRegistro;
import br.com.ms.util.HibernateUtil;

public class MotivoEdicaoRegistroDao {

	private Transaction transaction;
	private Session session;

	private Session getSession() {
		return HibernateUtil.getFrabricadeSessoes().openSession();
	}

	/**
	 * Para editar um registro será necessário que o usuário informe o motivo da
	 * edição Este método ficará reponsável por salvar e manter este regitro
	 * 
	 * @param motivo
	 */
	public void salvar(MotivoEdicaoRegistro motivo) {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			session.merge(motivo);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	public MotivoEdicaoRegistro consultar(Long idConsulta) {
		session = getSession();
		try {
			MotivoEdicaoRegistro mt = new MotivoEdicaoRegistro();
			Criteria consulta = session.createCriteria(MotivoEdicaoRegistro.class);
			consulta.createAlias("registro", "r");
			consulta.add(Restrictions.eq("r.id", idConsulta)).setMaxResults(1);
			mt = (MotivoEdicaoRegistro) consulta.uniqueResult();
			return mt;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();

		}
	}
}
