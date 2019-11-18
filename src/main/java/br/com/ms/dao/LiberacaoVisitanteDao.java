package br.com.ms.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import br.com.ms.model.LiberacaoVisitante;
import br.com.ms.model.Registro;
import br.com.ms.util.HibernateUtil;

public class LiberacaoVisitanteDao {
	private Transaction transaction;
	private LiberacaoVisitante liberacaoVisitante;
	private Session session;
	
	private Session getSession() {
		return HibernateUtil.getFrabricadeSessoes().openSession();
	}
	public LiberacaoVisitanteDao() {
		liberacaoVisitante = new LiberacaoVisitante();
	}

	public LiberacaoVisitante salvar(LiberacaoVisitante liberacao) {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			liberacaoVisitante = (LiberacaoVisitante) session.merge(liberacao);
			transaction.commit();
			return liberacaoVisitante;
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}finally {
			session.close();
		}
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<LiberacaoVisitante> consultarLiberacaoVisitante(Date dataInicial, Date dataFinal, String nome) {
		List<LiberacaoVisitante> lista = new ArrayList<>();
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(LiberacaoVisitante.class);
			consulta.createAlias("entrada.visitante", "v");
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
			consulta.add(Restrictions.like("v.nome", "%" + nome + "%")).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			for (LiberacaoVisitante liberacaoVisitante : lista) {
				liberacaoVisitante.getEntrada().getNotas().size();
				liberacaoVisitante.getSaida().getNotas().size();
			}
			lista = consulta.list();
			return lista;
		} catch (Exception e) {
			throw e;
		}finally {
			session.close();
		}
	}

	public LiberacaoVisitante consultarLiberacaoPorIdSaida(long id) {
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(LiberacaoVisitante.class).createAlias("saida", "s");
			consulta.add(Restrictions.eq("s.id", id)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			liberacaoVisitante = (LiberacaoVisitante) consulta.uniqueResult();
			liberacaoVisitante.getEntrada().getNotas().size();
			liberacaoVisitante.getSaida().getNotas().size();
			return liberacaoVisitante;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	public LiberacaoVisitante consultarLiberacaoPorIdEntrada(long id) {
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(LiberacaoVisitante.class).createAlias("entrada", "e");
			consulta.add(Restrictions.eq("e.id", id)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			liberacaoVisitante = (LiberacaoVisitante) consulta.uniqueResult();
			liberacaoVisitante.getEntrada().getNotas().size();
			liberacaoVisitante.getSaida().getNotas().size();
			return liberacaoVisitante;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}

	public void excluirLiberacaoVisitante(LiberacaoVisitante lb, Registro entrada, Registro saida) {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			session.delete(lb);
			session.delete(entrada);
			session.delete(saida);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}finally {
			session.close();
		}
	}
}
