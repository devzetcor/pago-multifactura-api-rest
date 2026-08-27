package com.davivienda.sv.app.services.operaciones;

import java.util.Optional;

import com.davivienda.sv.app.util.MQCliente;
import org.dom4j.Document;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.Response;
import com.davivienda.sv.app.data.beans.usuario.InfoUsuarioRequest;
import com.davivienda.sv.app.data.beans.usuario.InfoUsuarioResponse;
import com.davivienda.sv.app.util.R;

@Service
public class InfoUsuarioService extends TransaccionService<InfoUsuarioRequest, InfoUsuarioResponse> {
	@Autowired
	@Qualifier(R.MQCliente.PFS.NAME)
    MQCliente mqcService;
	private static final Logger LOGGER = LogManager.getLogger(InfoUsuarioService.class);

	@Override
	public Optional<String> construirPeticion(Request<InfoUsuarioRequest> request) {
		String xmlPeticion = "<peticionEntorno>" + "	<header>" + "		<fabrica>" + R.Fabricas.ESBeBanca
				+ "</fabrica>" + "		<servicio>OBTENER_INFO_USUARIO</servicio>" + "	</header>" + "	<body>"
				+ "		<contenedor>" + "			<peticionObtenerInfo>"  + "				<usuario>"
				+ request.getBody().getUsuario().toUpperCase() + "</usuario>" + "			</peticionObtenerInfo>"
				+ "		</contenedor>" + "	</body>" + "</peticionEntorno>";

		return Optional.of(xmlPeticion);
	}

	@Override
	public Response<InfoUsuarioResponse> evaluarRespuesta(Request<InfoUsuarioRequest> request, Document docResp) {
		Response<InfoUsuarioResponse> resp = new Response<>(request, new InfoUsuarioResponse());

		LOGGER.info("Obteniendo respuesta servicio InfoUsuarioService...");

		long codResp = Long.parseLong(docResp.selectSingleNode("/respuestaEntorno/header/codigo").getText());
		String descResp = docResp.selectSingleNode("/respuestaEntorno/header/descripcion").getText();

		if (codResp != 0) {
			return new Response<InfoUsuarioResponse>(request, new InfoUsuarioResponse(), 1003, "OBTENER_INFO_USUARIO",
					descResp);
		}

		resp.getBody().setNiu(Long.parseLong(
				docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaObtenerInfo/niu").getText()));
		resp.getBody().setCorreo(
				docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaObtenerInfo/correo").getText());
		resp.getBody().setTelefono(
				docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaObtenerInfo/telefono").getText());
		resp.getBody()
				.setModoAutenticacion(Integer.parseInt(docResp
						.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaObtenerInfo/tipoAutorizacion")
						.getText()));
		resp.getBody().setProfesion(
				docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaObtenerInfo/profesion").getText());
		resp.getBody().setEdad(Integer.parseInt(
				docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaObtenerInfo/edad").getText()));
		resp.getBody().setPersonaNatural(
				docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaObtenerInfo/tipoCliente").getText()
						.equals("2") ? true : false);
		resp.getBody().setNIT(
				docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaObtenerInfo/NIT").getText());

		LOGGER.info("Obteniendo respuesta servicio InfoUsuarioService...OK");

		return resp;
	}

	@Override
	public Response<InfoUsuarioResponse> ejecutar(Request<InfoUsuarioRequest> request, String nombreServicio)
			throws Throwable {
		return super.ejecutar(request, nombreServicio, mqcService);
	}
}
