package br.com.ms.util;


import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

public class AutenticarServidorNotas {
	public static void buscarNotaEntrada(String nota) throws Exception {
		try {
			NtlmPasswordAuthentication authentication = new NtlmPasswordAuthentication("croma", "administrador", "0566e7357");
			SmbFile home = new SmbFile("smb://125.67.2.242/SUPERSMART/NFE/PDF/", authentication);
			for (SmbFile file : home.listFiles()) {
				SmbFile in = new SmbFile(file.toString() + "/NF" +nota+".pdf", authentication);
				if(in.exists()) {
					HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
					res.setContentType("application/pdf");
					res.setHeader("Content-disposition", "inline;filename=arquivo.pdf");
					byte[] b = IOUtils.toByteArray(in.getInputStream());
					res.getOutputStream().write(b);
					res.getCharacterEncoding();
					FacesContext.getCurrentInstance().responseComplete();
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
}
