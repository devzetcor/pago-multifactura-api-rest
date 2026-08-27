package com.davivienda.sv.app.services.operaciones;

import java.util.Optional;

import com.davivienda.sv.app.util.MQCliente;
import org.dom4j.Document;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.davivienda.sv.app.data.beans.BasicRequest;
import com.davivienda.sv.app.data.beans.BasicResponse;
import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.Response;
import com.davivienda.sv.app.util.R;

@Service
public class LogoutService extends TransaccionService<BasicRequest, BasicResponse> {
	@Autowired
	@Qualifier(R.MQCliente.PFS.NAME)
	private MQCliente mqcService;

	private static final Logger LOGGER = LogManager.getLogger(LogoutService.class);
	@Autowired
	RegistraWRINTASService reWrintasService;

	@Override
	public Optional<String> construirPeticion(Request<BasicRequest> request) {
		String xmlPeticion = "<peticionEntorno><header><fabrica>" + R.Fabricas.ESBeBanca
				+ "</fabrica><servicio>CERRAR_SESION_USUARIO</servicio></header><body><contenedor><peticionCerrarSesion><usuario><nombre>"
				+ request.getHeader().getUsuario().toUpperCase() + "</nombre><correlativo>"
				+ request.getHeader().getIdSesion() + "</correlativo><ip>" + request.getHeader().getIp()
				+ "</ip></usuario></peticionCerrarSesion></contenedor></body></peticionEntorno>";

		return Optional.of(xmlPeticion);
	}

	@Override
	public Response<BasicResponse> evaluarRespuesta(Request<BasicRequest> request, Document docResp) {
		Response<BasicResponse> resp = new Response<>(request, new BasicResponse());
		LOGGER.info("Obteniendo respuesta servicio LogoutService...");

		long codResp = Long.parseLong(docResp.selectSingleNode("/respuestaEntorno/header/codigo").getText());
		String descResp = docResp.selectSingleNode("/respuestaEntorno/header/descripcion").getText();

		if (codResp != 0) {
			return new Response<BasicResponse>(request, new BasicResponse(), 0, "CERRAR_SESION_USUARIO", descResp);
		}
		LOGGER.info("Obteniendo respuesta servicio LogoutService...OK");

		return resp;
	}

	@Override
	public Response<BasicResponse> ejecutar(Request<BasicRequest> request, String nombreServicio) throws Throwable {
		return super.ejecutar(request, nombreServicio, mqcService);
	}
}
