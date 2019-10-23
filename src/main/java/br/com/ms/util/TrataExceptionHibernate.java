package br.com.ms.util;

import org.omnifaces.util.Messages;

public class TrataExceptionHibernate {

	public TrataExceptionHibernate() {

	}

	public static void getErro(Exception exception) {
		try {
			if (exception.getCause().getMessage().toLowerCase().contains("duplicate entry")) {
				Messages.addGlobalError("Este cadastro já existe!");
			} else if (exception.getCause().getMessage().toLowerCase().contains("cannot delete or update a parent row: a foreign key constraint fails")) {
				Messages.addGlobalError("Este resgitro possui um vínculo que impossibilita sua exclusão!");
			} else if (exception.getCause().getMessage().toLowerCase().contains("O CNPJ digitado é inválido!")) {
				Messages.addGlobalInfo("O CNPJ digitado é inválido!");
			}
		} catch (NullPointerException e) {
			Messages.addGlobalInfo("Erro aqui " +e.getMessage() );
		}
	}

}
