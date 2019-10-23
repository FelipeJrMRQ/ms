package br.com.ms.util;

import java.util.Calendar;
import java.util.Date;

public class HoraDaInternet {
	
	public static Date getHora() throws Exception {
		try {
			//SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:MM");// se quiser apenas a hora apague o formato da data
//			String ntpServer = "a.st1.ntp.br";// servidor de horario brasileiro
//			NTPUDPClient timeClient = new NTPUDPClient();
//			InetAddress inetAddress = InetAddress.getByName(ntpServer);
//			TimeInfo timeInfo = timeClient.getTime(inetAddress);
//			long returnTime = timeInfo.getReturnTime();
			Date time = Calendar.getInstance().getTime();  //new Date(returnTime);
			return time;
		} catch (Exception ex) {
			throw ex;
		}
	}
}
