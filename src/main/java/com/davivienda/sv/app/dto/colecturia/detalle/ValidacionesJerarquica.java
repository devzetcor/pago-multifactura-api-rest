package com.davivienda.sv.app.dto.colecturia.detalle;

public class ValidacionesJerarquica {
    private String valEnLinea;
    private String valBaseDatoRecibida;
    private String valDigitoVerificador;
	public ValidacionesJerarquica() {
		super();
	}
	
	public ValidacionesJerarquica(String valEnLinea, String valBaseDatoRecibida, String valDigitoVerificador) {
		super();
		this.valEnLinea = valEnLinea;
		this.valBaseDatoRecibida = valBaseDatoRecibida;
		this.valDigitoVerificador = valDigitoVerificador;
	}

	public String getValEnLinea() {
		return valEnLinea;
	}
	public void setValEnLinea(String valEnLinea) {
		this.valEnLinea = valEnLinea;
	}
	public String getValBaseDatoRecibida() {
		return valBaseDatoRecibida;
	}
	public void setValBaseDatoRecibida(String valBaseDatoRecibida) {
		this.valBaseDatoRecibida = valBaseDatoRecibida;
	}
	public String getValDigitoVerificador() {
		return valDigitoVerificador;
	}
	public void setValDigitoVerificador(String valDigitoVerificador) {
		this.valDigitoVerificador = valDigitoVerificador;
	}

  
}
