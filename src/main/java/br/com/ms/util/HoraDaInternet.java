package br.com.ms.util;

import java.util.Calendar;
import java.util.Date;

public class HoraDaInternet {
	
	public static Date getHora() {
		try {
			Date time = Calendar.getInstance().getTime();  //new Date(returnTime);
			return time;
		} catch (Exception ex) {
			throw ex;
		}
	}
}
