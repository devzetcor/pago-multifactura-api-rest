package com.davivienda.sv.app.data.beans.usuario;

public class InfoUsuarioRequest {
	private String usuario;

	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	@Override
	public String toString() {
		return "InfoUsuarioRequest [usuario=" + usuario + "]";
	}
}
