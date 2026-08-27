package com.davivienda.sv.app.dto.colecturia.pagar;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class AtributoColector {
	@JacksonXmlProperty(localName = "idAtributo")
	private String idAtributo;
	@JacksonXmlProperty(localName = "valorAtributoPantalla")
	private String valorAtributoPantalla;
	@JacksonXmlProperty(localName = "nombre")
	private String nombre;
	@JacksonXmlProperty(localName = "srvValidacion")
	private String srvValidacion;

	public AtributoColector() {
	}

	public AtributoColector(String idAtributo, String valorAtributoPantalla, String srvValidacion) {
		super();
		this.idAtributo = idAtributo;
		this.valorAtributoPantalla = valorAtributoPantalla;
		this.srvValidacion = srvValidacion;
	}

	public String getIdAtributo() {
		return idAtributo;
	}

	public void setIdAtributo(String idAtributo) {
		this.idAtributo = idAtributo;
	}

	public String getValorAtributoPantalla() {
		return valorAtributoPantalla;
	}

	public void setValorAtributoPantalla(String valorAtributoPantalla) {
		this.valorAtributoPantalla = valorAtributoPantalla;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getSrvValidacion() {
		return srvValidacion;
	}

	public void setSrvValidacion(String srvValidacion) {
		this.srvValidacion = srvValidacion;
	}
}