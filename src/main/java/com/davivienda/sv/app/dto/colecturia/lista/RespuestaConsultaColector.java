package com.davivienda.sv.app.dto.colecturia.lista;

import com.davivienda.sv.app.util.SubContenedor;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@SubContenedor
public class RespuestaConsultaColector {

    @JacksonXmlProperty(localName = "tipoMensajeRespuesta")
    private String tipoMensajeRespuesta;

    @JacksonXmlProperty(localName = "respuestaListaColector")
    RespuestaListaColector respuestaListaColector;

	public RespuestaConsultaColector() {
		super();
	}

	public RespuestaConsultaColector(String tipoMensajeRespuesta, RespuestaListaColector respuestaListaColector) {
		super();
		this.tipoMensajeRespuesta = tipoMensajeRespuesta;
		this.respuestaListaColector = respuestaListaColector;
	}

	public String getTipoMensajeRespuesta() {
		return tipoMensajeRespuesta;
	}

	public void setTipoMensajeRespuesta(String tipoMensajeRespuesta) {
		this.tipoMensajeRespuesta = tipoMensajeRespuesta;
	}

	public RespuestaListaColector getRespuestaListaColector() {
		return respuestaListaColector;
	}

	public void setRespuestaListaColector(RespuestaListaColector respuestaListaColector) {
		this.respuestaListaColector = respuestaListaColector;
	}

	


}
