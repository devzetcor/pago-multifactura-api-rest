package com.davivienda.sv.app.data.beans.sesion;

public class CambioClaveRequest {
	private String usuario;
	private String clave;
	private String repetirClave;
	private String imei;
	private String sistemaOperativo;
	private int modoOperacion;
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getRepetirClave() {
		return repetirClave;
	}
	public void setRepetirClave(String repetirClave) {
		this.repetirClave = repetirClave;
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
	public int getModoOperacion() {
		return modoOperacion;
	}
	public void setModoOperacion(int modoOperacion) {
		this.modoOperacion = modoOperacion;
	}
	@Override
	public String toString() {
		return "CambioClaveRequest [usuario=" + usuario + ", clave=" + clave + ", repetirClave=" + repetirClave
				+ ", imei=" + imei + ", sistemaOperativo=" + sistemaOperativo + ", modoOperacion=" + modoOperacion
				+ "]";
	}
}
