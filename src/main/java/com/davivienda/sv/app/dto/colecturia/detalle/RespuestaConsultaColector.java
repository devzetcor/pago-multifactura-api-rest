package com.davivienda.sv.app.dto.colecturia.detalle;

import com.davivienda.sv.app.util.SubContenedor;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.ToString;

@SubContenedor
@ToString
public class RespuestaConsultaColector {

    @JacksonXmlProperty(localName = "tipoMensajeRespuesta")
    private String tipoMensajeRespuesta;

    @JacksonXmlProperty(localName = "respuestaInfoColector")
    RespuestaInfoColector respuestaInfoColector;

	public RespuestaConsultaColector() {
		super();
	}

	public RespuestaConsultaColector(String tipoMensajeRespuesta, RespuestaInfoColector respuestaInfoColector) {
		super();
		this.tipoMensajeRespuesta = tipoMensajeRespuesta;
		this.respuestaInfoColector = respuestaInfoColector;
	}

	public String getTipoMensajeRespuesta() {
		return tipoMensajeRespuesta;
	}

	public void setTipoMensajeRespuesta(String tipoMensajeRespuesta) {
		this.tipoMensajeRespuesta = tipoMensajeRespuesta;
	}

	public RespuestaInfoColector getRespuestaInfoColector() {
		return respuestaInfoColector;
	}

	public void setRespuestaInfoColector(RespuestaInfoColector respuestaInfoColector) {
		this.respuestaInfoColector = respuestaInfoColector;
	}


}
