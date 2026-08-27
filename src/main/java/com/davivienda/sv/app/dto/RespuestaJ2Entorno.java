package com.davivienda.sv.app.dto;

import com.davivienda.sv.app.util.GetData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;



@JacksonXmlRootElement(localName = "respuestaEntorno")
public class RespuestaJ2Entorno<T> extends AbstractRespuestaJ2Entorno<HeaderResponse, Body<T>> implements GetData<T> {

	public RespuestaJ2Entorno() {
		this.header = new HeaderResponse();
		this.body = new Body<T>();
	}

	@Override
	public T getData() {
		return this.body.getContenedor().getData();
	}

	public void setData(T data) {
		this.body.getContenedor().setData(data);

	}

	@Override
	public String getCodigo() {
		return this.header.getCodigo();
	}

	@Override
	public void setCodigo(String codigo) {
	this.header.setCodigo(codigo);	

	}

	@Override
	public String getDescripcion() {
		return this.header.getDescripcion();
	}

	@Override
	public void setDescripcion(String descripcion) {
		this.header.setDescripcion(descripcion);

	}

}
