package com.davivienda.sv.app.services;

import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.springframework.beans.factory.annotation.Autowired;

import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.Response;





public abstract class TransaccionService<T, U> {
	
	private final static Logger LOGGER = LogManager.getLogger(TransaccionService.class);
		
	@Autowired
	protected ErrorService errorService;
	
	public abstract Optional<String> construirPeticion(Request<T> request) throws Throwable;
	public abstract Response<U> evaluarRespuesta(Request<T> request, Document docResp) throws Throwable;
	public abstract Response<U> ejecutar(Request<T> request, String nombreServicio)  throws Throwable ;

	
	protected Response<U> ejecutar(Request<T> request, String nombreServicio, MQClientService mqcService)  throws Throwable {
		Response<U> resp = new Response<>(request);
		Document docResp = null;
		
		//1 - Construcción del XML de petición
		Optional<String> xmlPeticion = this.construirPeticion(request);
		LOGGER.info("xml de peticion CONSTRUIDA");
		if (!xmlPeticion.isPresent()) {
			resp.getHeader().setCodigo(1002);
			resp.getHeader().setDescripcion(errorService.getMensajeError(1002, nombreServicio));
			
			LOGGER.debug("XML de respuesta no est� presente");
			return resp;
		}
		
		LOGGER.info("xmlPeticion est� presente: "+ xmlPeticion.get());
		
		//2 - Ejecución de servicio por MQ
		Optional<String> xmlRespuesta = mqcService.execute(nombreServicio + ".REQ", nombreServicio + ".RESP", xmlPeticion.get());
		if (!xmlRespuesta.isPresent()) {
			resp.getHeader().setCodigo(1001);
			resp.getHeader().setDescripcion(errorService.getMensajeError(1001));
			return resp;
		}
		//AGREGADA
		
		
		LOGGER.debug("\n XML Respuesta del servicio: " + nombreServicio + " \nRespuesta: \n" + xmlRespuesta.get() + "\nCodigo de respuesta: " + resp.getHeader());
		
		//3 - Evaluación de la respuesta
		try {
			docResp = DocumentHelper.parseText(xmlRespuesta.get());
		} catch (DocumentException e) {
			LOGGER.error("Excepción parseando XML de respuesta de servicio " + nombreServicio + ": " + e.getMessage(), e);
			resp.getHeader().setCodigo(1002);
			resp.getHeader().setDescripcion(errorService.getMensajeError(1003, nombreServicio));
			return resp;
		}
		
		return evaluarRespuesta(request, docResp);
	}
}