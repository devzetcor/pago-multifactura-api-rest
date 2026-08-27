package com.davivienda.sv.app.dto;

public class Tarjeta {
	private String numero;
	private String alias;
	private String mesExpiracion;
	private String anioExpiracion;

	public Tarjeta() {
	}

	public Tarjeta(String numero, String alias, String mesExpiracion, String anioExpiracion) {
		super();
		this.numero = numero;
		this.alias = alias;
		this.mesExpiracion = mesExpiracion;
		this.anioExpiracion = anioExpiracion;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getMesExpiracion() {
		return mesExpiracion;
	}

	public void setMesExpiracion(String mesExpiracion) {
		this.mesExpiracion = mesExpiracion;
	}

	public String getAnioExpiracion() {
		return anioExpiracion;
	}

	public void setAnioExpiracion(String anioExpiracion) {
		this.anioExpiracion = anioExpiracion;
	}

	
}