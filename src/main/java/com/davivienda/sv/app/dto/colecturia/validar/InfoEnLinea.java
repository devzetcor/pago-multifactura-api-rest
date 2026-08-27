package com.davivienda.sv.app.dto.colecturia.validar;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import lombok.ToString;

import java.util.List;
@ToString
public class InfoEnLinea {

	@JacksonXmlElementWrapper(localName = "alternativasPago")
	@JacksonXmlProperty(localName = "alternativaPago")
	private List<AlternativaPago> alternativasPago;

	@JacksonXmlElementWrapper(localName = "datosEnLinea")
	@JacksonXmlProperty(localName = "datoEnLinea")
	private List<DatoEnLinea> datosEnLinea;

	@JacksonXmlProperty(localName = "publicidad")
	private Publicidad publicidad;

	public InfoEnLinea() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InfoEnLinea(List<AlternativaPago> alternativasPago, List<DatoEnLinea> datosEnLinea, Publicidad publicidad) {
		super();
		this.alternativasPago = alternativasPago;
		this.datosEnLinea = datosEnLinea;
		this.publicidad = publicidad;
	}

	// Getters y setters
	public List<AlternativaPago> getAlternativasPago() {
		return alternativasPago;
	}

	public void setAlternativasPago(List<AlternativaPago> alternativasPago) {
		this.alternativasPago = alternativasPago;
	}

	public List<DatoEnLinea> getDatosEnLinea() {
		return datosEnLinea;

	}

	public void setDatosEnLinea(List<DatoEnLinea> datosEnLinea) {
		this.datosEnLinea = datosEnLinea;
	}

	public Publicidad getPublicidad() {
		return publicidad;
	}

	public void setPublicidad(Publicidad publicidad) {
		this.publicidad = publicidad;
	}

}
