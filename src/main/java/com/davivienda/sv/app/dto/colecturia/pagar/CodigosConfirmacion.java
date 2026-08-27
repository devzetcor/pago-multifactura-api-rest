package com.davivienda.sv.app.dto.colecturia.pagar;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class CodigosConfirmacion {

    @JacksonXmlProperty(localName = "respuestaCargoAbonoCuenta")
    private RespuestaCargoAbonoCuenta respuestaCargoAbonoCuenta;

    @JacksonXmlProperty(localName = "respuestaPagoOnline")
    private RespuestaPagoOnline respuestaPagoOnline;

    @JacksonXmlProperty(localName = "respuestaRegistrarPagoFactura")
    private RespuestaRegistrarPagoFactura respuestaRegistrarPagoFactura;

	public CodigosConfirmacion() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RespuestaCargoAbonoCuenta getRespuestaCargoAbonoCuenta() {
		return respuestaCargoAbonoCuenta;
	}

	public void setRespuestaCargoAbonoCuenta(RespuestaCargoAbonoCuenta respuestaCargoAbonoCuenta) {
		this.respuestaCargoAbonoCuenta = respuestaCargoAbonoCuenta;
	}

	public RespuestaPagoOnline getRespuestaPagoOnline() {
		return respuestaPagoOnline;
	}

	public void setRespuestaPagoOnline(RespuestaPagoOnline respuestaPagoOnline) {
		this.respuestaPagoOnline = respuestaPagoOnline;
	}

	public RespuestaRegistrarPagoFactura getRespuestaRegistrarPagoFactura() {
		return respuestaRegistrarPagoFactura;
	}

	public void setRespuestaRegistrarPagoFactura(RespuestaRegistrarPagoFactura respuestaRegistrarPagoFactura) {
		this.respuestaRegistrarPagoFactura = respuestaRegistrarPagoFactura;
	}

   
}
