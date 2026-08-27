package com.davivienda.sv.app.data.beans.sesion;

public class LoginRequest {
	private String usuario;
	private String clave;
	private String imei;
	private String sistemaOperativo;
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario.toUpperCase();
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getSistemaOperativo() {
		return sistemaOperativo;
	}
	public void setSistemaOperativo(String sistemaOperativo) {
		this.sistemaOperativo = sistemaOperativo;
	}
	@Override
	public String toString() {
		return "LoginRequest [usuario=" + usuario + ", clave=" + clave + ", imei=" + imei + ", sistemaOperativo="
				+ sistemaOperativo + "]";
	}
}
