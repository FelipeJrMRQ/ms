package br.com.ms.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.ms.model.NotaRegistro;
import br.com.ms.model.Registro;
import br.com.ms.model.Visitante;
import br.com.ms.util.DAO;

public class RegistroDao {
	private Transaction transaction;
	private Registro registro;
	private Session session;

	private Session getSession() {
		return DAO.getSession();
	}
	public RegistroDao() {
		registro = new Registro();
	}

	public Registro salvar(Registro registro) {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			for (NotaRegistro nfe : registro.getNotas()) {
				nfe.setRegistro(registro);
			}
			registro = (Registro) session.merge(registro);
			transaction.commit();
			return registro;
		} catch (RuntimeException erro) {
			transaction.rollback();
			throw erro;
		}
	}

	public void excluir(Registro r) {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			Registro rg = new Registro();
			rg = consultaRegistroPeloId(r.getId());
			session.delete(rg);
			transaction.commit();
		} catch (RuntimeException erro) {
			transaction.rollback();
			throw erro;
		}
	}

	public void alterarRegistro(Registro r) {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			session.merge(r);
			session.flush();
			transaction.commit();
		} catch (RuntimeException e) {
			transaction.rollback();
			throw e;
		}
	}

	public void desfazerSaida(Registro r) {
		session = getSession();
		List<NotaRegistro> ls = new ArrayList<>();
		for (NotaRegistro n : r.getNotas()) {
			ls.add(n);
		}
		try {
			transaction = session.beginTransaction();
			session.update(r);
			session.flush();
			transaction.commit();
		} catch (RuntimeException e) {
			transaction.rollback();
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Registro> consultarRegistroPeloNomeDaEmpresa(String nome) {
		session = getSession();
		List<Registro> registros = new ArrayList<>();
		try {
			Criteria consulta = session.createCriteria(registro.getClass()).createAlias("empresa", "e").createAlias("visitante", "v");
			consulta.add(Restrictions.and(Restrictions.like("e.nome", "%" + nome + "%", MatchMode.ANYWHERE), Restrictions.eq("tipo", "ENTRADA"), Restrictions.eq("status", "ABERTO"), Restrictions.eq("v.tipo", "PRESTADOR"))).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			registros = consulta.list();
			return registros;
		} catch (RuntimeException erro) {
			throw erro;
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Registro> quantidadeEntradas() {
		List<Registro> lista = new ArrayList<>();
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(Registro.class);
			Date dFim = Calendar.getInstance().getTime();
			Date dataInicial = Calendar.getInstance().getTime();
			dFim.setDate(dFim.getDate());
			dFim.setMonth(dFim.getMonth());
			dFim.setYear(dFim.getYear());
			dFim.setHours(23);
			dFim.setMinutes(59);
			dFim.setSeconds(59);
			dataInicial.setHours(00);
			dataInicial.setMinutes(00);
			dataInicial.setSeconds(00);
			consulta.add(Restrictions.eq("tipo", "ENTRADA"));
			consulta.add(Restrictions.ge("data", dataInicial));
			consulta.add(Restrictions.le("data", dFim));
			lista = (List<Registro>) consulta.list();
			return lista;
		} catch (Exception e) {
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Registro> quantidadePresentes() {
		session = getSession();
		try {
			List<Registro> list = new ArrayList<>();
			list = (List<Registro>) session.createSQLQuery("select * from registro where registro.tipo ='ENTRADA' and registro.status = 'INICIADO' or registro.status = 'ABERTO' or registro.tipo = 'LIBERADO'").addEntity(Registro.class).list();
			return list;
		} catch (Exception e) {
			throw (e);
		}
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<Registro> quantidadeAtendimentos() {
		List<Registro> lista = new ArrayList<>();
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(Registro.class);
			Date dFim = Calendar.getInstance().getTime();
			Date dataInicial = Calendar.getInstance().getTime();
			dFim.setDate(dFim.getDate());
			dFim.setMonth(dFim.getMonth());
			dFim.setYear(dFim.getYear());
			dFim.setHours(23);
			dFim.setMinutes(59);
			dFim.setSeconds(59);
			dataInicial.setHours(00);
			dataInicial.setMinutes(00);
			dataInicial.setSeconds(00);
			consulta.add(Restrictions.eq("status", "INICIADO"));
			consulta.add(Restrictions.ge("data", dataInicial));
			consulta.add(Restrictions.le("data", dFim));
			lista = (List<Registro>) consulta.list();
			return lista;
		} catch (Exception e) {
			throw e;
		}
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<Registro> quantidadeSaidas() {
		List<Registro> lista = new ArrayList<>();
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(Registro.class);
			Date dFim = Calendar.getInstance().getTime();
			Date dataInicial = Calendar.getInstance().getTime();
			dFim.setDate(dFim.getDate());
			dFim.setMonth(dFim.getMonth());
			dFim.setYear(dFim.getYear());
			dFim.setHours(23);
			dFim.setMinutes(59);
			dFim.setSeconds(59);
			dataInicial.setHours(00);
			dataInicial.setMinutes(00);
			dataInicial.setSeconds(00);
			consulta.add(Restrictions.eq("tipo", "SAIDA"));
			consulta.add(Restrictions.ge("data", dataInicial));
			consulta.add(Restrictions.le("data", dFim));
			lista = (List<Registro>) consulta.list();
			return lista;
		} catch (Exception e) {
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Registro> consultarRegistroPeloNomeDoPrestador(String nome) {
		List<Registro> registros = new ArrayList<>();
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(registro.getClass()).createAlias("visitante", "v");
			consulta.add(Restrictions.and(Restrictions.like("v.nome", "%" + nome + "%", MatchMode.ANYWHERE), Restrictions.eq("status", "ABERTO"), Restrictions.eq("v.tipo", "PRESTADOR"))).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			registros = consulta.list();
			return registros;
		} catch (RuntimeException erro) {
			throw erro;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Registro> consultarRegistroPeloCpf(String cpf) {
		List<Registro> registros = new ArrayList<>();
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(registro.getClass()).createAlias("visitante", "v");
			consulta.add(Restrictions.and(Restrictions.eq("tipo", "ENTRADA"), Restrictions.eq("v.cpf", cpf), Restrictions.eq("status", "ABERTO"), Restrictions.eq("v.tipo", "PRESTADOR"))).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			registros = consulta.list();
			return registros;
		} catch (RuntimeException erro) {
			throw erro;
		}
	}

	public Registro consultaRegistroPeloCpf(String cpf) {
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(registro.getClass()).createAlias("visitante", "v");
			consulta.add(Restrictions.and(Restrictions.eq("tipo", "ENTRADA"), Restrictions.eq("v.cpf", cpf), Restrictions.eq("status", "ABERTO"), Restrictions.eq("v.tipo", "PRESTADOR"))).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			registro = (Registro) consulta.uniqueResult();
			return registro;
		} catch (RuntimeException erro) {
			throw erro;
		}
	}

	public Registro consultaRegistroPeloId(long id) {
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(Registro.class);
			consulta.add(Restrictions.eq("id", id)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			registro = (Registro) consulta.uniqueResult();
			return registro;
		} catch (RuntimeException erro) {
			throw erro;
		}
	}

	public Registro consultaRegistroPeloIdVisitante(Visitante v) {
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(Registro.class).createAlias("visitante", "v");
			consulta.add(Restrictions.eq("v.id", v.getId())).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			registro = (Registro) consulta.uniqueResult();
			return registro;
		} catch (RuntimeException erro) {
			throw erro;
		}
	}

	/**
	 * Executa a consulta no registro de registro seja ele do tipo registro ou saida
	 * 
	 * @param status
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Registro> consultarRegistroPeloTipo(String tipo) {
		List<Registro> registros = new ArrayList<>();
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(registro.getClass()).createAlias("visitante", "v");
			consulta.add(Restrictions.and(Restrictions.eq("tipo", tipo), Restrictions.eq("status", "ABERTO"), Restrictions.eq("v.tipo", "PRESTADOR"))).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			registros = consulta.list();
			return registros;
		} catch (RuntimeException erro) {
			throw erro;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Registro> consultarRegistroPelaNFE(String nfe) {
		List<Registro> registros = new ArrayList<>();
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(registro.getClass()).createAlias("nota", "nota").createAlias("visitante", "v");
			consulta.add(Restrictions.and(Restrictions.eq("tipo", "ENTRADA"), Restrictions.like("nota.numeroNfe", nfe), Restrictions.eq("v.tipo", "PRESTADOR")));
			registros = consulta.list();
			return registros;
		} catch (RuntimeException erro) {
			throw erro;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Registro> consultaEntradaDeVisitantes() {
		session = getSession();
		List<Registro> registros = new ArrayList<>();
		try {
			Criteria consulta = session.createCriteria(this.registro.getClass()).createAlias("visitante", "v");
			consulta.add(Restrictions.eq("v.tipo", "VISITANTE"));
			registros = consulta.list();
			return registros;
		} catch (Exception erro) {
			throw erro;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Registro> consultaUltimosRegistros() {
		session = getSession();
		List<Registro> registros = new ArrayList<>();
		try {
			Criteria consulta = session.createCriteria((this.registro.getClass())).setMaxResults(90).addOrder(Order.desc("data"));
			consulta.add(Restrictions.or(Restrictions.eq("tipo", "ENTRADA"), Restrictions.eq("tipo", "SAIDA")));
			registros = consulta.list();
			return registros;
		} catch (Exception e) {
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Registro> consultaLiberadors() {
		session = getSession();
		List<Registro> registros = new ArrayList<>();
		try {
			Criteria consulta = session.createCriteria((this.registro.getClass())).addOrder(Order.asc("data"));
			consulta.add(Restrictions.eq("tipo", "LIBERADO"));
			registros = consulta.list();
			return registros;
		} catch (Exception e) {
			throw e;
		} 
	}

	@SuppressWarnings("unchecked")
	public List<NotaRegistro> consultaNotasPorIdRegistro(Long id) {
		session = getSession();
		NotaRegistro notaRegistro = new NotaRegistro();
		List<NotaRegistro> notas = new ArrayList<>();
		try {
			Criteria consulta = session.createCriteria(notaRegistro.getClass());
			consulta.add(Restrictions.eq("registro.id", id));
			notas = consulta.list();
			return notas;
		} catch (RuntimeException erro) {
			throw erro;
		} 
	}
}
