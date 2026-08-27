package com.davivienda.sv.app.dto;

import java.util.List;


public class ListaCuenta {
	private List<Cuenta> cuentas;

	public ListaCuenta() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ListaCuenta(List<Cuenta> cuentas) {
		this.cuentas = cuentas;
	}

	public List<Cuenta> getCuentas() {
		return cuentas;
	}

	public void setCuentas(List<Cuenta> cuentas) {
		this.cuentas = cuentas;
	}
}