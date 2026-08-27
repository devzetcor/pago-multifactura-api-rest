package com.davivienda.sv.app.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.davivienda.sv.app.dto.ContenedorDTO;
import com.davivienda.sv.app.dto.ObjetoSesionUsuarioDataResponse;
import com.davivienda.sv.app.util.J2EntornoInvocacion;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsbc.sv.desarrollo.contenedores.Peticion;
import com.hsbc.sv.desarrollo.contenedores.Respuesta;

@Service
public class ConsultaSesionService {

	private static final Logger LOGGER = LogManager.getLogger(ConsultaSesionService.class);
	@Autowired
	J2EntornoInvocacion j2EntornoInvocacion;

	public String ejecutar(ContenedorDTO contenedorDTO) {
		try {

			try {
				Peticion p = new Peticion();
				p.setDatosXML(generarXml(contenedorDTO));
				LOGGER.debug("-> Request: " + generarXml(contenedorDTO));
				Respuesta respuesta = j2EntornoInvocacion.obtenerDatos("FabricaWebServicesCanales", "srvGestionCache", p);

				LOGGER.debug("-> Result: " + respuesta.simpleXML());
				LOGGER.debug("-> Result: " + respuesta.getDatosXML());
				return getResponse(respuesta);

			} catch (Exception e) {
				LOGGER.debug("Deberia salirse ");
				LOGGER.debug("Deberia salirse " + e.getMessage());
				LOGGER.error("Deberia salirse " + e.getMessage(),e);
			}

		} catch (Exception e) {
			LOGGER.debug("Deberia salirse 2");
			LOGGER.debug("Error en consumo de servicio fabrica " + e.getMessage());
			LOGGER.info("Error en consumo de servicio fabrica " + e.getMessage());
			LOGGER.error("Error estableciendo conexiOn realizada a entorno...", e);

		}
		return null;
	}

	public String generarXml(ContenedorDTO contenedor) {
		StringBuilder xmlBuilder = new StringBuilder();

		xmlBuilder.append("<contenedor>\n");

		xmlBuilder.append("\t<ip>").append(contenedor.getIp() != null ? contenedor.getIp() : "").append("</ip>\n");
		xmlBuilder.append("\t<idTransaccion>")
				.append(contenedor.getIdTransaccion() != null ? contenedor.getIdTransaccion() : "")
				.append("</idTransaccion>\n");
		xmlBuilder.append("\t<idSesion>").append(contenedor.getIdSesion() != null ? contenedor.getIdSesion() : "")
				.append("</idSesion>\n");
		xmlBuilder.append("\t<token>").append(contenedor.getToken() != null ? contenedor.getToken() : "")
				.append("</token>\n");
		xmlBuilder.append("\t<sufijo>").append("_Micrositio")
		.append("</sufijo>\n");

		xmlBuilder.append("</contenedor>");

		return xmlBuilder.toString();
	}

	private String getResponse(Respuesta respuesta) throws Exception {
		return obtenerObjetoSesioUsuario(respuesta.getDatosXML()).getNumeroIdentificacionOperador().replaceAll("-", "");
	}

	private ObjetoSesionUsuarioDataResponse obtenerObjetoSesioUsuario(String json) throws Exception {
		ObjetoSesionUsuarioDataResponse objetoSesionUsuario = new ObjetoSesionUsuarioDataResponse();

		try {
//			json = json.replace("'", "\"");
		Document documento=	DocumentHelper.parseText(json);
		String json2=documento.selectSingleNode("/respuestaEntorno/body/contenedor/json").getText();
		json2 = json2.replace("'", "\"");
			objetoSesionUsuario = new ObjectMapper().readValue(json2, ObjetoSesionUsuarioDataResponse.class);
		} catch (Exception e) {
			LOGGER.error("Error eObjetoSesionUsuarioDataResponse...", e);
			throw new Exception();
		}

		return objetoSesionUsuario;
	}

}
