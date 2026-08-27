package com.davivienda.sv.app.dto.colecturia.validar;

import com.davivienda.sv.app.util.SubContenedor;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import lombok.ToString;

@SubContenedor
@ToString
public class ValidarDatosPago {

	@JacksonXmlProperty(localName = "datosValidar")
	private DatosValidar datosValidar;
	@JacksonXmlProperty(localName = "respuestaInfoColector")
	private RespuestaInfoColector respuestaInfoColector;

	public ValidarDatosPago() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ValidarDatosPago(DatosValidar datosValidar
			, RespuestaInfoColector respuestaInfoColector
			) {
		super();
		this.datosValidar = datosValidar;
		this.respuestaInfoColector = respuestaInfoColector;
	}

	public DatosValidar getDatosValidar() {
		return datosValidar;
	}

	public void setDatosValidar(DatosValidar datosValidar) {
		this.datosValidar = datosValidar;
	}

	public RespuestaInfoColector getRespuestaInfoColector() {
		return respuestaInfoColector;
	}

	public void setRespuestaInfoColector(RespuestaInfoColector respuestaInfoColector) {
		this.respuestaInfoColector = respuestaInfoColector;
	}
}
