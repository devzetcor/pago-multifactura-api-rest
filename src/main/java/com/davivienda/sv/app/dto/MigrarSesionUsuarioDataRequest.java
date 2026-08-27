package com.davivienda.sv.app.dto;

public class MigrarSesionUsuarioDataRequest {

	private String correlativo;
	private String usuario;
	private String canal;
	private String token;

	public MigrarSesionUsuarioDataRequest() {
		super();
	}

	public String getCorrelativo() {
		return correlativo;
	}

	public void setCorrelativo(String correlativo) {
		this.correlativo = correlativo;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getCanal() {
		return canal;
	}

	public void setCanal(String canal) {
		this.canal = canal;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "MigrarSesionUsuarioDataRequest [correlativo=" + correlativo + ", usuario=" + usuario + ", canal="
				+ canal + ", token=" + token + "]";
	}

}
