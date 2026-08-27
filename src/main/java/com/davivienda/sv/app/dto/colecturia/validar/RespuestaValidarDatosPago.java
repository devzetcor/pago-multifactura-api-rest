package com.davivienda.sv.app.dto.colecturia.validar;

import com.davivienda.sv.app.util.SubContenedor;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import lombok.ToString;

@SubContenedor
@ToString
public class RespuestaValidarDatosPago {

	@JacksonXmlProperty(localName = "infoEnLinea")
	private InfoEnLinea infoEnLinea;

	@JacksonXmlProperty(localName = "montoTotal")
	private double montoTotal;

	@JacksonXmlProperty(localName = "montoParcial")
	private double montoParcial;

	@JacksonXmlProperty(localName = "valorMora")
	private double valorMora;

	public RespuestaValidarDatosPago() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RespuestaValidarDatosPago(InfoEnLinea infoEnLinea, double montoTotal, double montoParcial,
			double valorMora) {
		super();
		this.infoEnLinea = infoEnLinea;
		this.montoTotal = montoTotal;
		this.montoParcial = montoParcial;
		this.valorMora = valorMora;
	}

	public InfoEnLinea getInfoEnLinea() {
		return infoEnLinea;
	}

	public void setInfoEnLinea(InfoEnLinea infoEnLinea) {
		this.infoEnLinea = infoEnLinea;
	}

	public double getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(double montoTotal) {
		this.montoTotal = montoTotal;
	}

	public double getMontoParcial() {
		return montoParcial;
	}

	public void setMontoParcial(double montoParcial) {
		this.montoParcial = montoParcial;
	}

	public double getValorMora() {
		return valorMora;
	}

	public void setValorMora(double valorMora) {
		this.valorMora = valorMora;
	}

	// Getters y setters

}
	