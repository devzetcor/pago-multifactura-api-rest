package com.davivienda.sv.app.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public interface ToXML {
	
	default String toXML() throws JsonProcessingException {
		return new XmlMapper().writeValueAsString(this);
	}

}
