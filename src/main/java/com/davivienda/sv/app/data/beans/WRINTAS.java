package com.davivienda.sv.app.data.beans;

import java.math.BigDecimal;

public class WRINTAS {
	private String usuario;
	private String cuentaOrigen;
	private String codAccion;
	private String actividad;
	private BigDecimal monto;
	private String ip;
	private long niu;
	private String direccionMAC;
	private String sistemaOperativo;
	private String idTransaccion;
	private String idSesion;
	private int codigoRespuesta;
	private String cuentaDestino;
	private long montoDavipuntos;
	
	public WRINTAS() {
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getCuentaOrigen() {
		return cuentaOrigen;
	}

	public void setCuentaOrigen(String cuentaOrigen) {
		this.cuentaOrigen = cuentaOrigen;
	}

	public String getCodAccion() {
		return codAccion;
	}

	public void setCodAccion(String codAccion) {
		this.codAccion = codAccion;
	}

	public String getActividad() {
		return actividad;
	}

	public void setActividad(String actividad) {
		this.actividad = actividad;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public long getNiu() {
		return niu;
	}

	public void setNiu(long niu) {
		this.niu = niu;
	}

	public String getDireccionMAC() {
		return direccionMAC;
	}

	public void setDireccionMAC(String direccionMAC) {
		this.direccionMAC = direccionMAC;
	}

	public String getSistemaOperativo() {
		return sistemaOperativo;
	}

	public void setSistemaOperativo(String sistemaOperativo) {
		this.sistemaOperativo = sistemaOperativo;
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

	public int getCodigoRespuesta() {
		return codigoRespuesta;
	}

	public void setCodigoRespuesta(int codigoRespuesta) {
		this.codigoRespuesta = codigoRespuesta;
	}

	public String getCuentaDestino() {
		return cuentaDestino;
	}

	public void setCuentaDestino(String cuentaDestino) {
		this.cuentaDestino = cuentaDestino;
	}

	public long getMontoDavipuntos() {
		return montoDavipuntos;
	}

	public void setMontoDavipuntos(long montoDavipuntos) {
		this.montoDavipuntos = montoDavipuntos;
	}

	@Override
	public String toString() {
		return String.format(
				"WRINTAS [usuario=%s, cuentaOrigen=%s, codAccion=%s, actividad=%s, monto=%s, ip=%s, niu=%s, direccionMAC=%s, sistemaOperativo=%s, idTransaccion=%s, idSesion=%s, codigoRespuesta=%s, cuentaDestino=%s, montoDavipuntos=%s]",
				usuario, cuentaOrigen, codAccion, actividad, monto, ip, niu, direccionMAC, sistemaOperativo,
				idTransaccion, idSesion, codigoRespuesta, cuentaDestino, montoDavipuntos);
	}	
}
