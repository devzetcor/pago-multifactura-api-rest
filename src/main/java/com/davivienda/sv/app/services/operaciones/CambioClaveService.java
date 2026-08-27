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
import com.davivienda.sv.app.data.beans.sesion.CambioClaveRequest;
import com.davivienda.sv.app.services.JEncryptorService;
import com.davivienda.sv.app.util.R;

@Service
public class CambioClaveService extends TransaccionService<CambioClaveRequest, BasicResponse> {
	private static final Logger LOGGER = LogManager.getLogger(CambioClaveService.class);

	@Autowired
	@Qualifier(R.MQCliente.PFS.NAME)
    MQCliente mqcService;
	@Autowired
	JEncryptorService jEncryptoService;
	@Override
	public Optional<String> construirPeticion(Request<CambioClaveRequest> request)  {
		String clave = request.getBody().getClave().trim();
//		LOGGER.info("Obteniendo clave desde front end... "+clave);
		try {
			clave = jEncryptoService.desencriptar(clave);
//			LOGGER.info("Obteniendo clave desencriptada... "+clave);
			String xmlPeticion = "<peticionEntorno><header><fabrica>"+R.Fabricas.ESBeBanca+"</fabrica><servicio>CAMBIAR_CLAVE_ACCESO</servicio></header><body><contenedor><peticionCambioClave><usuario>"
					+ request.getHeader().getUsuario().toUpperCase() + "</usuario><correlativo>1</correlativo><sistema>MIB</sistema><accion>PWD</accion><codTransaccion>PIB9995</codTransaccion><cantidadParametros>15</cantidadParametros>"
					+ "<ip>" + request.getHeader().getIp() + "</ip><imei>"+request.getBody().getImei()+"</imei><sistemaOperativo>"+request.getBody().getSistemaOperativo()+"</sistemaOperativo>"
					+ "<idTransaccion>"+request.getHeader().getIdTransaccion()+"</idTransaccion><idSesion>"+request.getHeader().getIdSesion()+"</idSesion><tag1>"
					+ request.getHeader().getUsuario().toUpperCase() + "</tag1><tag2></tag2><tag3>" + clave + "</tag3><tag4/><tag5/><tag6/><tag7/><tag8/><tag9/><tag10/><tag11/><tag12/><tag13/>"
					+ "<tag14>0</tag14><tag15>Y</tag15></peticionCambioClave></contenedor></body></peticionEntorno>";
			return Optional.of(xmlPeticion);
		} catch (Exception e) {
			LOGGER.error("Excepcion: Error al desencriptar usuario ó contraseña..." + e.getMessage() , e);
			return Optional.empty();
		}
		
	}
	
	public Response<BasicResponse> ejecutar(Request<CambioClaveRequest> request, String nombreServicio) throws Throwable {
		if (!request.getBody().getClave().equals(request.getBody().getRepetirClave())) {
			return new Response<BasicResponse>(request, new BasicResponse(), 1007);
		}
		return super.ejecutar(request, nombreServicio, mqcService);
	}

	@Override
	public Response<BasicResponse> evaluarRespuesta(Request<CambioClaveRequest> request, Document docResp) {
		Response<BasicResponse> resp = new Response<>(request, new BasicResponse());
		LOGGER.info("Obteniendo respuesta servicio CambioClaveService...");

		long codResp = Long.parseLong(docResp.selectSingleNode("/respuestaEntorno/header/codigo").getText());
		String status = docResp.selectSingleNode("respuestaEntorno/body/contenedor/msj6").getText();
		String longitudPassword = docResp.selectSingleNode("respuestaEntorno/body/contenedor/msj15").getText();
		
		if (codResp != 0) {
			return new Response<BasicResponse>(request, new BasicResponse(), 1003, "CAMBIAR_CLAVE_ACCESO");
		}
		if (status.equals("6")) {
			return new Response<BasicResponse>(request, new BasicResponse(), 1008, longitudPassword);
		}
		if (status.equals("7")) {
			return new Response<BasicResponse>(request, new BasicResponse(), 1009);
		}
		if (status.equals("8")) {
			return new Response<BasicResponse>(request, new BasicResponse(), 1010, status);
		}
		if (status.equals("A")) {
			return new Response<BasicResponse>(request, new BasicResponse(), 1011);
		}
		if (status.equals("B")) {
			return new Response<BasicResponse>(request, new BasicResponse(), 1012);
		}
		if (status.equals("C")) {
			return new Response<BasicResponse>(request, new BasicResponse(), 1013);
		}
		LOGGER.info("Obteniendo respuesta servicio CambioClaveService... OK");
		return resp;
	}
	

}
