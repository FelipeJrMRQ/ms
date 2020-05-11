package br.com.ms.teste;

import br.com.ms.dao.RegistroDao;
import br.com.ms.dao.VisitanteDao;
import br.com.ms.model.Registro;
import br.com.ms.model.Visitante;


public class Teste{

	  
	

	
	public static void main(String[] args) {
		RegistroDao registroDao = new RegistroDao();
		
		  
		  Registro registro = new Registro();
		
		
		  Visitante visitante = new Visitante();
		
		  
		  VisitanteDao visitanteDao = new VisitanteDao();
		  
		  visitante.setId(278L);
		  
		  visitante = visitanteDao.consultaVisitantePorId(visitante.getId());
		  
		  System.out.println(visitante.getNome());
		  
	
	}
}
