package com.davivienda.sv.app.data.beans;

public class StatusRequest {
	private String usuario;

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return "StatusRequest [usuario=" + usuario + "]";
	}

}
