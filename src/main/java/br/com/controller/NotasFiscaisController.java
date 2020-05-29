package br.com.controller;

import java.util.ArrayList;
import java.util.List;

import br.com.ms.model.NotaRegistro;
import br.com.ms.model.Registro;
import br.com.ms.util.ConverteChaveDeAcesso;

public class NotasFiscaisController {

	private List<String> listNfe;

	public NotasFiscaisController() {
		listNfe = new ArrayList<>();
	}

	public NotaRegistro carregaNota(String chaveDeAcesso, Registro registro) {
		String nf = ConverteChaveDeAcesso.getNumeroNfe(chaveDeAcesso);
		// listNfe.add(MontaRegistroNfe.getNfe(nfe, registro));
		NotaRegistro n = new NotaRegistro();
		n.setChave(chaveDeAcesso);
		n.setRegistro(registro);
		n.setNumeroNfe(nf);
		return n;
	}

	public NotaRegistro addNota(String chaveDeAcesso, Registro registro) {
		String numeroNf = ConverteChaveDeAcesso.getNumeroNfe(chaveDeAcesso);
		if (!listNfe.contains(numeroNf)) {
			listNfe.add(numeroNf);
			return montarNotaFiscal(chaveDeAcesso, registro, numeroNf);
		}else {
			throw new IllegalArgumentException("Este número de nota já foi inserido!");
		}
	}
	
	public void removeNota(String numero) {
		listNfe.remove(numero);
	}

	private NotaRegistro montarNotaFiscal(String chaveDeAcesso, Registro registro, String numeroNf) {
		try {
			NotaRegistro notaRegistro = new NotaRegistro();
			notaRegistro.setChave(chaveDeAcesso);
			notaRegistro.setNumeroNfe(numeroNf);
			notaRegistro.setRegistro(registro);
			return notaRegistro;
		} catch (Exception e) {
			throw e;
		}
	}

	public void limparListaDeNotas() {
		listNfe = new ArrayList<>();
	}
}
