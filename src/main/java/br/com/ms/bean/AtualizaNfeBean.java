package br.com.ms.bean;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;

import br.com.ms.dao.NotaResgitroDao;
import br.com.ms.dao.RegistroDao;
import br.com.ms.model.NotaRegistro;
import br.com.ms.model.Registro;
import br.com.ms.nfe.MontaRegistroNfe;
import br.com.swconsultoria.certificado.exception.CertificadoException;
import br.com.swconsultoria.nfe.exception.NfeException;

@ManagedBean
@ViewScoped
public class AtualizaNfeBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6816686737400007298L;
	private List<NotaRegistro> notas;
	private NotaResgitroDao dao;
	private Date data;
	private NotaRegistro notaRegistro;
	private NotaRegistro novaNota;
	//private Registro registro;
	private RegistroDao rDao;
	private List<NotaRegistro> auxiliar;
	private String tipo;

	public AtualizaNfeBean() {
		notas = new ArrayList<NotaRegistro>();
		dao = new NotaResgitroDao();
		data = Calendar.getInstance().getTime();
		//registro = new Registro();
		notaRegistro = new NotaRegistro();
		rDao = new RegistroDao();
		auxiliar = new ArrayList<>();
		tipo = "ENTRADA";
	}

	public void consultaNotasDesatualizadas() {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			auxiliar = dao.consultaNotasNaoSincrozizadas(format.format(data), tipo);
			if (!auxiliar.isEmpty()) {
				for (NotaRegistro notaRegistro : auxiliar) {
					if (notaRegistro.getValor() == null && notaRegistro.getChave() != null) {
						if (notaRegistro.getChave().length() == 44 && !notas.contains(notaRegistro)) {
							notas.add(notaRegistro);
						}
					}
				}
			} else {
				Messages.addGlobalInfo("Não há notas para sincronizar.");
			}
		} catch (Exception e) {
			Messages.addGlobalError("Não foi possível realizar esta consulta");
		}
	}

	public void atualizaInterno() {
		for (NotaRegistro notaRegistro : notas) {
			Registro registro = new Registro();
			registro = rDao.consultaRegistroPeloId(notaRegistro.getRegistro().getId());
			for (NotaRegistro nr : registro.getNotas()) {
				try {
					MontaRegistroNfe montaReg = new MontaRegistroNfe();
					novaNota = montaReg.getNfe(notaRegistro.getRegistro(), notaRegistro.getChave());
					nr.setNome(novaNota.getNome());
					nr.setNumeroNfe(novaNota.getNumeroNfe());
					nr.setCnpj(novaNota.getCnpj());
					nr.setEmissao(novaNota.getEmissao());
					nr.setValor(novaNota.getValor());
					rDao.alterarRegistro(registro);
					limpar();
					// consultaNotasDesatualizadas();
				} catch (FileNotFoundException e) {
					Messages.addGlobalFatal("Não atualizado tente novamente");
				} catch (IOException e) {
					Messages.addGlobalFatal("Não atualizado tente novamente");
				} catch (NfeException e) {
					Messages.addGlobalFatal("Não atualizado tente novamente");
				} catch (CertificadoException e) {
					Messages.addGlobalFatal("Não atualizado tente novamente");
				} catch (ParseException e) {
					Messages.addGlobalFatal("Não atualizado tente novamente");
				} catch (Exception e) {
					Messages.addGlobalFatal(e.getMessage());
				}
			}
		}
	}

	public void atualizarEmMass() {
		try {
			for (NotaRegistro notaRegistro : notas) {
				Registro r = new Registro();
				r = rDao.consultaRegistroPeloId(notaRegistro.getRegistro().getId());
				for (NotaRegistro nr : r.getNotas()) {
					novaNota = MontaRegistroNfe.getNfe(notaRegistro.getChave(), notaRegistro.getRegistro());
					nr.setNome(novaNota.getNome());
					nr.setNumeroNfe(novaNota.getNumeroNfe());
					nr.setCnpj(novaNota.getCnpj());
					nr.setEmissao(novaNota.getEmissao());
					nr.setValor(novaNota.getValor());
					rDao.alterarRegistro(r);
				}
			}
			limpar();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			notas = dao.consultaNotasNaoSincrozizadas(sdf.format(data), tipo);
		} catch (FileNotFoundException e) {

			Messages.addGlobalFatal("Não atualizado tente novamente");
		} catch (IOException e) {

			Messages.addGlobalFatal("Não atualizado tente novamente");
		} catch (NfeException e) {

			Messages.addGlobalFatal("Não atualizado tente novamente");
		} catch (CertificadoException e) {

			Messages.addGlobalFatal("Não atualizado tente novamente");
		} catch (ParseException e) {

			Messages.addGlobalFatal("Não atualizado tente novamente");
		}

	}

	public void limpar() {
		notaRegistro = new NotaRegistro();
		//registro = new Registro();
		novaNota = new NotaRegistro();
		auxiliar = new ArrayList<>();
		notas = new ArrayList<>();
	}

	public void notaSelecionada(ActionEvent event) {
		try {
			notaRegistro = (NotaRegistro) event.getComponent().getAttributes().get("notaSelecionada");
			Registro r = new Registro();
			r = rDao.consultaRegistroPeloId(notaRegistro.getRegistro().getId());
			for (NotaRegistro notaRegistro : r.getNotas()) {
				novaNota = MontaRegistroNfe.getNfe(notaRegistro.getChave(), notaRegistro.getRegistro());
				notaRegistro.setNome(novaNota.getNome());
				notaRegistro.setNumeroNfe(novaNota.getNumeroNfe());
				notaRegistro.setCnpj(novaNota.getCnpj());
				notaRegistro.setEmissao(novaNota.getEmissao());
				notaRegistro.setValor(novaNota.getValor());
				rDao.alterarRegistro(r);
			}
			limpar();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			notas = dao.consultaNotasSincrozizadas(sdf.format(data), tipo);
		} catch (FileNotFoundException e) {

			Messages.addGlobalFatal("Não atualizado tente novamente");
		} catch (IOException e) {

			Messages.addGlobalFatal("Não atualizado tente novamente");
		} catch (NfeException e) {

			Messages.addGlobalFatal("Não atualizado tente novamente");
		} catch (CertificadoException e) {

			Messages.addGlobalFatal("Não atualizado tente novamente");
		} catch (ParseException e) {

			Messages.addGlobalFatal("Não atualizado tente novamente");
		}
	}

	public List<NotaRegistro> getNotas() {
		return notas;
	}

	public void setNotas(List<NotaRegistro> notas) {
		this.notas = notas;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
