package com.davivienda.sv.app.dto;

import java.io.Serializable;
import java.util.LinkedHashMap;

import com.davivienda.sv.app.util.DataDeserializer;
import com.davivienda.sv.app.util.DataSerializer;
import com.davivienda.sv.app.util.ToXML;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;



/**
 * 
 * @author Christian Guillen
 * @since 7 jul 2023
 * @version 1.0
 * 
 * @param <T>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(using = DataSerializer.class)
@JsonDeserialize(using = DataDeserializer.class)
@JacksonXmlRootElement(localName = "contenedor")
public class Contenedor<T> implements Serializable, ToXML {

	private static final long serialVersionUID = 1L;
    
	public T data;
	public LinkedHashMap<String, StackTrace> excepcionJ2E;

	public Contenedor() {
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	public LinkedHashMap<String, StackTrace> getExcepcionJ2E() {
		return excepcionJ2E;
	}

	public void setExcepcionJ2E(LinkedHashMap<String, StackTrace> excepcionJ2E) {
		this.excepcionJ2E = excepcionJ2E;
	}
	
	@Override
	public String toXML() throws JsonProcessingException {
		return ToXML.super.toXML().replace("<>","").replace("</>", "");
	}

	@Override
	public String toString() {
		return "Contenedor [data=" + data + ", excepcionJ2E=" + excepcionJ2E + "]";
	}

}
