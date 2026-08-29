package com.davivienda.sv.app.services.operaciones;

import com.davivienda.sv.app.data.beans.BasicResponse;
import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.Response;
import com.davivienda.sv.app.data.beans.token.ValidarOtpRequest;
import com.davivienda.sv.app.util.MQCliente;
import com.davivienda.sv.app.util.R;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ValidarHardTokenService extends TransaccionService<ValidarOtpRequest, BasicResponse> {


	@Autowired
	@Qualifier(R.MQCliente.PFS.NAME)
	MQCliente mqcService;
	private static final Logger LOGGER = LogManager.getLogger(ValidarHardTokenService.class);

	@Override
	public Optional<String> construirPeticion(Request<ValidarOtpRequest> request) {
		String xmlPeticion = "<peticionEntorno><header><fabrica>"+R.Fabricas.ESBeBanca+"</fabrica><servicio>VALIDA_TOKEN_USUARIO</servicio></header><body><contenedor><peticionValidaToken><canal>MIB</canal><usuario>"
				+ request.getHeader().getUsuario().toUpperCase() + "</usuario><numToken>" + request.getBody().getOtp() + "</numToken></peticionValidaToken></contenedor></body></peticionEntorno>";
				
		return Optional.of(xmlPeticion);
	}

	@Override
	public Response<BasicResponse> evaluarRespuesta(Request<ValidarOtpRequest> request, Document docResp) {
		Response<BasicResponse> resp = new Response<>(request, new BasicResponse());
		LOGGER.info("Obteniendo respuesta servicio ValidarHardTokenService...");

		long codResp = Long.parseLong(docResp.selectSingleNode("/respuestaEntorno/header/codigo").getText());
		
		if (codResp == 0 && "true".equals(docResp.selectSingleNode("//valorValidacion").getText().trim())) {
			LOGGER.info("Obteniendo respuesta servicio ValidarHardTokenService... OK");

			return resp;
		} else {
			return new Response<BasicResponse>(request, new BasicResponse(), 1101);
		}
	}

	@Override
	public Response<BasicResponse> ejecutar(Request<ValidarOtpRequest> request, String nombreServicio)  throws Throwable{
		return super.ejecutar(request, nombreServicio, mqcService);
	}

}
