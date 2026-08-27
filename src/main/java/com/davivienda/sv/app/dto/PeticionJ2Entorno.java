package com.davivienda.sv.app.dto;


import javax.xml.stream.XMLOutputFactory;

import com.davivienda.sv.app.util.GetData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;


/**
*
* @author Christian Guillén
* @since 2 jul 2023
* @version 1.0
* @param <T>
* 
*/
@JacksonXmlRootElement(localName = "peticionEntorno")
public class PeticionJ2Entorno<T> extends AbstractPeticionJ2Entorno<HeaderRequest, Body<T>> implements GetData<T>{
	
	public PeticionJ2Entorno() {
		this.header = new HeaderRequest();
		this.body = new Body<T>();
    }

	@Override
	public void setData(T data) {
		this.body.getContenedor().setData(data);
	}

	@Override
	public T getData() {
		return this.body.getContenedor().getData();
	}

	@Override
	public String toString() {
		return "PeticionJ2Entorno [header=" + header + ", body=" + body + "]";
	}
	
	@Override
	public String toXML() throws JsonProcessingException {
		String xmlOriginal =null;
		String xmlModificado =null;
		 XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
	        outputFactory.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, false);
	        
	        XmlMapper xmlMapper = XmlMapper.builder()
	            .defaultUseWrapper(false)
	            .configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, false)
	            .configure(SerializationFeature.INDENT_OUTPUT, true)
	            .build();
	        
	        xmlMapper.getFactory().getXMLOutputFactory()
	            .setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, false);
	        
	        // Generar XML inicial
	        xmlOriginal= xmlMapper.writeValueAsString(this);
	        if(xmlOriginal!=null) {
	        	xmlModificado = xmlOriginal.replace(" xmlns=\"\"", "");
	        	return xmlModificado;
	        }
	       return  xmlOriginal;
	}
	
}
