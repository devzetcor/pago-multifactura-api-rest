package com.davivienda.sv.app.dto.colecturia;

import com.davivienda.sv.app.dto.AbstractPeticionJ2Entorno;
import com.davivienda.sv.app.util.GetData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;



@JacksonXmlRootElement(localName = "env:Envelope")
public class ColecturiaRequest<T> extends AbstractPeticionJ2Entorno<HeaderColecturiaRequest, BodyColecturiaRequest<T>>
		implements GetData<T> {

	@JacksonXmlProperty(localName = "xmlns:env", isAttribute = true)
	private final String env = "http://www.w3.org/2003/05/soap-envelope";

	public ColecturiaRequest() {
		this.header = new HeaderColecturiaRequest();
		this.body = new BodyColecturiaRequest<T>();
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
		return this.getBody().getPeticionEntorno().getData();
	}

	@Override
	public void setData(T data) {
		this.getBody().getPeticionEntorno().setData(data);

	}

	@Override
	@JacksonXmlProperty(localName = "env:Body")
	public BodyColecturiaRequest<T> getBody() {
		return super.getBody();
	}

}