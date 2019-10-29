package br.com.ms.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.faces.bean.ApplicationScoped;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.service.ServiceRegistry;

@ApplicationScoped
public abstract class HibernateUtil {
	private static final SessionFactory fabricaDeSessoes = criarFabricaDeSessoes();

	public static Connection getConexao() {
		Session sessao = fabricaDeSessoes.openSession();
		Connection connection = sessao.doReturningWork(new ReturningWork<Connection>() {
			@Override
			public Connection execute(Connection conn) throws SQLException {
				return conn;
			}
		});
		return connection;
	}

	public static SessionFactory getFrabricadeSessoes() {
		return fabricaDeSessoes;
	}

	/*
	 * Classe de comunica��o com hibernate.
	 */
	private static SessionFactory criarFabricaDeSessoes() {
		try {
			// Busca a configura��o XML do Hibernate
			Configuration configuracao = new Configuration().configure();

			// Registando o servi�o passando para o registro as configura��es
			ServiceRegistry registro = new StandardServiceRegistryBuilder().applySettings(configuracao.getProperties()).build();

			// Criando uma fabria de sess�o passando o servi�o do registro.
			SessionFactory fabrica = configuracao.buildSessionFactory(registro);

			return fabrica;
		} catch (Throwable ex) {
			System.out.println("A fabrica de conex�o n�o pode ser criada " + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
}
