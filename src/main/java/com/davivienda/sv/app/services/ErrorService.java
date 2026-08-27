package com.davivienda.sv.app.services;

import java.text.MessageFormat;
import java.util.Properties;

import org.springframework.stereotype.Component;

import com.davivienda.sv.app.util.R;
import com.davivienda.sv.app.util.ResourceLocator;

@Component
public class ErrorService {
	
	public static final String ERROR_DESCONOCIDO = "ERROR DESCONOCIDO";

	public String getMensajeError(long codigoError) {
		Properties props = ResourceLocator.loadPropertiesFromPath(R.Configuracion.ERROR_PROPERTIES);
		return props.getProperty(String.valueOf(codigoError), ERROR_DESCONOCIDO);
	}
	
	public String getMensajeError(long codigoError, Object... parametrosError) {
		String mensajeError = this.getMensajeError(codigoError);
		return MessageFormat.format(mensajeError, parametrosError);
	}
	
}
