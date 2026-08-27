package com.davivienda.sv.app.dto.colecturia.detalle;

public class CalculoMora {
	private String tipoDeMora;
	private String valorMora;
	private String realizarCalculoMoraLinea;
	private String calculoMoraEspecializada;

	public CalculoMora() {
		super();
	}

	public String getTipoDeMora() {
		return tipoDeMora;
	}

	public void setTipoDeMora(String tipoDeMora) {
		this.tipoDeMora = tipoDeMora;
	}

	public String getValorMora() {
		return valorMora;
	}

	public void setValorMora(String valorMora) {
		this.valorMora = valorMora;
	}

	public String getRealizarCalculoMoraLinea() {
		return realizarCalculoMoraLinea;
	}

	public void setRealizarCalculoMoraLinea(String realizarCalculoMoraLinea) {
		this.realizarCalculoMoraLinea = realizarCalculoMoraLinea;
	}

	public String getCalculoMoraEspecializada() {
		return calculoMoraEspecializada;
	}

	public void setCalculoMoraEspecializada(String calculoMoraEspecializada) {
		this.calculoMoraEspecializada = calculoMoraEspecializada;
	}

}
