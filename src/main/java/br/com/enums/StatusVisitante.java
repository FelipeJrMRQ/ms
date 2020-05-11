package br.com.enums;

public enum StatusVisitante {
		
     BLOQUEADO(0, "BLOQUEADO"), LIBERADO(1, "LIBERADO");
	
	private String descricao;
	private int codigo;
	
	private StatusVisitante(int codigo, String descrição) {
		this.codigo = codigo;
		this.descricao = descrição;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public int getCodigo() {
		return codigo;
	}
	
	public static StatusVisitante toEnum(Integer codigo) {
		if(codigo == null) {
			return null;
		}
		
		for(StatusVisitante s: StatusVisitante.values()) {
			if(codigo.equals(s.getCodigo())) {
				return s;
			}
		}
		throw new IllegalArgumentException("Id inválido: "+ codigo);
	}
}
