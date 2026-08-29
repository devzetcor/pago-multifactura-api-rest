package com.davivienda.sv.app.services.operaciones;

import com.davivienda.sv.app.TacticoDepositosReferenciadosApplication;
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
public class ValidarSoftTokenService extends TransaccionService<ValidarOtpRequest, BasicResponse> {


	@Autowired
	@Qualifier(R.MQCliente.PFS.NAME)
	MQCliente mqcService;
	private static final Logger LOGGER = LogManager.getLogger(ValidarSoftTokenService.class);

	@Override
	public Optional<String> construirPeticion(Request<ValidarOtpRequest> request) {
		String xmlPeticion = "<peticionEntorno><header><fabrica>"+R.Fabricas.ESBeBanca+"</fabrica><servicio>AUTENTICAR_TRANSACCION</servicio></header><body><contenedor>"
				+ "<peticionAutenticarTransaccion><canal>" + TacticoDepositosReferenciadosApplication.CANAL + "</canal><usuario>" + request.getHeader().getUsuario().toUpperCase() + "</usuario><niu>"
				+ request.getBody().getNiu() + "</niu><token>" + request.getBody().getOtp() + "</token><tipoAutenticacion>1</tipoAutenticacion><requiereToken>1</requiereToken></peticionAutenticarTransaccion></contenedor></body></peticionEntorno>";
				
		return Optional.of(xmlPeticion);
	}

	@Override
	public Response<BasicResponse> evaluarRespuesta(Request<ValidarOtpRequest> request, Document docResp) {
		Response<BasicResponse> resp = new Response<>(request, new BasicResponse());
		LOGGER.info("Obteniendo respuesta servicio ValidarSoftTokenService...");

		long codResp = Long.parseLong(docResp.selectSingleNode("/respuestaEntorno/header/codigo").getText());
		
		if (codResp == 0) {
			LOGGER.info("Obteniendo respuesta servicio ValidarSoftTokenService... OK");
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
