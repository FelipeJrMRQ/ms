package br.com.ms.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import br.com.ms.model.Usuario;
import br.com.ms.util.HibernateUtil;

public class UsuarioDao implements Serializable {

	private static final long serialVersionUID = 6204865180564306025L;

	private Transaction transaction;
	private Session session;
	
	private Session getSession() {
		return HibernateUtil.getFrabricadeSessoes().openSession();
	}

	public void salvarUsuario(Usuario u) {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			session.merge(u);
			transaction.commit();
		} catch (Exception er) {
			transaction.rollback();
			throw er;
		}finally {
			session.close();
		}
	}

	public void alterarUsuario(Usuario u) {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			session.merge(u);
			transaction.commit();
		} catch (Exception er) {
			transaction.rollback();
			throw er;
		} finally {
			session.close();
		}
	}

	public void excluirUsuario(Usuario u) {

	}

	@SuppressWarnings("unchecked")
	public List<Usuario> buscarUsuarioPorNome(String nome) {
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(Usuario.class);
			return consulta.add(Restrictions.like("nome", "%" + nome + "%", MatchMode.ANYWHERE)).list();
		} catch (Exception err) {
			throw err;
		}finally {
			session.close();
		}
	}
	
	public Usuario buscarPorCodigoProgramador(String codigo) {
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(Usuario.class);
			return (Usuario) consulta.add(Restrictions.eq("codigoProgramador", codigo)).uniqueResult();
		} catch (Exception err) {
			throw err;
		}finally {
			session.close();
		}
	}

	public Usuario autenticarUsuario(Usuario usuario) {
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(Usuario.class);
			consulta.add(Restrictions.and(Restrictions.eq("nome", usuario.getNome()), Restrictions.eq("senha", usuario.getSenha())));
			Usuario resultado = (Usuario) consulta.uniqueResult();
			return resultado;
		} catch (Exception erro) {
			throw erro;
		}finally {
			session.close();
		}
	}
}
