package com.davivienda.sv.app.services.operaciones;

import com.davivienda.sv.app.data.beans.BasicResponse;
import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.Response;
import com.davivienda.sv.app.data.beans.token.GenerarOtpRequest;
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
public class GenerarOtpService extends TransaccionService<GenerarOtpRequest, BasicResponse> {

	@Autowired
	@Qualifier(R.MQCliente.PFS.NAME)
    MQCliente mqcService;
	private static final Logger LOGGER = LogManager.getLogger(GenerarOtpService.class);
	
	@Override
	public Optional<String> construirPeticion(Request<GenerarOtpRequest> request) {
		LOGGER.info("Obteniendo respuesta servicio GenerarOtpService... "+request.getBody().getEsOperador().equals("true")+" " + request.getBody()+" "+ request.getBody().getEsOperador() + " "+request.getBody().getNiuString()+" "+request.getBody().getNiu());
		String xmlPeticion = "<peticionEntorno><header><fabrica>" + R.Fabricas.ESBeBanca
				+ "</fabrica><servicio>GENERAR_OTP_PYME</servicio></header><body><contenedor><peticionGenerarOTP><niu>"
				+ (request.getBody().getEsOperador().equals("true")? request.getBody().getNiuString():request.getBody().getNiu()) + "</niu><esOperador>"+request.getBody().getEsOperador()+"</esOperador></peticionGenerarOTP></contenedor></body></peticionEntorno>";

		return Optional.of(xmlPeticion);
	}

	@Override
	public Response<BasicResponse> evaluarRespuesta(Request<GenerarOtpRequest> request, Document docResp) {
		Response<BasicResponse> resp = new Response<>(request, new BasicResponse());
		LOGGER.info("Obteniendo respuesta servicio GenerarOtpService... ");

		long codResp = Long.parseLong(docResp.selectSingleNode("/respuestaEntorno/header/codigo").getText());
		String descResp = docResp.selectSingleNode("/respuestaEntorno/header/descripcion").getText();

		if (codResp == 7779) {
			return new Response<BasicResponse>(request, new BasicResponse(), 1029, "GENERAR_OTP_PYME");
		}
		if (codResp == 7780) {
			return new Response<BasicResponse>(request, new BasicResponse(), 1031, "GENERAR_OTP_PYME");
		}
		if (codResp != 0) {
			return new Response<BasicResponse>(request, new BasicResponse(), 1003, "GENERAR_OTP_PYME", descResp);
		}

		LOGGER.info("Obteniendo respuesta servicio GenerarOtpService... OK");
		return resp;
	}

	@Override
	public Response<BasicResponse> ejecutar(Request<GenerarOtpRequest> request, String nombreServicio)
			throws Throwable {
		return super.ejecutar(request, nombreServicio, mqcService);
	}

	

}
