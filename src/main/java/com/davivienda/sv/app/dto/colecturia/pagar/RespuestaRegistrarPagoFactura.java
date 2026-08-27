package com.davivienda.sv.app.dto.colecturia.pagar;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class RespuestaRegistrarPagoFactura {

	@JacksonXmlProperty(localName = "confirmacionPago")
	private String confirmacionPago;

	public RespuestaRegistrarPagoFactura() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getConfirmacionPago() {
		return confirmacionPago;
	}

	public void setConfirmacionPago(String confirmacionPago) {
		this.confirmacionPago = confirmacionPago;
	}

}
