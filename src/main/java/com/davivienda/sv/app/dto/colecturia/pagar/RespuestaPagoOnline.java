package com.davivienda.sv.app.dto.colecturia.pagar;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class RespuestaPagoOnline {

	@JacksonXmlProperty(localName = "codigorespuesta")
	private int codigoRespuesta;

	@JacksonXmlProperty(localName = "descripcion")
	private String descripcion;

	@JacksonXmlProperty(localName = "codigoautorizacion")
	private String codigoAutorizacion;

	@JacksonXmlProperty(localName = "tramaPeticion")
	private Trama tramaPeticion;

	@JacksonXmlProperty(localName = "tramaRespuesta")
	private Trama tramaRespuesta;

	public RespuestaPagoOnline() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getCodigoRespuesta() {
		return codigoRespuesta;
	}

	public void setCodigoRespuesta(int codigoRespuesta) {
		this.codigoRespuesta = codigoRespuesta;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCodigoAutorizacion() {
		return codigoAutorizacion;
	}

	public void setCodigoAutorizacion(String codigoAutorizacion) {
		this.codigoAutorizacion = codigoAutorizacion;
	}

	public Trama getTramaPeticion() {
		return tramaPeticion;
	}

	public void setTramaPeticion(Trama tramaPeticion) {
		this.tramaPeticion = tramaPeticion;
	}

	public Trama getTramaRespuesta() {
		return tramaRespuesta;
	}

	public void setTramaRespuesta(Trama tramaRespuesta) {
		this.tramaRespuesta = tramaRespuesta;
	}

}
