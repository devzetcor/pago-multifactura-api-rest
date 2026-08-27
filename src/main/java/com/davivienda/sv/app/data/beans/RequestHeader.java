package com.davivienda.sv.app.data.beans;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.davivienda.sv.app.TacticoDepositosReferenciadosApplication;
import com.fasterxml.jackson.annotation.JsonFormat;

public class RequestHeader {
	private String idTransaccion;
	private String idSesion;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern=TacticoDepositosReferenciadosApplication.DATETIME_DEFAULT_FORMAT, timezone="GMT-6")
	private Date fechaHora;
	private String usuario;
	private String canal;
	private String ip;
	private String dispositivo;
	private Float niu;
	
	private DateFormat fmt = new SimpleDateFormat(TacticoDepositosReferenciadosApplication.DATETIME_DEFAULT_FORMAT);
	
	public RequestHeader() {
		this.fechaHora = new Date();
	}
	
	public String getIdTransaccion() {
		return idTransaccion;
	}
	public void setIdTransaccion(String idTransaccion) {
		this.idTransaccion = idTransaccion;
	}
	public String getIdSesion() {
		return idSesion;
	}
	public void setIdSesion(String idSesion) {
		this.idSesion = idSesion;
	}
	public Date getFechaHora() {
		return fechaHora;
	}
	public void setFechaHora(Date fechaHora) {
		this.fechaHora = fechaHora;
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
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getDispositivo() {
		return dispositivo;
	}
	public void setDispositivo(String dispositivo) {
		this.dispositivo = dispositivo;
	}
	public Float getNiu(){
		return this.niu;
	}
	public void setNiu(Float niu){
		this.niu = niu;
	}
	@Override
	public String toString() {
		return "RequestHeader [idTransaccion=" + idTransaccion + ", idSesion=" + idSesion + ", fechaHora=" + fmt.format(fechaHora)
				+ ", usuario=" + usuario + ", canal=" + canal
				+ ", ip=" + ip + ", dispositivo=" + dispositivo + "]";
	}
}
