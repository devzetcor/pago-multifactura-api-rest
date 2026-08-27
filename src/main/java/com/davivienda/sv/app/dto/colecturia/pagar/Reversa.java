package com.davivienda.sv.app.dto.colecturia.pagar;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Reversa {

	@JacksonXmlProperty(localName = "respuestaReversaCargoAbono")
	private RespuestaReversaCargoAbono respuestaReversaCargoAbono;

	public Reversa() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RespuestaReversaCargoAbono getRespuestaReversaCargoAbono() {
		return respuestaReversaCargoAbono;
	}

	public void setRespuestaReversaCargoAbono(RespuestaReversaCargoAbono respuestaReversaCargoAbono) {
		this.respuestaReversaCargoAbono = respuestaReversaCargoAbono;
	}

}
