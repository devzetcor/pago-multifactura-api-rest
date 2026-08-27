package com.davivienda.sv.app.dto.colecturia.lista;

import java.util.List;
import com.davivienda.sv.app.util.SubContenedor;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@SubContenedor
public class RespuestaListaColector {
	
	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "colector")
	List<Colector> colector;

	public RespuestaListaColector() {
		super();
	}

	public RespuestaListaColector(List<Colector> colector) {
		super();
		this.colector = colector;
	}

	public List<Colector> getColector() {
		return colector;
	}

	public void setColector(List<Colector> colector) {
		this.colector = colector;
	}

	

}
