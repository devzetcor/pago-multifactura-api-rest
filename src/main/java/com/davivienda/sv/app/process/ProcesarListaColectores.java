package com.davivienda.sv.app.process;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.davivienda.sv.app.data.beans.mq.InvocadorServiciosMQ;
import com.davivienda.sv.app.dto.PeticionJ2Entorno;
import com.davivienda.sv.app.dto.PeticionListaColectoresDto;
import com.davivienda.sv.app.dto.colecturia.lista.PeticionConsultaColectores;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class ProcesarListaColectores {

    // Constantes de operación
    public static final String FABRICA_ESB = "fabricaESB";
    public static final String SERVICIO = "COLECTURIA";
    public static final String OPERACION_CONSULTA_COLECTORES = "CONSULTA_COLECTORES";
    public static final String OPERACION_LISTA_COLECTORES = "LISTA_COLECTORES";

    // Logger estático y final (Mejor rendimiento)
    private static final Logger LOGGER = LogManager.getLogger(ProcesarListaColectores.class);

    @Autowired
    private InvocadorServiciosMQ invocadorMQ;

    public String procesarConsultaColectores(PeticionListaColectoresDto dto) throws JsonProcessingException {
        String xmlRequest = definicionXMLPeticionConsultarColectoresMQ(dto);
        String xmlResponse = null;

        // Logging eficiente
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Inicio ProcesarListaColectores. XML Peticion: " + xmlRequest);
        }

        try {
            // Nota: Se mantiene el uso de OPERACION_CONSULTA_COLECTORES para la invocación
            // tal como estaba en el código original, aunque el XML usa LISTA_COLECTORES.
            xmlResponse = invocadorMQ.invocarServicio(SERVICIO, OPERACION_CONSULTA_COLECTORES, xmlRequest);
            
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Respuesta ProcesarListaColectores MQ: " + xmlResponse);
            }

        } catch (Throwable e) {
            // Corrección de encoding y uso correcto de LOGGER.error
            LOGGER.error("Error tratando de consultar lista colectores: " + e.getMessage(), e);
        }
        
        return xmlResponse;
    }

    public String definicionXMLPeticionConsultarColectoresMQ(PeticionListaColectoresDto dto) throws JsonProcessingException {
        // Construcción de datos internos
        PeticionConsultaColectores peticionData = new PeticionConsultaColectores();
        peticionData.setCodigoCanal(dto.getCodigoCanal());
        peticionData.setFlagSinNPE(dto.getFlagSinNPE());
        // Se mantienen los valores vacíos explícitos por si el esquema XML los requiere
        peticionData.setNpe("");
        peticionData.setBarra("");
        peticionData.setIdcolector("");

        // Construcción del Wrapper/Entorno
        PeticionJ2Entorno<PeticionConsultaColectores> peticionJ2Entorno = new PeticionJ2Entorno<>();
        peticionJ2Entorno.getHeader().setFabrica(FABRICA_ESB);
        peticionJ2Entorno.getHeader().setServicio(OPERACION_LISTA_COLECTORES);
        peticionJ2Entorno.setData(peticionData);

        return peticionJ2Entorno.toXML();
    }
}