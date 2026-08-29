package com.davivienda.sv.app.dto.colecturia.validar;

import com.davivienda.sv.app.dto.colecturia.detalle.AtributoColectorFull;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.ToString;

import java.util.List;

@ToString
public class RespuestaInfoColector {
	@JacksonXmlProperty(localName = "idColector")
	private String idColector;

	@JacksonXmlElementWrapper(localName = "atributos")
	@JacksonXmlProperty(localName = "atributoColector")
	private List<AtributoColectorFull> atributos;

	public RespuestaInfoColector() {
	}

	public String getIdColector() {
		return idColector;
	}

	public void setIdColector(String idColector) {
		this.idColector = idColector;
	}

		public List<AtributoColectorFull> getAtributos() {
			return atributos;
		}
	
		public void setAtributos(List<AtributoColectorFull> list) {
			this.atributos = list;
		}
}