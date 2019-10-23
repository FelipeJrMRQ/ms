package br.com.ms.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import br.com.ms.model.Empresa;
import br.com.ms.model.Registro;
import br.com.ms.util.DAO;

public class EmpresaDao {
	private Transaction transaction;
	private Session session;
	
	private Session getSession() {
		return DAO.getSession();
	}

	public void salvarEmpresa(Empresa empresa) {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			session.save(empresa);
			transaction.commit();
		} catch (Exception erro) {
			throw erro;
		}
	}

	public void excluirEmpresa(Empresa e) {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			session.createQuery("delete from Empresa where id = " + e.getId() + "").executeUpdate();
			transaction.commit();
		} catch (Exception erro) {
			throw erro;
		} catch (Error ex) {
			throw ex;
		}
	}

	public void alterarEmpresa(Empresa empresa) {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			session.merge(empresa);
			transaction.commit();
		} catch (Exception erro) {
			throw erro;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Empresa> consultarEmpresa(String nome) {
		Empresa empresa = new Empresa();
		List<Empresa> empresas = new ArrayList<>();
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(empresa.getClass());
			consulta.add(Restrictions.like("nome", "%" + nome + "%", MatchMode.ANYWHERE));
			empresas = consulta.list();
			return empresas;
		} catch (RuntimeException erro) {
			throw erro;
		}
	}

	public Empresa consultarEmpresaPeloCnpj(String cnpj) {
		Empresa empresa = new Empresa();
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(empresa.getClass());
			consulta.add(Restrictions.eq("cnpj", cnpj));
			empresa = (Empresa) consulta.uniqueResult();
			return empresa;
		} catch (RuntimeException erro) {
			throw erro;
		}
	}
	
	public Registro consultaRegistroEmpresaPorId(long id) {
		Registro r = new Registro();
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(Registro.class);
			consulta.createAlias("empresa", "e");
			consulta.add(Restrictions.eq("e.id", id)).setMaxResults(1);
			r = (Registro) consulta.uniqueResult();
			return r;
		} catch (RuntimeException erro) {
			throw erro;
		}
	}
}
