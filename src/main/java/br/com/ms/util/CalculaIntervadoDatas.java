package br.com.ms.util;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class CalculaIntervadoDatas {

	/**
	 * Realiza o cálculo do intervalo entre duas datas e retorna o valor no formato
	 * de texto
	 * 
	 * @param entrada
	 * @param saida
	 * @return
	 */
	public static String intevalo(Date entrada, Date saida) {
		try {
			Instant dataInicial = entrada.toInstant();
			Instant dataFinal = saida.toInstant();
			Duration d = Duration.between(dataInicial, dataFinal);
			return (verificaHora(d.toMillis() / 3600000)) + ":" + verificaMinuto(((d.toMillis() / 60000) % 60)) + ":" + verificaSegundo(((d.toMillis() / 1000) % 60));
		} catch (Exception e) {
			throw e;
		}
	}

	private static String verificaHora(long hora) {
		String h = "";
		if (hora < 10) {
			h = "0" + hora;
		} else {
			h = "" + hora;
		}
		return h;
	}

	private static String verificaMinuto(long minuto) {
		String m = "";
		if (minuto < 10) {
			m = "0" + minuto;
		} else {
			m = "" + minuto;
		}
		return m;
	}

	private static String verificaSegundo(long minuto) {
		String m = "";
		if (minuto < 10) {
			m = "0" + minuto;
		} else {
			m = "" + minuto;
		}
		return m;
	}
}
