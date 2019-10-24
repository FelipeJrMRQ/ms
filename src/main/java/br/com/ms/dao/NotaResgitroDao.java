package br.com.ms.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import br.com.ms.model.NotaRegistro;
import br.com.ms.model.Registro;
import br.com.ms.util.HibernateUtil;

public class NotaResgitroDao {

	private Transaction transaction;
	private Session session;
	
	private Session getSession() {
		return HibernateUtil.getFrabricadeSessoes().openSession();
	}
	public void salvarNotas(NotaRegistro notas) {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			session.save(notas);
			transaction.commit();
		} catch (RuntimeException erro) {
			transaction.rollback();
			throw erro;
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<NotaRegistro> consultarNotasRegistroEntrada(Registro registroEntrada) {
		List<NotaRegistro> notas = new ArrayList<>();
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(getClass()).createAlias("registroEntrada", "r");
			consulta.add(Restrictions.eq("r.id", registroEntrada.getId())).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			notas = consulta.list();
			return notas;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			session.close();
		}
	}

	public void excluirNotasEntrada(NotaRegistro nota) {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			session.delete(nota);
			transaction.commit();
		} catch (RuntimeException erro) {
			transaction.rollback();
			throw erro;
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<NotaRegistro> consultaNotasNaoSincrozizadas(String data, String tipo) {
		session = getSession();
		try {
			List<NotaRegistro> lista = new ArrayList<>();
			lista = (List<NotaRegistro>) session.createSQLQuery("select * from notas_registro\n" + "inner join registro on registro.id = notas_registro.registro_id\n" + " where registro.data > '" + data + "' and !isnull(notas_registro.chave) and registro.tipo = '" + tipo + "'").addEntity(NotaRegistro.class).list();
			return lista;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}
}
