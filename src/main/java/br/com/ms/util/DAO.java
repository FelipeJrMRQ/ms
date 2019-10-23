package br.com.ms.util;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;

import org.hibernate.Session;

/*
 * Classe Responsável pela criação de uma unica sessão de conexão com o banco de dados.
 * O objetivo desta classe é otimizar o numero de conexão com o banco de dados;
 */
@ApplicationScoped
public abstract class DAO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Session s = HibernateUtil.getFrabricadeSessoes().openSession();
	
	/**
	 * Retorna um objeto do tipo Session do pacote
	 * org.hibernate.Session
	 * 
	 * @return Session
	 */
	public static Session getSession() {
		return s;
	}
}


