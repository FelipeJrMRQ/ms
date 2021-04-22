package br.com.ms.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "logintegracao")
public class LogIntegracao implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4473338101914582776L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String log;
	private Date data;
	private String statushttp;
	
	
	public LogIntegracao() {
	
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	public String getStatushttp() {
		return statushttp;
	}
	public void setStatushttp(String statushttp) {
		this.statushttp = statushttp;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LogIntegracao other = (LogIntegracao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LogIntegracao [id=" + id + ", log=" + log + ", data=" + data + ", statushttp=" + statushttp + "]";
	}
	
	
	
}
