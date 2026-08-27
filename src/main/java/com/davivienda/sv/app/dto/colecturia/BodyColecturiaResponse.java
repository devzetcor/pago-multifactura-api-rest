package com.davivienda.sv.app.dto.colecturia;

import com.davivienda.sv.app.dto.RespuestaJ2Entorno;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;



public class BodyColecturiaResponse<T> {

	private RespuestaJ2Entorno<T> respuestaJ2Entorno;

	public BodyColecturiaResponse() {
		this.respuestaJ2Entorno = new RespuestaJ2Entorno<>();
	}
	@JacksonXmlProperty(localName = "respuestaEntorno")
	public RespuestaJ2Entorno<T> getRespuestaJ2Entorno() {
		return respuestaJ2Entorno;
	}

	public void setRespuestaJ2Entorno(RespuestaJ2Entorno<T> respuestaJ2Entorno) {
		this.respuestaJ2Entorno = respuestaJ2Entorno;
	}

	

}
