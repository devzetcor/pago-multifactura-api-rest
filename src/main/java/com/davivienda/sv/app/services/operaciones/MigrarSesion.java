package com.davivienda.sv.app.services.operaciones;

import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.Response;
import com.davivienda.sv.app.data.beans.sesion.LoginResponse;
import com.davivienda.sv.app.dto.MigrarSesionUsuarioDataRequest;
import com.davivienda.sv.app.util.MQCliente;
import com.davivienda.sv.app.util.R;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Optional;

@Service
public class MigrarSesion extends TransaccionService<MigrarSesionUsuarioDataRequest, LoginResponse> {
	private SimpleDateFormat dFmt = new SimpleDateFormat("yyyyMMddHHmmss");

	private static final Logger LOGGER = LogManager.getLogger(MigrarSesion.class);

	@Autowired
	@Qualifier(R.MQCliente.PFS.NAME)
    MQCliente mqcService;
	

	public Optional<String> construirPeticion(Request<MigrarSesionUsuarioDataRequest> request) {
        try {
        String	xmlPeticion=generarXmlPeticionMigrarSesion(request.getBody());
            return Optional.of(xmlPeticion);
        }
        catch (Exception e) {
            this.LOGGER.error("Exception: Error al desencriptar contraseña... "+ e.getMessage(),e);
            return Optional.empty();
        }
    }

	public Response<LoginResponse> evaluarRespuesta(Request<MigrarSesionUsuarioDataRequest> request, Document docResp) {
		Response<LoginResponse> resp = new Response<LoginResponse>(request, new LoginResponse());
		LOGGER.info("Obteniendo respuesta servicio LoginService..."+ docResp.asXML());

		long codResp = Long.parseLong(docResp.selectSingleNode("/respuestaEntorno/header/codigo").getText());
		String descResp = docResp.selectSingleNode("/respuestaEntorno/header/descripcion").getText();
		String correlativo = docResp
				.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaMigrarSesionUsuario/correlativo") == null
						? "0"
						: docResp
								.selectSingleNode(
										"/respuestaEntorno/body/contenedor/respuestaMigrarSesionUsuario/correlativo")
								.getText();

		if (codResp == -1L) {
			resp = new Response<LoginResponse>(request, new LoginResponse(), 1014);
			resp.getHeader().setIdSesion(correlativo);
			return resp;
		}
		if (codResp == 12L) {
			resp = new Response<LoginResponse>(request, new LoginResponse(), 1015);
			resp.getHeader().setIdSesion(correlativo);
			return resp;
		}
		if (codResp == 8L) {
			resp = new Response<LoginResponse>(request, new LoginResponse(), 1016);
			resp.getHeader().setIdSesion(correlativo);
			return resp;
		}
		if (codResp == 5L) {
			resp = new Response<LoginResponse>(request, new LoginResponse(), 1018);
			resp.getHeader().setIdSesion(correlativo);
			return resp;
		}
		if (codResp == 9994L) {
			resp = new Response<LoginResponse>(request, new LoginResponse(), 1020);
			resp.getHeader().setIdSesion(correlativo);
			return resp;
		}
		if (codResp == 6) {
			resp = new Response<LoginResponse>(request, new LoginResponse(), 1026);
			resp.getHeader().setIdSesion(correlativo);
			return resp;
		}
		if (codResp == -4L) {
			resp = new Response<LoginResponse>(request, new LoginResponse(), 1);
		} else if (codResp != 0L) {
			resp = new Response<LoginResponse>(request, new LoginResponse(), 1003, "INICIAR_SESION_USUARIO", descResp);
			resp.getHeader().setIdSesion(correlativo);
			return resp;
		}
		String niuString=docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaMigrarSesionUsuario/niu").getText();
		if (niuString != null && !niuString.isEmpty()) {
			Long niu = Long.parseLong(niuString);
			resp.getBody().setNiu(niu);
		}else {
			resp.getBody().setNiu(-1);
		}
		String nombre = docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaMigrarSesionUsuario/nombre")
				.getText();
		int tipoAutorizacion = Integer.parseInt(
				docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaMigrarSesionUsuario/tipoAutorizacion")
						.getText());
		String tipoDocumento = docResp
				.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaMigrarSesionUsuario/tipoIdentificacion")
				.getText();
		String numeroDocumento = docResp
				.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaMigrarSesionUsuario/numeroIndentificacion")
				.getText();
		
		String usuario = docResp
				.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaMigrarSesionUsuario/usuario")
				.getText();
		
		resp.getHeader().setIdSesion(correlativo);
		
		resp.getBody().setNombre(nombre);
		resp.getBody().setModoAutenticacion(tipoAutorizacion);
		resp.getBody().setTipoDocumento(tipoDocumento);
		resp.getBody().setNumeroDocumento(numeroDocumento);

		try {
			String fecha = docResp
					.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaMigrarSesionUsuario/ultimoLogin").getText();
			if (fecha == null || fecha.trim().isEmpty()) {
				this.LOGGER.info("No se obtuvo fecha del ultimo login");
				resp.getBody().setUltimoLogin("");
			} else {
				resp.getBody()
						.setUltimoLogin(fecha);
			}
			resp.getBody().setUsuario(usuario);
		} catch (Exception e) {
			this.LOGGER.error("Excepción parseando la fecha " + docResp
					.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaMigrarSesionUsuario/ultimoLogin").getText()
					+ ": " + e.getMessage(), (Throwable) e);
		}
		LOGGER.info("Obteniendo respuesta servicio LoginService...OK");

		return resp;
	}

	public String generarXmlPeticionMigrarSesion(MigrarSesionUsuarioDataRequest dto) {
		StringBuilder xmlBuilder = new StringBuilder();
		xmlBuilder.append("<peticionEntorno><header><fabrica>"+R.Fabricas.ESBeBanca+"</fabrica><servicio>MIGRAR_SESION_USUARIO</servicio></header><body>");
		xmlBuilder.append("<contenedor>");
		xmlBuilder.append("<peticionMigrarSesionUsuario>");
		xmlBuilder.append("<correlativo>");
		if (dto.getCorrelativo() != null) {
			xmlBuilder.append(dto.getCorrelativo());
		}
		xmlBuilder.append("</correlativo>");
		
		xmlBuilder.append("<usuario>");
		if (dto.getUsuario() != null) {
			xmlBuilder.append(dto.getUsuario());
		}
		xmlBuilder.append("</usuario>");
		
		xmlBuilder.append("<canalOrigen>");
			xmlBuilder.append("WEB");
		xmlBuilder.append("</canalOrigen>");
		
		xmlBuilder.append("<canal>");
		if (dto.getCanal() != null) {
			xmlBuilder.append("PMF");
		}
		xmlBuilder.append("</canal>");

		xmlBuilder.append("</peticionMigrarSesionUsuario>");
		xmlBuilder.append("</contenedor>");
		xmlBuilder.append("</body></peticionEntorno>");		
		
		 this.LOGGER.info("PETICION "+xmlBuilder.toString());
		return xmlBuilder.toString();
	}

	// Método para escapar caracteres especiales de XML
	private static String escapeXml(String text) {
		return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'",
				"&apos;");
	}

	@Override
	public Response<LoginResponse> ejecutar(Request<MigrarSesionUsuarioDataRequest> request, String nombreServicio)
			throws Throwable {
		  return super.ejecutar(request, nombreServicio, this.mqcService);
	}
}
