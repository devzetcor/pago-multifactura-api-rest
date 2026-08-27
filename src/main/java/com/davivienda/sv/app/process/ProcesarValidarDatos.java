package com.davivienda.sv.app.process;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.davivienda.sv.app.data.beans.mq.InvocadorServiciosMQ;
import com.davivienda.sv.app.dto.PeticionJ2Entorno;
import com.davivienda.sv.app.dto.colecturia.validar.ValidarDatosPago;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class ProcesarValidarDatos {

    public static final String FABRICA_ESB = "fabricaESB";
    public static final String SERVICIO = "COLECTURIA";
    public static final String OPERACION_VALIDAR_DATOS_PAGO = "VALIDAR_DATOS_PAGO";

    // Logger estático y final (Mejor práctica de rendimiento)
    private static final Logger LOGGER = LogManager.getLogger(ProcesarValidarDatos.class);

    @Autowired
    private InvocadorServiciosMQ invocadorMQ;

    // Se mantiene el nombre del método "procesarProcesar..." para no romper la firma original
    public String procesarProcesarValidarDatos(ValidarDatosPago dto) throws JsonProcessingException {
        String xmlRequest = definicionXMLPeticionValidarDatosMQ(dto);
        String xmlResponse = null;

        // Uso de isInfoEnabled para evitar concatenación de Strings costosa si no es necesario
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Inicio ValidarDatos. XML Peticion: " + xmlRequest);
        }

        try {
            xmlResponse = invocadorMQ.invocarServicio(SERVICIO, OPERACION_VALIDAR_DATOS_PAGO, xmlRequest);
            
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Respuesta ValidarDatos MQ: " + xmlResponse);
            }

        } catch (Exception e) {
            // Corrección crítica: 
            // 1. Se usa LOGGER.error en vez de info.
            // 2. Se arregla el texto "Excepcin" (problema de encoding).
            // 3. Se corrige "tratando de consultar" por "validar".
            LOGGER.error("Error tratando de validar datos: " + e.getMessage(), e);
        }
        
        return xmlResponse;
    }

    public String definicionXMLPeticionValidarDatosMQ(ValidarDatosPago dto) throws JsonProcessingException {
        PeticionJ2Entorno<ValidarDatosPago> peticionJ2Entorno = new PeticionJ2Entorno<>();
        
        peticionJ2Entorno.getHeader().setFabrica(FABRICA_ESB);
        peticionJ2Entorno.getHeader().setServicio(OPERACION_VALIDAR_DATOS_PAGO);
        peticionJ2Entorno.setData(dto);
        
        return peticionJ2Entorno.toXML();
    }
}