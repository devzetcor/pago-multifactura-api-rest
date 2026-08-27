package com.davivienda.sv.app.process;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.davivienda.sv.app.data.beans.mq.InvocadorServiciosMQ;
import com.davivienda.sv.app.dto.PeticionJ2Entorno;
import com.davivienda.sv.app.dto.colecturia.detalle.PeticionConsultaColectorDto;
import com.davivienda.sv.app.dto.colecturia.lista.PeticionConsultaColectores;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class ProcesarConsultaColectores {

    // Constantes agrupadas y limpias
    public static final String FABRICA_ESB = "fabricaESB";
    public static final String SERVICIO = "COLECTURIA";
    public static final String OPERACION_CONSULTA_COLECTORES = "CONSULTA_COLECTORES";

    // Logger estático y final (buena práctica para evitar instancias innecesarias)
    private static final Logger LOGGER = LogManager.getLogger(ProcesarConsultaColectores.class);

    @Autowired
    private InvocadorServiciosMQ invocadorMQ;

    public String procesarConsultaColectores(PeticionConsultaColectorDto dto) throws JsonProcessingException {
        // 1. Generar XML de petición
        String xmlRequest = definicionXMLPeticionConsultarColectoresMQ(dto);
        String xmlResponse = null;

        // Logging defensivo para evitar sobrecarga si INFO está desactivado
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Inicio proceso consulta colectores. XML Peticion: " + xmlRequest);
        }

        try {
            // 2. Invocación del servicio MQ
            xmlResponse = invocadorMQ.invocarServicio(SERVICIO, OPERACION_CONSULTA_COLECTORES, xmlRequest);
            
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Respuesta recibida MQ: " + xmlResponse);
            }

        } catch (Throwable e) {
            // 3. Manejo de error optimizado
            LOGGER.error("Error al consultar colectores en MQ: " + e.getMessage(), e);
            // No modificamos el retorno null para mantener el comportamiento original
        }

        return xmlResponse;
    }

    public String definicionXMLPeticionConsultarColectoresMQ(PeticionConsultaColectorDto dto) throws JsonProcessingException {
        // Construcción del payload
        PeticionConsultaColectores peticionData = new PeticionConsultaColectores();
        peticionData.setFlagSinNPE("N");
        peticionData.setNpe("N");
        peticionData.setIdcolector(dto.getIdcolector());

        // Construcción del Wrapper/Entorno
        PeticionJ2Entorno<PeticionConsultaColectores> peticionJ2Entorno = new PeticionJ2Entorno<>();
        peticionJ2Entorno.getHeader().setFabrica(FABRICA_ESB);
        peticionJ2Entorno.getHeader().setServicio(OPERACION_CONSULTA_COLECTORES);
        peticionJ2Entorno.setData(peticionData);

        return peticionJ2Entorno.toXML();
    }
}