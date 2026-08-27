package com.davivienda.sv.app.dto.colecturia.validar;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.ToString;

@ToString
public class AlternativaPago {

	@JacksonXmlProperty(localName = "leyenda")
	private String leyenda;

	@JacksonXmlProperty(localName = "valor")
	private double valor;

	@JacksonXmlProperty(localName = "preferido")
	private String preferido;

	public AlternativaPago() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AlternativaPago(String leyenda, double valor, String preferido) {
		super();
		this.leyenda = leyenda;
		this.valor = valor;
		this.preferido = preferido;
	}

	public String getLeyenda() {
		return leyenda;
	}

	public void setLeyenda(String leyenda) {
		this.leyenda = leyenda;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public String getPreferido() {
		return preferido;
	}

	public void setPreferido(String preferido) {
		this.preferido = preferido;
	}

}
