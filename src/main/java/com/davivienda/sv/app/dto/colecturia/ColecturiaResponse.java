package com.davivienda.sv.app.dto.colecturia;

import com.davivienda.sv.app.dto.AbstractRespuestaJ2Entorno;
import com.davivienda.sv.app.util.GetData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;



@JacksonXmlRootElement(localName = "env:Envelope")
public class ColecturiaResponse<T>
		extends AbstractRespuestaJ2Entorno<HeaderColecturiaRequest, BodyColecturiaResponse<T>> implements GetData<T> {

	@JacksonXmlProperty(localName = "xmlns:env", isAttribute = true)
	private final String env = "http://www.w3.org/2003/05/soap-envelope";

	public ColecturiaResponse() {
		this.header = new HeaderColecturiaRequest();
		this.body = new BodyColecturiaResponse<T>();
	}

	@Override
	@JacksonXmlProperty(localName = "env:Header")
	public HeaderColecturiaRequest getHeader() {
		return super.getHeader();
	}

	@Override
	public String toString() {
		return "PeticionJ2Entorno [header=" + header + ", body=" + body + "]";
	}

	@Override
	public T getData() {
		return this.getBody().getRespuestaJ2Entorno().getData();
	}

	@Override
	public void setData(T data) {
		this.getBody().getRespuestaJ2Entorno().setData(data);

	}

	@Override
	@JacksonXmlProperty(localName = "env:Body")
	public BodyColecturiaResponse<T> getBody() {
		return super.getBody();
	}

	@Override
	public String getCodigo() {
		return this.body.getRespuestaJ2Entorno().getCodigo();
	}

	@Override
	public void setCodigo(String codigo) {
		this.body.getRespuestaJ2Entorno().setCodigo(codigo);

	}

	@Override
	public String getDescripcion() {
		return this.getBody().getRespuestaJ2Entorno().getDescripcion();
	}

	@Override
	public void setDescripcion(String descripcion) {
		this.body.getRespuestaJ2Entorno().setDescripcion(descripcion);
	}

}