package com.davivienda.sv.app.services.operaciones;

import java.util.Optional;

import com.davivienda.sv.app.util.MQCliente;
import org.dom4j.Document;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.davivienda.sv.app.data.beans.BasicResponse;
import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.Response;
import com.davivienda.sv.app.data.beans.token.ValidarOtpRequest;
import com.davivienda.sv.app.util.R;

@Service
public class ValidarOtpService extends TransaccionService<ValidarOtpRequest, BasicResponse> {


	@Autowired
	@Qualifier(R.MQCliente.PFS.NAME)
	MQCliente mqcService;
	private static final Logger LOGGER = LogManager.getLogger(ValidarOtpService.class);
	
	@Override
	public Optional<String> construirPeticion(Request<ValidarOtpRequest> request) {
		String xmlPeticion = "<peticionEntorno><header><fabrica>" + R.Fabricas.ESBeBanca
				+ "</fabrica><servicio>VALIDACION_OTP_SMS_DID</servicio></header><body><contenedor><peticionValidacionOTPDID>"
				+ "<niu>"
				+ (request.getBody().getIsOperador().equals("true") ? request.getBody().getNiuString()
						: request.getBody().getNiu())
				+ "</niu>"
				+ "<otp>" + request.getBody().getOtp()+ "</otp>"
						+ "<canal>MIB</canal>"
						+"<esOperador>"+request.getBody().getIsOperador()+"</esOperador>"
						+ "</peticionValidacionOTPDID></contenedor></body></peticionEntorno>";

		return Optional.of(xmlPeticion);
	}

	@Override
	public Response<BasicResponse> evaluarRespuesta(Request<ValidarOtpRequest> request, Document docResp) {
		Response<BasicResponse> resp = new Response<>(request, new BasicResponse());
		LOGGER.info("Obteniendo respuesta servicio ValidarOtpService...");
		
		long codResp = Long.parseLong(docResp.selectSingleNode("/respuestaEntorno/header/codigo").getText());
		String descResp = docResp.selectSingleNode("/respuestaEntorno/header/descripcion").getText();
		
		if (codResp == 902 || codResp == 903 || codResp == 803) {
			return new Response<BasicResponse>(request, new BasicResponse(), 1101);
		} else if (codResp != 0) {
			return new Response<BasicResponse>(request, new BasicResponse(), 1003, "VALIDACION_OTP_SMS_DID", descResp);
		}
		
		LOGGER.info("Obteniendo respuesta servicio ValidarOtpService... OK");
		return resp;
	}

	@Override
	public Response<BasicResponse> ejecutar(Request<ValidarOtpRequest> request, String nombreServicio) throws Throwable {
		return super.ejecutar(request, nombreServicio, mqcService);
	}

}
