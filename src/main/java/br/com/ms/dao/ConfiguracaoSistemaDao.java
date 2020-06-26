package br.com.ms.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import br.com.ms.model.ConfiguracaoSistema;
import br.com.ms.util.HibernateUtil;

public class ConfiguracaoSistemaDao {
	private Transaction transaction;
	private Session session;
	private ConfiguracaoSistema configuracaoSistema;
	
	public ConfiguracaoSistemaDao() {
		configuracaoSistema = new ConfiguracaoSistema();
	}

	private Session getSession() {
		return HibernateUtil.getFrabricadeSessoes().openSession();
	}
	
	public ConfiguracaoSistema consultarConfiguracoes(Long id) {
		session = getSession();
		try {
			Criteria consulta = session.createCriteria(ConfiguracaoSistema.class);
			consulta.add(Restrictions.eq("id", id));
			return (ConfiguracaoSistema) consulta.uniqueResult();
		} catch (Exception erro) {
			throw erro;
		} finally {
			session.close();
		}
	}
	
	public ConfiguracaoSistema salvarConfig(ConfiguracaoSistema config) {
		session = getSession();
		try {
			transaction = session.beginTransaction();
			configuracaoSistema =(ConfiguracaoSistema) session.merge(config);
			transaction.commit();
			return configuracaoSistema;
		} catch (Exception erro) {
			transaction.rollback();
			throw erro;
		} finally {
			session.close();
		}
	}
}
