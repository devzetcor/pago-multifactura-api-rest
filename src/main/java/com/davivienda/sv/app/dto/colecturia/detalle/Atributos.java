package com.davivienda.sv.app.dto.colecturia.detalle;

import java.util.List;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Atributos {
	@JacksonXmlElementWrapper(useWrapping = !true)
	@JacksonXmlProperty(localName = "atributoColector")
	private List<AtributoColectorFull> atributoColector;

	public Atributos() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Atributos(List<AtributoColectorFull> atributoColector) {
		super();
		this.atributoColector = atributoColector;
	}

	public List<AtributoColectorFull> getAtributoColector() {
		return atributoColector;
	}

	public void setAtributoColector(List<AtributoColectorFull> atributoColector) {
		this.atributoColector = atributoColector;
	}



	
	
}
