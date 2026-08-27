package com.davivienda.sv.app.dto;

public class Cuenta {
	private String numero;
	private String tipo;
	private String alias;
	private String disponible;

	public Cuenta() {
	}

	public Cuenta(String numero, String tipo, String alias, String disponible) {
		super();
		this.numero = numero;
		this.tipo = tipo;
		this.alias = alias;
		this.disponible = disponible;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getDisponible() {
		return disponible;
	}

	public void setDisponible(String moneda) {
		this.disponible = moneda;
	}
}