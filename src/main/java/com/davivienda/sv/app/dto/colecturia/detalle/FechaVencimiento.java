package com.davivienda.sv.app.dto.colecturia.detalle;

public class FechaVencimiento {
    private String validarFechaVencimiento;
    private String accionValFecVencimiento;
	public FechaVencimiento() {
		super();
	}
	
	public FechaVencimiento(String validarFechaVencimiento, String accionValFecVencimiento) {
		super();
		this.validarFechaVencimiento = validarFechaVencimiento;
		this.accionValFecVencimiento = accionValFecVencimiento;
	}

	public String getValidarFechaVencimiento() {
		return validarFechaVencimiento;
	}
	public void setValidarFechaVencimiento(String validarFechaVencimiento) {
		this.validarFechaVencimiento = validarFechaVencimiento;
	}
	public String getAccionValFecVencimiento() {
		return accionValFecVencimiento;
	}
	public void setAccionValFecVencimiento(String accionValFecVencimiento) {
		this.accionValFecVencimiento = accionValFecVencimiento;
	}

  
}
