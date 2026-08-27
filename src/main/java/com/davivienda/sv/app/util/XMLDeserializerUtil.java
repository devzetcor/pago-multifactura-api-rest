package com.davivienda.sv.app.util;

import java.io.IOException;

import com.davivienda.sv.app.dto.AbstractRespuestaJ2Entorno;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;



/**
 * 
 * @author Christian Guillen
 * @since 25 ago 2023
 * @version 1.0
 *
 */
public final class XMLDeserializerUtil {
	
	private static void configure(XmlMapper mapper) {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
	}

	public static <R extends AbstractRespuestaJ2Entorno<?, ?>> R parseXML(String xml, Class<R> trespuesta, Class<?> tdata) throws JsonMappingException, JsonProcessingException  {
		XmlMapper mapper = new XmlMapper();
		configure(mapper);
		return mapper.readValue(xml, mapper.getTypeFactory().constructParametricType(trespuesta, tdata));
	}
	
	public static <R> R parseXML(JsonParser parse, JavaType type) throws IOException {
		XmlMapper mapper = (XmlMapper) parse.getCodec();
		configure(mapper);
		return mapper.readValue(parse, type);
	}
	
	public static <R> R parseXML(JsonParser parse, TypeReference<R> type) throws IOException {
		XmlMapper mapper = (XmlMapper) parse.getCodec();
		configure(mapper);
		return mapper.readValue(parse, type);
	}
	
}
