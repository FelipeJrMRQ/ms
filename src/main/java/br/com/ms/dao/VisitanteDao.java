package br.com.ms.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import br.com.ms.model.Registro;
import br.com.ms.model.Visitante;
import br.com.ms.util.HibernateUtil;

public class VisitanteDao {

	private Transaction transaction;
	private Visitante visitante;
	private Session session;
	
	public VisitanteDao() {
		visitante = new Visitante();
	}
	
	private Session getSession() {
		return HibernateUtil.getFrabricadeSessoes().openSession();
	}
	
	public void salvarPrestadorDeServico(Visitante p) {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			session.merge(p);
			transaction.commit();
		} catch (Exception erro) {
			transaction.rollback();
			throw erro;
		}finally {
			session.close();
		}
	}

	public void excluirPrestadorDeServico(Visitante p) {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			// DAO.getSession().delete(p);
			session.createQuery("delete from Visitante where id = " + p.getId() + "").executeUpdate();
			transaction.commit();
		} catch (Exception er) {
			transaction.rollback();
			throw er;
		}finally {
			session.close();
		}
	}

	public Visitante consultaPrestadorPeloCpf(String cpf) {
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
	public List<Visitante> consultaPrestadorPeloRg(String rg) {
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
	public List<Visitante> consultaPrestadorDeServicoPeloNome(String nome) {
		List<Visitante> prestadores = new ArrayList<>();
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(Visitante.class);
			consulta.add(Restrictions.like("nome", "%" + nome + "%", MatchMode.ANYWHERE));
			consulta.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
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
			r.getNotas().size();
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
	public Visitante validaExistenciaRg(Visitante v) {
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(Visitante.class);
			consulta.add(Restrictions.eq("rg", v.getRg())).setMaxResults(1);
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
	public Visitante validaExistenciaCpf(Visitante v) {
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(Visitante.class);
			consulta.add(Restrictions.eq("cpf", v.getCpf())).setMaxResults(1);
			return (Visitante) consulta.uniqueResult();
		} catch (Exception e) {
			throw e;
		}finally {
			session.close();
		}
	}

}
