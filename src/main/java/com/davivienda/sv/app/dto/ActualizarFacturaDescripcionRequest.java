package com.davivienda.sv.app.dto;

public class ActualizarFacturaDescripcionRequest {
	  private String estado;
      private String descripcion;

      public ActualizarFacturaDescripcionRequest(String estado, String descripcion) {
		super();
		this.estado = estado;
		this.descripcion = descripcion;
	}
	// Getters y Setters
      public String getEstado() { return estado; }
      public void setEstado(String estado) { this.estado = estado; }

      public String getDescripcion() { return descripcion; }
      public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
