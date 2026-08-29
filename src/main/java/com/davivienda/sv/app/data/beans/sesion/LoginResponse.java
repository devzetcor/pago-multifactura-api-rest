package com.davivienda.sv.app.data.beans.sesion;

import com.davivienda.sv.app.TacticoDepositosReferenciadosApplication;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class LoginResponse {
	private long niu;
	private String nombre;
	private String tipoDocumento;
	private String numeroDocumento;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern=TacticoDepositosReferenciadosApplication.DATETIME_DEFAULT_FORMAT, timezone="GMT-6")
	private String ultimoLogin;
	private int modoAutenticacion;
	private String usuario;
	
	private DateFormat fmt = new SimpleDateFormat(TacticoDepositosReferenciadosApplication.DATETIME_DEFAULT_FORMAT);
	
	public long getNiu() {
		return niu;
	}
	public void setNiu(long niu) {
		this.niu = niu;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public String getUltimoLogin() {
		return ultimoLogin;
	}
	public void setUltimoLogin(String ultimoLogin) {
		this.ultimoLogin = ultimoLogin;
	}
	public int getModoAutenticacion() {
		return modoAutenticacion;
	}
	public void setModoAutenticacion(int modoAutenticacion) {
		this.modoAutenticacion = modoAutenticacion;
	}
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	@Override
	public String toString() {
		return "LoginResponse [niu=" + niu + ", nombre=" + nombre + ", tipoDocumento=" + tipoDocumento
				+ ", numeroDocumento=" + numeroDocumento + ", ultimoLogin=" + ultimoLogin + ", modoAutenticacion="
				+ modoAutenticacion + ", usuario=" + usuario + ", fmt=" + fmt + "]";
	}
	
	
}
