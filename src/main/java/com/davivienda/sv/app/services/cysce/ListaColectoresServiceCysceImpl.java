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
import com.davivienda.sv.app.dto.PeticionListaColectoresDto;
import com.davivienda.sv.app.dto.RespuestaJ2Entorno;
import com.davivienda.sv.app.dto.colecturia.lista.RespuestaConsultaColector;
import com.davivienda.sv.app.process.ProcesarListaColectores;
import com.davivienda.sv.app.services.ErrorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Service
public class ListaColectoresServiceCysceImpl {

    private static final Logger LOGGER = LogManager.getLogger(ListaColectoresServiceCysceImpl.class);
    private static final String NOMBRE_SERVICIO = "ListaColectoresServiceCysceImpl";
    
    // Optimización: Instancia única y thread-safe de XmlMapper para evitar overhead en cada petición
    private static final XmlMapper XML_MAPPER;
    
    static {
        XML_MAPPER = new XmlMapper();
        // Configuración opcional recomendada: ignorar propiedades desconocidas para evitar errores si el XML cambia levemente
        XML_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private final ErrorService errorService;
    private final ProcesarListaColectores pcMQcon;

    // Inyección por Constructor (Mejor práctica que @Autowired en atributos)
    @Autowired
    public ListaColectoresServiceCysceImpl(ErrorService errorService, ProcesarListaColectores pcMQcon) {
        this.errorService = errorService;
        this.pcMQcon = pcMQcon;
    }

    public Response<RespuestaConsultaColector> process(Request<PeticionListaColectoresDto> request) {
        LOGGER.info("###### " + NOMBRE_SERVICIO + " INICIO");

        String resultadoDatos;

        // 1. Invocar servicio MQ
        try {
            resultadoDatos = pcMQcon.procesarConsultaColectores(request.getBody());
            
            if (resultadoDatos == null || resultadoDatos.trim().isEmpty()) {
                String descriptionLog = errorService.getMensajeError(1004, NOMBRE_SERVICIO);
                LOGGER.error("Respuesta nula del servicio MQ: " + descriptionLog);
                return new Response<>(request, 1003, NOMBRE_SERVICIO.concat(": ").concat(descriptionLog));
            }
            
        } catch (Throwable e) {
            LOGGER.error("Error procesando JSON para MQ", e);
            String descriptionLog = errorService.getMensajeError(1003, NOMBRE_SERVICIO);
            return new Response<>(request, 1003, NOMBRE_SERVICIO.concat(": ").concat(descriptionLog));
        }

        LOGGER.info("###################################################");
        LOGGER.info("###### " + NOMBRE_SERVICIO + " resultadoDatos: " + resultadoDatos);
        LOGGER.info("###################################################");

        // 2. Parseo inicial con Dom4j para validar cabecera (codigo y descripcion)
        try {
            SAXReader reader = new SAXReader();
            // Se usa try-with-resources implícito o bloques controlados. StringReader no requiere cierre estricto pero es buen hábito limpiar.
            Document document = reader.read(new StringReader(resultadoDatos));
            
            if (document.getRootElement() == null) {
                throw new DocumentException("El documento XML no tiene raíz.");
            }

            Element headerElement = document.getRootElement().element("header");
            if (headerElement != null) {
                String codigoStr = headerElement.elementText("codigo");
                String descripcion = headerElement.elementText("descripcion");
                
                LOGGER.info("###### " + NOMBRE_SERVICIO + " Header: " + codigoStr + "-" + descripcion);

                int codigo = (codigoStr != null) ? Integer.parseInt(codigoStr) : -1;
                
                // Si el código no es exitoso (0), retornamos error inmediatamente
                if (codigo != 0) {
                    return new Response<>(request, codigo, descripcion);
                }
            } else {
                LOGGER.warn("No se encontró elemento <header> en la respuesta XML.");
            }

        } catch (Throwable e) {
            LOGGER.error("Error al parsear la estructura XML de respuesta o el código de error", e);
            return new Response<>(request, 1003, NOMBRE_SERVICIO + ": Error estructura XML");
        }

        // 3. Mapeo de Objeto con Jackson (XmlMapper) si la respuesta fue exitosa
        try {
            RespuestaJ2Entorno<RespuestaConsultaColector> respuestaEntorno = XML_MAPPER.readValue(
                resultadoDatos, 
                XML_MAPPER.getTypeFactory().constructParametricType(RespuestaJ2Entorno.class, RespuestaConsultaColector.class)
            );
            
            Response<RespuestaConsultaColector> respuesta = new Response<>(request, new RespuestaConsultaColector());
            if (respuestaEntorno != null) {
                respuesta.setBody(respuestaEntorno.getData());
            }
            return respuesta;

        } catch (Throwable e) {
            LOGGER.error("Error de mapeo Jackson en " + NOMBRE_SERVICIO, e);
            return new Response<>(request, 1003, NOMBRE_SERVICIO + ": Error Mapeo Datos");
        }
    }
}