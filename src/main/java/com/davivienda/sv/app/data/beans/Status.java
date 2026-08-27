package com.davivienda.sv.app.data.beans;

public class Status {
	private String canal;
	private String statusIngreso;
	private String correlativo;
	private String usuario;
	private String ultimoLogin;
	public String getCanal() {
		return canal;
	}
	public void setCanal(String canal) {
		this.canal = canal;
	}
	public String getStatusIngreso() {
		return statusIngreso;
	}
	public void setStatusIngreso(String statusIngreso) {
		this.statusIngreso = statusIngreso;
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
	public String getUltimoLogin() {
		return ultimoLogin;
	}
	public void setUltimoLogin(String ultimoLogin) {
		this.ultimoLogin = ultimoLogin;
	}
	
	public boolean isActive() {
		return  this.statusIngreso.equals("CN");
	}
	@Override
	public String toString() {
		return "Status [canal=" + canal + ", statusIngreso=" + statusIngreso + ", correlativo=" + correlativo
				+ ", usuario=" + usuario + ", ultimoLogin=" + ultimoLogin + "]";
	}
	
	

}
