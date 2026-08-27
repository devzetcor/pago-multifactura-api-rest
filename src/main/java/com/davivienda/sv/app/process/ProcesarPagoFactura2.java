package com.davivienda.sv.app.process;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Service;

import com.davivienda.sv.app.data.beans.mq.InvocadorServiciosMQ;
import com.davivienda.sv.app.dto.PeticionJ2Entorno;
import com.davivienda.sv.app.dto.colecturia.validar.ValidarDatosPago;
import com.fasterxml.jackson.core.JsonProcessingException;
@Service
public class ProcesarPagoFactura2 {
	public static final String FABRICA_ESB = "fabricaESB";
	public static final String SERVICIO = "COLECTURIA";
	public static final String OPERACION_VALIDAR_DATOS_PAGO = "VALIDAR_DATOS_PAGO";
	private static final Logger LOGGER = LogManager.getLogger(ProcesarPagoFactura2.class);

	public String procesarProcesarValidarDatos(ValidarDatosPago dto, InvocadorServiciosMQ invocadorMQ) throws JsonProcessingException {
		String xmlPetConsultaCS = definicionXMLPeticionConsultarColectoresMQ(dto);
		String xmlRespConsulCS = null;
		LOGGER.info("xmlPetConsultaCS:" + xmlPetConsultaCS);

		try {
			xmlRespConsulCS = invocadorMQ.invocarServicio(SERVICIO, OPERACION_VALIDAR_DATOS_PAGO, xmlPetConsultaCS);
			LOGGER.info("xmlRespConsulCS:" + xmlRespConsulCS);

		} catch (Exception e) {
			LOGGER.error("Excepci�n tratando de consultar : " + e.getMessage(),e);
		}
		return xmlRespConsulCS;
	}

	public String definicionXMLPeticionConsultarColectoresMQ(ValidarDatosPago dto) throws JsonProcessingException  {
		PeticionJ2Entorno<ValidarDatosPago> peticionJ2Entorno = new PeticionJ2Entorno<>();
		ValidarDatosPago validarDatosPago=dto;
		peticionJ2Entorno.getHeader().setFabrica(FABRICA_ESB);
		peticionJ2Entorno.getHeader().setServicio(OPERACION_VALIDAR_DATOS_PAGO);
		peticionJ2Entorno.setData(validarDatosPago);
		return peticionJ2Entorno.toXML();
		
	}

}
