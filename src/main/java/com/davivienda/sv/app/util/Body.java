package com.davivienda.sv.app.util;

import com.davivienda.sv.app.dto.Contenedor;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;



/**
 *
 * @author Christian Guillén
 * @since 2 jul 2023
 * @version 1.0
 * @param <T>
 * 
 */
@JacksonXmlRootElement(localName = "body")
public class Body<T> implements ToXML {
	
	public Contenedor<T> contenedor = new Contenedor<T>();
	
	public Body() {
	}
	
	public Body(Contenedor<T> contenedor) {
		super();
		this.contenedor = contenedor;
	}

	public Contenedor<T> getContenedor() {
		return contenedor;
	}

	public void setContenedor(Contenedor<T> contenedor) {
		this.contenedor = contenedor;
	}

	@Override
	public String toString() {
		return "Body [contenedor=" + contenedor + "]";
	}

}
