package com.davivienda.sv.app.process;

import com.davivienda.sv.app.data.beans.mq.InvocadorServiciosMQ;
import com.davivienda.sv.app.dto.PeticionJ2Entorno;
import com.davivienda.sv.app.dto.colecturia.pagar.PeticionPagoFactura;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcesarPagoFactura {

	public static final String FABRICA_ESB = "fabricaESB";
	public static final String SERVICIO = "COLECTURIA";
	public static final String OPERACION_PAGO_FACTURA = "PAGO_FACTURA";

	// Logger estático y haciendo referencia a la clase directa
	private static final Logger LOGGER = LogManager.getLogger(ProcesarPagoFactura.class);

	@Autowired
	private InvocadorServiciosMQ invocadorMQ;

	// Nota: Se mantiene el nombre del método 'procesarConsultaColectores'
	// para respetar la firma original, aunque funcionalmente realiza un PAGO.
	public String procesarConsultaColectores(PeticionPagoFactura dto) throws JsonProcessingException {
		String xmlRequest = definicionXMLPeticionPagoFacturaMQ(dto);
		String xmlResponse = null;

		// Validación para evitar concatenación de strings costosa si no es necesario
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Inicio ProcesarPagoFactura. XML Peticion: " + xmlRequest);
		}

		try {
			xmlResponse = invocadorMQ.invocarServicio(SERVICIO, OPERACION_PAGO_FACTURA, xmlRequest);

			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Respuesta ProcesarPagoFactura MQ: " + xmlResponse);
			}

		} catch (Throwable e) {
			// Se corrige el mensaje de log (antes decía "consultar" siendo un pago)
			// y se usa LOGGER.error en lugar de Priority
			LOGGER.error("Error tratando de procesar pago factura: " + e.getMessage(), e);
		}

		return xmlResponse;
	}

	public String definicionXMLPeticionPagoFacturaMQ(PeticionPagoFactura dto) throws JsonProcessingException {
		// Construcción del Wrapper/Entorno
		PeticionJ2Entorno<PeticionPagoFactura> peticionJ2Entorno = new PeticionJ2Entorno<>();

		peticionJ2Entorno.getHeader().setFabrica(FABRICA_ESB);
		peticionJ2Entorno.getHeader().setServicio(OPERACION_PAGO_FACTURA);
		// Asignación directa del DTO, eliminando variable intermedia redundante
		peticionJ2Entorno.setData(dto);

		return peticionJ2Entorno.toXML();
	}
}