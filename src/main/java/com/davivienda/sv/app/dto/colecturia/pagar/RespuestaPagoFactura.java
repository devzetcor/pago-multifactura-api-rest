package com.davivienda.sv.app.dto.colecturia.pagar;

import com.davivienda.sv.app.util.SubContenedor;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;


@SubContenedor
public class RespuestaPagoFactura {

	@JacksonXmlProperty(localName = "codigosConfirmacion")
	private CodigosConfirmacion codigosConfirmacion;

	@JacksonXmlProperty(localName = "reversaEfectuada")
	private String reversaEfectuada;

	@JacksonXmlProperty(localName = "reversa")
	private Reversa reversa;

	public RespuestaPagoFactura() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CodigosConfirmacion getCodigosConfirmacion() {
		return codigosConfirmacion;
	}

	public void setCodigosConfirmacion(CodigosConfirmacion codigosConfirmacion) {
		this.codigosConfirmacion = codigosConfirmacion;
	}

	public String getReversaEfectuada() {
		return reversaEfectuada;
	}

	public void setReversaEfectuada(String reversaEfectuada) {
		this.reversaEfectuada = reversaEfectuada;
	}

	public Reversa getReversa() {
		return reversa;
	}

	public void setReversa(Reversa reversa) {
		this.reversa = reversa;
	}

}
