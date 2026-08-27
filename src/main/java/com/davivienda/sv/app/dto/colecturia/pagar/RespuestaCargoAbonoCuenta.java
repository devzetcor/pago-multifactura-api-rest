package com.davivienda.sv.app.dto.colecturia.pagar;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class RespuestaCargoAbonoCuenta {

    @JacksonXmlProperty(localName = "confirmacionCargoAbono")
    private String confirmacionCargoAbono;

    @JacksonXmlProperty(localName = "fechaIBS")
    private String fechaIBS;

	public RespuestaCargoAbonoCuenta() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getConfirmacionCargoAbono() {
		return confirmacionCargoAbono;
	}

	public void setConfirmacionCargoAbono(String confirmacionCargoAbono) {
		this.confirmacionCargoAbono = confirmacionCargoAbono;
	}

	public String getFechaIBS() {
		return fechaIBS;
	}

	public void setFechaIBS(String fechaIBS) {
		this.fechaIBS = fechaIBS;
	}

  
}
