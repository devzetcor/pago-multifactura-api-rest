package com.davivienda.sv.app.services.cysce;

import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.Response;
import com.davivienda.sv.app.dto.RespuestaJ2Entorno;
import com.davivienda.sv.app.dto.colecturia.validar.RespuestaValidarDatosPago;
import com.davivienda.sv.app.process.ProcesarValidarDatos;
import com.davivienda.sv.app.services.ErrorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Service
public class ValidarDatosPagoServiceCysceImpl {

    private static final Logger LOGGER = LogManager.getLogger(ValidarDatosPagoServiceCysceImpl.class);
    private static final String NOMBRE_SERVICIO = "ValidarDatosPagoServiceCysceImpl";

    // Optimización: Instancia única y estática para evitar overhead en cada petición
    private static final XmlMapper XML_MAPPER;

    static {
        XML_MAPPER = new XmlMapper();
        // Previene errores si el XML trae campos nuevos no mapeados en el DTO
        XML_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private final ErrorService errorService;
    private final ProcesarValidarDatos pcMQcon;

    // Inyección por Constructor (Mejora de arquitectura)
    @Autowired
    public ValidarDatosPagoServiceCysceImpl(ErrorService errorService, ProcesarValidarDatos pcMQcon) {
        this.errorService = errorService;
        this.pcMQcon = pcMQcon;
    }

    public Response<RespuestaValidarDatosPago> process(Request<com.davivienda.sv.app.dto.colecturia.validar.ValidarDatosPago> request) {
        LOGGER.info("###### " + NOMBRE_SERVICIO + ": request:"+request.toString());

        String resultadoDatos;

        // 1. Llamada al servicio MQ
        try {
            resultadoDatos = pcMQcon.procesarProcesarValidarDatos(request.getBody());
            
            if (resultadoDatos == null || resultadoDatos.trim().isEmpty()) {
                String descriptionLog = errorService.getMensajeError(1004, NOMBRE_SERVICIO);
                LOGGER.error(NOMBRE_SERVICIO + ": Respuesta MQ nula o vacía.");
                return new Response<>(request, 1003, NOMBRE_SERVICIO.concat(": ").concat(descriptionLog));
            }
        } catch (JsonProcessingException e) {
            LOGGER.error("Error procesando JSON request: " + e.getMessage(), e);
            String descriptionLog = errorService.getMensajeError(1003, NOMBRE_SERVICIO);
            return new Response<>(request, 1003, NOMBRE_SERVICIO.concat(": ").concat(descriptionLog));
        }

        LOGGER.info("###################################################");
        LOGGER.info("###### " + NOMBRE_SERVICIO + " resultadoDatos: " + resultadoDatos);
        LOGGER.info("###################################################");

        // 2. Parseo y Validación del Header XML
        int codigoInt;
        String descripcion;

        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new StringReader(resultadoDatos));
            
            if (document.getRootElement() == null) {
                throw new DocumentException("El documento XML no tiene raíz.");
            }

            Element headerElement = document.getRootElement().element("header");
            if (headerElement == null) {
                throw new DocumentException("El documento XML no tiene elemento header.");
            }

            String codigoStr = headerElement.elementText("codigo");
            descripcion = headerElement.elementText("descripcion");
            
            LOGGER.info("###### " + NOMBRE_SERVICIO + " Header: " + codigoStr + "-" + descripcion);

            codigoInt = (codigoStr != null && !codigoStr.isEmpty()) ? Integer.parseInt(codigoStr) : -1;

        } catch (Throwable e) {
            LOGGER.error("Error parseando estructura XML o código de respuesta", e);
            // Retornamos una respuesta de error controlada en lugar de dejar que ocurra un NullPointerException
            return new Response<>(request, 1003, NOMBRE_SERVICIO + ": Error estructura XML");
        }

        // 3. Validación de Negocio (Lógica Original: Codigo != 0 y != 3020)
        if (codigoInt != 0 && codigoInt != 3020) {
            // Se respeta estrictamente el tipo de retorno genérico explícito
            return new Response<RespuestaValidarDatosPago>(request, codigoInt, descripcion);
        }

        // 4. Deserialización del Body (Jackson)
        try {
            RespuestaJ2Entorno<RespuestaValidarDatosPago> respuestaEntorno = XML_MAPPER.readValue(
                resultadoDatos, 
                XML_MAPPER.getTypeFactory().constructParametricType(RespuestaJ2Entorno.class, RespuestaValidarDatosPago.class)
            );
            
            Response<RespuestaValidarDatosPago> respuesta = new Response<>(request, new RespuestaValidarDatosPago());
            if (respuestaEntorno != null) {
                respuesta.setBody(respuestaEntorno.getData());
            }
            return respuesta;

        } catch (Throwable e) {
            LOGGER.error("Error de mapeo Jackson en " + NOMBRE_SERVICIO, e);
            return new Response<>(request, 1003, NOMBRE_SERVICIO);
        }
    }
}