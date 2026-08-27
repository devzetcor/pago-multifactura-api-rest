package com.davivienda.sv.app.dto.colecturia.detalle;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.ToString;
@ToString
public class ValorPosibleAtributo {
	@JacksonXmlProperty(localName = "despliegue")
	private String despliegue;
	@JacksonXmlProperty(localName = "valorAtributo")
	private String valorAtributo;

	public ValorPosibleAtributo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ValorPosibleAtributo(String despliegue, String valorAtributo) {
		super();
		this.despliegue = despliegue;
		this.valorAtributo = valorAtributo;
	}

	public String getDespliegue() {
		return despliegue;
	}

	public void setDespliegue(String despliegue) {
		this.despliegue = despliegue;
	}

	public String getValorAtributo() {
		return valorAtributo;
	}

	public void setValorAtributo(String valorAtributo) {
		this.valorAtributo = valorAtributo;
	}

}
