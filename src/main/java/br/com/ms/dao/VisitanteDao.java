package br.com.ms.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import br.com.ms.model.Registro;
import br.com.ms.model.Visitante;
import br.com.ms.util.HibernateUtil;


@Component
public class VisitanteDao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Transaction transaction;
	private Visitante visitante;
	private Session session;
	
	public VisitanteDao() {
		visitante = new Visitante();
	}
	
	private Session getSession() {
		return HibernateUtil.getFrabricadeSessoes().openSession();
	}
	
	public void salvarVisitante(Visitante visitante) {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			session.merge(visitante);
			transaction.commit();
		} catch (Exception erro) {
			transaction.rollback();
			throw erro;
		}finally {
			session.close();
		}
	}

	public void excluirVisitante(Visitante visitante) {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			session.delete(visitante);
			transaction.commit();
		} catch (Exception er) {
			transaction.rollback();
			throw er;
		}finally {
			session.close();
		}
	}

	public Visitante consultaVisitantePeloCpf(String cpf) {
		Visitante visitante = new Visitante();
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(visitante.getClass());
			consulta.add(Restrictions.eq("cpf", cpf));
			visitante = (Visitante) consulta.uniqueResult();
			if(visitante != null) {
				visitante.getEmpresas().size();
			}
			return visitante;
		} catch (Exception erro) {
			throw erro;
		}finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Visitante> consultaVisitantePeloRg(String rg) {
		List<Visitante> prestadores = new ArrayList<>();
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(Visitante.class);
			consulta.add(Restrictions.like("rg", "%" + rg + "%", MatchMode.ANYWHERE));
			prestadores = consulta.list();
			for (Visitante visitante : prestadores) {
				visitante.getEmpresas().size();
			}
			return prestadores;
		} catch (Exception erro) {
			throw erro;
		}finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Visitante> consultaVisitantePeloNome(String nome) {
		List<Visitante> prestadores = new ArrayList<>();
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(Visitante.class);
			consulta.add(Restrictions.like("nome", "%" + nome + "%", MatchMode.ANYWHERE));
			consulta.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			consulta.setMaxResults(40);
			prestadores = consulta.list();
			for (Visitante visitante : prestadores) {
				visitante.getEmpresas().size();
			}
			return prestadores;
		} catch (Exception erro) {
			throw erro;
		}finally {
			session.close();
		}
	}

	public Visitante consultaVisitantePorId(long id) {
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(Visitante.class);
			consulta.add(Restrictions.eq("id", id)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			this.visitante = (Visitante) consulta.uniqueResult();
			visitante.getEmpresas().size();
			return visitante;
		} catch (RuntimeException erro) {
			throw erro;
		}finally {
			session.close();
		}
	}

	public Registro consultaRegistroVisitantePorId(long id) {
		Registro r = new Registro();
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(Registro.class);
			consulta.createAlias("visitante", "v");
			consulta.add(Restrictions.eq("v.id", id)).setMaxResults(1);
			r = (Registro) consulta.uniqueResult();
			try {
				r.getNotas().size();
			}catch(NullPointerException e) {
				
			}
			return r;
		} catch (RuntimeException erro) {
			throw erro;
		}finally {
			session.close();
		}
	}

	/**
	 * Verifica a existencia do Rg no cadastro e true quando existe e false quando
	 * não existe
	 * 
	 * @param v
	 * @return
	 */
	public Visitante validaExistenciaRg(Visitante visitante) {
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(Visitante.class);
			consulta.add(Restrictions.eq("rg", visitante.getRg())).setMaxResults(1);
			return (Visitante) consulta.uniqueResult() ;
		} catch (Exception e) {
			throw e;
		}finally {
			session.close();
		}
	}

	/**
	 * Verifica a existencia do CPF no cadastro e true quando existe e false quando
	 * não existe
	 * 
	 * @param v
	 * @return
	 */
	public Visitante validaExistenciaCpf(Visitante visitante) {
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(Visitante.class);
			consulta.add(Restrictions.eq("cpf", visitante.getCpf())).setMaxResults(1);
			return (Visitante) consulta.uniqueResult();
		} catch (Exception e) {
			throw e;
		}finally {
			session.close();
		}
	}

}
