package com.davivienda.sv.app.data.beans;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.davivienda.sv.app.TacticoDepositosReferenciadosApplication;
import com.davivienda.sv.app.services.ErrorService;
import com.fasterxml.jackson.annotation.JsonFormat;

public class ResponseHeader {
	private String idTransaccion;
	private String idSesion;
	private int codigo;
	private String descripcion;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern=TacticoDepositosReferenciadosApplication.DATETIME_DEFAULT_FORMAT, timezone="GMT-6")
	private Date fechaHora;
	
	private DateFormat fmt = new SimpleDateFormat(TacticoDepositosReferenciadosApplication.DATETIME_DEFAULT_FORMAT);
	
	private ErrorService errorSrv;

	public ResponseHeader() {
		this.errorSrv = new ErrorService();
		this.fechaHora = new Date();
		this.codigo = 0;
		this.descripcion = "OK";
	}
	
	public ResponseHeader(RequestHeader header) {
		this();
		this.setIdSesion(header.getIdSesion());
		this.setIdTransaccion(header.getIdTransaccion());
	}
	
	public ResponseHeader(RequestHeader header, int codigoError) {
		this(header);
		this.setCodigo(codigoError);
		this.setDescripcion(errorSrv.getMensajeError(codigoError));
	}
	public ResponseHeader(RequestHeader header, int codigoError,String descripcion) {
		this(header);
		this.setCodigo(codigoError);
		this.setDescripcion(descripcion);
	}
	
	public ResponseHeader(RequestHeader header, int codigoError, Object... paramsError) {
		this(header);
		this.setCodigo(codigoError);
		this.setDescripcion(errorSrv.getMensajeError(codigoError, paramsError));
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
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Date getFechaHora() {
		return fechaHora;
	}
	public void setFechaHora(Date fechaHora) {
		this.fechaHora = fechaHora;
	}
	@Override
	public String toString() {
		return "ResponseHeader [idTransaccion=" + idTransaccion + ", idSesion=" + idSesion + ", codigo=" + codigo
				+ ", descripcion=" + descripcion + ", fechaHora=" + fmt.format(fechaHora) + "]";
	}
}
