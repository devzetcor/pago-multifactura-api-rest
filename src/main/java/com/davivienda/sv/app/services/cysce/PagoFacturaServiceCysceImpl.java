package com.davivienda.sv.app.services.cysce;

import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.Response;
import com.davivienda.sv.app.data.beans.ResponseHeader;
import com.davivienda.sv.app.dto.RespuestaJ2Entorno;
import com.davivienda.sv.app.dto.colecturia.pagar.PeticionPagoFactura;
import com.davivienda.sv.app.dto.colecturia.pagar.RespuestaPagoFactura;
import com.davivienda.sv.app.process.ProcesarPagoFactura;
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
public class PagoFacturaServiceCysceImpl {

    private static final Logger LOGGER = LogManager.getLogger(PagoFacturaServiceCysceImpl.class);
    private static final String NOMBRE_SERVICIO = "PagoFacturaServiceCysceImpl";

    // Optimización: Reutilizar instancia de XmlMapper
    private static final XmlMapper XML_MAPPER;

    static {
        XML_MAPPER = new XmlMapper();
        XML_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private final ErrorService errorService;
    private final ProcesarPagoFactura pcMQcon;

    // Inyección por Constructor
    @Autowired
    public PagoFacturaServiceCysceImpl(ErrorService errorService, ProcesarPagoFactura pcMQcon) {
        this.errorService = errorService;
        this.pcMQcon = pcMQcon;
    }

    public Response<RespuestaPagoFactura> process(Request<PeticionPagoFactura> request) {
        LOGGER.info("###### " + NOMBRE_SERVICIO + " request:"+request);
        
        Response<RespuestaPagoFactura> respuesta = new Response<>(request, new RespuestaPagoFactura());
        String resultadoDatos;

        // 1. Llamada al proceso MQ
        try {
            // Nota: Se mantiene la llamada a 'procesarConsultaColectores' según el código original, 
            // aunque la variable sea 'pcMQcon' (ProcesarPagoFactura).
            resultadoDatos = pcMQcon.procesarConsultaColectores(request.getBody());
            
            if (resultadoDatos == null || resultadoDatos.trim().isEmpty()) {
                String descriptionLog = errorService.getMensajeError(1004, NOMBRE_SERVICIO);
                LOGGER.error(NOMBRE_SERVICIO + ": Respuesta vacía del servicio.");
                return new Response<>(request, 1003, NOMBRE_SERVICIO.concat(": ").concat(descriptionLog));
            }
        } catch (Throwable e) {
            LOGGER.error("Error procesando JSON de entrada", e);
            String descriptionLog = errorService.getMensajeError(1003, NOMBRE_SERVICIO);
            return new Response<>(request, 1003, NOMBRE_SERVICIO.concat(": ").concat(descriptionLog));
        }

        LOGGER.info("###################################################");
        LOGGER.info("###### " + NOMBRE_SERVICIO + " resultadoDatos: " + resultadoDatos);
        LOGGER.info("###################################################");

        // 2. Parseo del Header XML (Dom4j)
        int codigoInt;
        String descripcion;

        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new StringReader(resultadoDatos));
            
            if (document.getRootElement() == null) {
                throw new DocumentException("Documento XML sin raíz");
            }

            Element headerElement = document.getRootElement().element("header");
            if (headerElement == null) {
                throw new DocumentException("Documento XML sin elemento header");
            }

            String codigo = headerElement.elementText("codigo");
            descripcion = headerElement.elementText("descripcion");
            
            LOGGER.info("###### " + NOMBRE_SERVICIO + " Header: " + codigo + "-" + descripcion);

            codigoInt = (codigo != null && !codigo.isEmpty()) ? Integer.parseInt(codigo) : -1;

        } catch (Throwable e) {
            LOGGER.error("Error al interpretar estructura XML o código de respuesta", e);
            return new Response<>(request, 1003, NOMBRE_SERVICIO + ": Error estructura XML");
        }

        // 3. Deserialización del cuerpo (Jackson)
        // Se unifica la lógica: se intenta deserializar independientemente del código de error,
        // ya que el código original lo hacía en ambos casos.
        try {
            RespuestaJ2Entorno<RespuestaPagoFactura> respuestaEntorno = XML_MAPPER.readValue(
                resultadoDatos, 
                XML_MAPPER.getTypeFactory().constructParametricType(RespuestaJ2Entorno.class, RespuestaPagoFactura.class)
            );
            
            if (respuestaEntorno != null) {
                respuesta.setBody(respuestaEntorno.getData());
            }

            // Si el código no es 0 (éxito), actualizamos el header de la respuesta con el error del XML
            if (codigoInt != 0) {
                ResponseHeader responseHeader = respuesta.getHeader();
                responseHeader.setCodigo(codigoInt);
                responseHeader.setDescripcion(descripcion);
                respuesta.setHeader(responseHeader);
            }
            
            return respuesta;

        } catch (Throwable e) {
            LOGGER.error("Error de mapeo Jackson en " + NOMBRE_SERVICIO, e);
            return new Response<>(request, 1003, NOMBRE_SERVICIO);
        }
    }
}