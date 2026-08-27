package com.davivienda.sv.app.dto;

public class ActualizarFacturaCompletaRequest {
	 private String nuevoEstado;
     private String referencia;
     private String descripcionError;

     public ActualizarFacturaCompletaRequest(String nuevoEstado, String referencia, String descripcionError) {
		super();
		this.nuevoEstado = nuevoEstado;
		this.referencia = referencia;
		this.descripcionError = descripcionError;
	}
	// Getters y Setters
     public String getNuevoEstado() { return nuevoEstado; }
     public void setNuevoEstado(String nuevoEstado) { this.nuevoEstado = nuevoEstado; }

     public String getReferencia() { return referencia; }
     public void setReferencia(String referencia) { this.referencia = referencia; }

     public String getDescripcionError() { return descripcionError; }
     public void setDescripcionError(String descripcionError) { this.descripcionError = descripcionError; }
}
