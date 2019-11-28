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

import org.omnifaces.util.Messages;

import br.com.ms.dao.NotaResgitroDao;
import br.com.ms.model.NotaRegistro;
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
	private Date data;
	private NotaResgitroDao nDao;
	private NotaRegistro novaNota;
	private String tipo;
	private Integer progress;
	private Integer sizeProgress;

	public AtualizaNfeBean() {
		notas = new ArrayList<NotaRegistro>();
		data = Calendar.getInstance().getTime();
		tipo = "ENTRADA";
		nDao = new NotaResgitroDao();
	}
	
	public void executeProgresso() {
		atualizaInterno();
	}

	public void consultaNotasNaoAtualizadas() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			notas = nDao.consultaNotasEntradaNaoSincronizada(sdf.format(data), tipo);
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void resultado() {
		getProgress();
	}
	

	public void atualizaInterno() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		notas = nDao.consultaNotasEntradaNaoSincronizada(sdf.format(data), tipo);
		for (NotaRegistro nrg : notas) {
			if (nrg.getCnpj() == null) {
				try {
					MontaRegistroNfe montaNota = new MontaRegistroNfe();
					novaNota = montaNota.getNfe(nrg.getRegistro(), nrg.getChave());
					nrg.setNome(novaNota.getNome());
					nrg.setNumeroNfe(novaNota.getNumeroNfe());
					nrg.setCnpj(novaNota.getCnpj());
					nrg.setEmissao(novaNota.getEmissao());
					nrg.setValor(novaNota.getValor());
					nDao.alterar(nrg);
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		notas = nDao.consultaNotasEntradaNaoSincronizada(sdf.format(data), tipo);
		for (NotaRegistro nrg : notas) {
			if (nrg.getCnpj() == null) {
				try {
					novaNota = MontaRegistroNfe.getNfe(nrg.getChave().trim(), nrg.getRegistro());
					nrg.setNome(novaNota.getNome());
					nrg.setNumeroNfe(novaNota.getNumeroNfe());
					nrg.setCnpj(novaNota.getCnpj());
					nrg.setEmissao(novaNota.getEmissao());
					nrg.setValor(novaNota.getValor());
					nDao.alterar(nrg);
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

	public void limpar() {
		novaNota = new NotaRegistro();
		notas = new ArrayList<>();
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

	public Integer getProgress() {
		if(progress == null) {
			progress = 0;
		}
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	public Integer getSizeProgress() {
		return sizeProgress;
	}

	public void setSizeProgress(Integer sizeProgress) {
		this.sizeProgress = sizeProgress;
	}

}
