package com.davivienda.sv.app.services.cysce;

import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.Response;
import com.davivienda.sv.app.dto.RespuestaJ2Entorno;
import com.davivienda.sv.app.dto.colecturia.detalle.PeticionConsultaColectorDto;
import com.davivienda.sv.app.dto.colecturia.detalle.RespuestaConsultaColector;
import com.davivienda.sv.app.process.ProcesarConsultaColectores;
import com.davivienda.sv.app.services.ErrorService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringReader;

@Service
public class ConsultaColectoresServiceCysceImpl {

    private static final Logger LOGGER = LogManager.getLogger(ConsultaColectoresServiceCysceImpl.class);
    private static final String NOMBRE_SERVICIO = "ConsultaColectoresServiceCysceImpl";

    // Optimización: XmlMapper es costoso de instanciar, se hace estático y final.
    private static final XmlMapper XML_MAPPER;

    static {
        XML_MAPPER = new XmlMapper();
        // Configuración recomendada para evitar errores si llegan campos extra en el XML
        XML_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private final ErrorService errorService;
    private final ProcesarConsultaColectores pcMQcon;

    // Inyección por constructor para asegurar inmutabilidad y testabilidad
    @Autowired
    public ConsultaColectoresServiceCysceImpl(ErrorService errorService, ProcesarConsultaColectores pcMQcon) {
        this.errorService = errorService;
        this.pcMQcon = pcMQcon;
    }

    public Response<RespuestaConsultaColector> process(Request<PeticionConsultaColectorDto> request) {
        LOGGER.info("###### " + NOMBRE_SERVICIO + " INICIO");

        String resultadoDatos;

        // 1. Llamado al servicio MQ
        try {
            resultadoDatos = pcMQcon.procesarConsultaColectores(request.getBody());
            
            if (resultadoDatos == null || resultadoDatos.trim().isEmpty()) {
                String descriptionLog = errorService.getMensajeError(1004, NOMBRE_SERVICIO);
                LOGGER.error(NOMBRE_SERVICIO + ": Respuesta nula o vacía del servicio MQ.");
                return new Response<>(request, 1003, NOMBRE_SERVICIO.concat(": ").concat(descriptionLog));
            }
            
        } catch (Throwable e) {
            LOGGER.error("Error al procesar JSON de entrada: " + e.getMessage(), e);
            String descriptionLog = errorService.getMensajeError(1003, NOMBRE_SERVICIO);
            return new Response<>(request, 1003, NOMBRE_SERVICIO.concat(": ").concat(descriptionLog));
        }

        LOGGER.info("###################################################");
        LOGGER.info("###### " + NOMBRE_SERVICIO + " resultadoDatos: " + resultadoDatos);
        LOGGER.info("###################################################");

        // 2. Validación de Header XML con Dom4j
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new StringReader(resultadoDatos));
            
            if (document.getRootElement() == null) {
                throw new DocumentException("El documento XML no tiene raíz.");
            }

            Element headerElement = document.getRootElement().element("header");
            if (headerElement != null) {
                String codigoStr = headerElement.elementText("codigo");
                String descripcion = headerElement.elementText("descripcion");
                
                LOGGER.info("###### " + NOMBRE_SERVICIO + " Header: " + codigoStr + "-" + descripcion);

                int codigo = (codigoStr != null && !codigoStr.isEmpty()) ? Integer.parseInt(codigoStr) : -1;
                
                if (codigo != 0) {
                    return new Response<>(request, codigo, descripcion);
                }
            } else {
                LOGGER.warn("Respuesta XML sin elemento <header>");
            }

        } catch (Throwable e) {
            LOGGER.error("Error parseando estructura XML o código de respuesta", e);
            // Se retorna error genérico si falla la lectura básica del XML
            return new Response<>(request, 1003, NOMBRE_SERVICIO + ": Error estructura XML");
        }

        // 3. Deserialización completa con Jackson
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
            LOGGER.error("Error mapeando XML a objetos: " + e.getMessage(), e);
            return new Response<>(request, 1003, NOMBRE_SERVICIO);
        }
    }
}