package com.davivienda.sv.app.util;

import com.davivienda.sv.app.dto.Contenedor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

import java.io.IOException;

/**
 * 
 * @author Christian Guillen
 * @since 22 may 2024
 * @version 1.0
 *
 */
public class DataSerializer extends JsonSerializer<com.davivienda.sv.app.dto.Contenedor<?>> {

	@Override
	public void serialize(Contenedor<?> contenedor, JsonGenerator jgen, SerializerProvider arg2) throws IOException {
		ToXmlGenerator xgen = (ToXmlGenerator) jgen;
		// valida si el objeto contenedor posee datos
		if (contenedor.getData() != null) {
			// Valida si es un subcontenedor
			Class<?> classData = contenedor.getData().getClass();
			// Subcontenedor
			if (classData.isAnnotationPresent(SubContenedor.class)) {
				String classNameData = classData.getSimpleName();
				classNameData = classNameData.substring(0, 1).toLowerCase() + classNameData.substring(1);
				xgen.writeStartObject();
				xgen.writeObjectField(classNameData, contenedor.getData());
				xgen.writeEndObject();
			}// Contenedor
			else {
				xgen.writeStartObject();
				xgen.writeObjectField("", contenedor.getData());
				xgen.writeEndObject();
			}
		}
	}

}
