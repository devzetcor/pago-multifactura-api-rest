package com.davivienda.sv.app.dto.colecturia.validar;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import lombok.ToString;
@ToString
public class DatoEnLinea {

	@JacksonXmlProperty(localName = "etiqueta")
	private String etiqueta;

	@JacksonXmlProperty(localName = "valor")
	private String valor;

	public DatoEnLinea() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DatoEnLinea(String etiqueta, String valor) {
		super();
		this.etiqueta = etiqueta;
		this.valor = valor;
	}

	// Getters y setters
	public String getEtiqueta() {
		return etiqueta;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}
