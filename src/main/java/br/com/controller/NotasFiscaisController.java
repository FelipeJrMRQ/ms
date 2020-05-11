package br.com.controller;

import java.util.HashSet;
import java.util.Set;

import br.com.ms.model.NotaRegistro;
import br.com.ms.model.Registro;
import br.com.ms.util.ConverteChaveDeAcesso;

public class NotasFiscaisController {
	
	public NotaRegistro carregaNota(String chaveDeAcesso, Registro registro) {
		String nf = ConverteChaveDeAcesso.getNumeroNfe(chaveDeAcesso);
		// listNfe.add(MontaRegistroNfe.getNfe(nfe, registro));
		NotaRegistro n = new NotaRegistro();
		n.setChave(chaveDeAcesso);
		n.setRegistro(registro);
		n.setNumeroNfe(nf);
		return n;
	}
	
	public Set<String> addNota(String nfe){
		Set<String> list = new HashSet<>();
		list.add(nfe);
		return list;
	}
	
	
}
