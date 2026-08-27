package com.davivienda.sv.app.dto.colecturia.validar;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import lombok.ToString;
@ToString
public class DatosValidar {
	@JacksonXmlProperty(localName = "npe")
	private String npe="";
	@JacksonXmlProperty(localName = "barra")
	private String barra="";
	@JacksonXmlProperty(localName = "categoria")
	private String categoria="";
	@JacksonXmlProperty(localName = "codigoCanal")
	private String codigoCanal="";

	public DatosValidar() {
	}

	public DatosValidar(String npe, String barra, String categoria, String codigoCanal) {
		super();
		this.npe = npe;
		this.barra = barra;
		this.categoria = categoria;
		this.codigoCanal = codigoCanal;
	}

	public String getNpe() {
		return npe;
	}

	public void setNpe(String npe) {
		this.npe = npe;
	}

	public String getBarra() {
		return barra;
	}

	public void setBarra(String barra) {
		this.barra = barra;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getCodigoCanal() {
		return codigoCanal;
	}

	public void setCodigoCanal(String codigoCanal) {
		this.codigoCanal = codigoCanal;
	}
}