package com.davivienda.sv.app.dto;

import java.io.Serializable;

/**
 * 
 * @author Christian Guillen
 * @since 17 jul 2023
 * @version 1.0
 *
 */
public class StackTrace implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String linea; 
	private String metodo; 
	private String clase; 
	private String resumen;
	private String archivo;
	
	public StackTrace() {
		super();
	}

	public String getLinea() {
		return linea;
	}

	public void setLinea(String linea) {
		this.linea = linea;
	}

	public String getMetodo() {
		return metodo;
	}

	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}

	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}

	public String getResumen() {
		return resumen;
	}

	public void setResumen(String resumen) {
		this.resumen = resumen;
	}

	public String getArchivo() {
		return archivo;
	}

	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}

	@Override
	public String toString() {
		return "StackTrace [linea=" + linea + ", metodo=" + metodo + ", clase=" + clase + ", resumen=" + resumen
				+ ", archivo=" + archivo + "]";
	}

}
