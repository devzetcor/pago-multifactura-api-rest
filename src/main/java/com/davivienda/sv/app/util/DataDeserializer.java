package com.davivienda.sv.app.util;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.davivienda.sv.app.dto.Contenedor;
import com.davivienda.sv.app.dto.StackTrace;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * 
 * @author Christian Guillen
 * @since 17 jul 2023
 * @version 1.0
 *
 */
public class DataDeserializer extends StdDeserializer<Contenedor<?>> implements ContextualDeserializer {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(DataDeserializer.class);

	private JavaType type;

	public DataDeserializer() {
		this(null);
	}

	public DataDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
		type = property.getType().containedType(0);
		return this;
	}

	@Override
	public Contenedor<?> deserialize(JsonParser jp, DeserializationContext context)throws IOException, JsonProcessingException {
		Contenedor<Object> contenedor = new Contenedor<>();
		jp.nextToken();
		if(jp.getCurrentName().equals("excepcionJ2E")) {
			jp.nextToken();
			contenedor.setExcepcionJ2E(XMLDeserializerUtil.parseXML(jp,new TypeReference<LinkedHashMap<String, StackTrace>>() {}));
		}else if(type.getRawClass().isAnnotationPresent(SubContenedor.class)) {
			jp.nextToken();
			contenedor.setData(XMLDeserializerUtil.parseXML(jp,type));
		}
		else {
			contenedor.setData(XMLDeserializerUtil.parseXML(jp,type));
		}
		
		//Si no retorno la data entonces se instancia un objeto vacio
		if(contenedor.getData() == null)
		 {
			try {
				contenedor.setData(type.getRawClass().getConstructors()[0].newInstance(new Object[] { }));
			} catch (Throwable e) {
				LOGGER.error("Error al crear instancia Data: "+e.getMessage(),e);
			}
		}
		
		return contenedor;
	}

}
