package com.davivienda.sv.app.services.operaciones;

import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.Response;
import com.davivienda.sv.app.data.beans.Status;
import com.davivienda.sv.app.data.beans.StatusRequest;
import com.davivienda.sv.app.util.MQCliente;
import com.davivienda.sv.app.util.R;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class StatusService extends TransaccionService<StatusRequest, Status> {

	@Autowired
	@Qualifier(R.MQCliente.PFS.NAME)
	MQCliente mqcService;
	private static final Logger LOGGER = LogManager.getLogger(RegistraWRINTASService.class);

	@Override
	public Optional<String> construirPeticion(Request<StatusRequest> request) {
		return construirPeticion(request.getHeader().getUsuario());
		
	}
	
	public Optional<String> construirPeticion(String username){
		String xmlPeticion = "<peticionEntorno><header><fabrica>"+R.Fabricas.ESBeBanca+"</fabrica><servicio>ESTATUS_SESION_USUARIO</servicio></header><body><contenedor><peticionEstatusSesion><usuario><nombre>"
				+ username.toUpperCase() + "</nombre></usuario></peticionEstatusSesion></contenedor></body></peticionEntorno>";
		return Optional.of(xmlPeticion);
	}

	@Override
	public Response<Status> evaluarRespuesta(Request<StatusRequest> request, Document docResp) {
		Response<Status> resp = new Response<>(request, new Status());
		long codResp = Long.parseLong(docResp.selectSingleNode("/respuestaEntorno/header/codigo").getText());
		String descResp = docResp.selectSingleNode("/respuestaEntorno/header/descripcion").getText();
        String status = docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaValidarSesion/ESTATUS_INGRESO") != null? docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaValidarSesion/ESTATUS_INGRESO").getText(): "DS";
        LOGGER.info("ESTATUS_SESION_USUARIO docResp.asXML()  "+R.Fabricas.ESBeBanca+": "+ docResp.asXML());
		LOGGER.info("CORRELATIVO: "+ request.getHeader().getIdSesion());
		LOGGER.info("STATUS: "+status+"\n CODIGO: "+codResp);
		if (codResp != 0) {
			return new Response<Status>(request, new Status(), 1003, "ESTATUS_SESION_USUARIO", descResp);
		} else {
			if (status.equals("CN")) {
				
					resp.getBody().setCanal(docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaValidarSesion/CANAL").getText());
					resp.getBody().setCorrelativo(docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaValidarSesion/CORRELATIVO").getText());
					resp.getBody().setStatusIngreso(docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaValidarSesion/ESTATUS_INGRESO").getText());
					resp.getBody().setUsuario(docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaValidarSesion/USUARIO").getText());
					resp.getBody().setUltimoLogin(docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaValidarSesion/FECHA_ULTIMO_INGRESO").getText());
				return resp;
				
			} else {
				return new Response<Status>(request, new Status(), 1017);
			}
		}
	
	}

	@Override
	public Response<Status> ejecutar(Request<StatusRequest> request, String nombreServicio)  throws Throwable{
		return super.ejecutar(request, nombreServicio, mqcService);
	}
	
	public Status ejecutar(String username) {	
		//1 - Construcción del XML de petición
		LOGGER.info("GENERANDO PETICION STATUS...");
		Optional<String> xmlPeticion = this.construirPeticion(username);
		if (!xmlPeticion.isPresent()) {
			LOGGER.info("GENERANDO PETICION STATUS... NO SE PUDO CONSTRUIR PETICION");
			LOGGER.info("RESULTADO XML PETICION: "+xmlPeticion);
			return null;
		}
		LOGGER.info("GENERANDO PETICION STATUS... OK");
		LOGGER.info("OBTENIENDO RESPUESTA STATUS...");
		//2 - Ejecución de servicio por MQ
		Optional<String> xmlRespuesta = mqcService.execute("ESTATUS_SESION_USUARIO" + ".REQ", "ESTATUS_SESION_USUARIO" + ".RESP", xmlPeticion.get());
		if (!xmlRespuesta.isPresent()) {
			LOGGER.info("GENERANDO RESPUESTA STATUS...");
			LOGGER.info("RESULTADO XML RESPUESTA: "+xmlRespuesta);
			return null;
		}
		LOGGER.info("GENERANDO RESPUESTA STATUS... OK");
		//3 - Evaluación de la respuesta
		try {
			LOGGER.info("INTERPRETANDO RESPUESTA STATUS...");
			Document docResp = null;
			docResp = DocumentHelper.parseText(xmlRespuesta.get());
			Status statusResponse	= evaluarRespuesta(docResp);
			if(statusResponse == null) {
				LOGGER.info("NO SE OBTUVO RESPUESTA...");
				return null;
			}else{
				LOGGER.info("SE OBTUVO RESPUESTA...");
				return statusResponse;
			}
		} catch (DocumentException e) {
			LOGGER.error("Excepción parseando XML de respuesta de servicio " + "ESTATUS_SESION_USUARIO" + ": " + e.getMessage(), e);
			return null;
		}
	}

	public Status evaluarRespuesta(Document docResp) {
		Status statusResp = new Status();
		long codResp = Long.parseLong(docResp.selectSingleNode("/respuestaEntorno/header/codigo").getText());
        String status = docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaValidarSesion/ESTATUS_INGRESO") != null? docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaValidarSesion/ESTATUS_INGRESO").getText(): "DS";
		LOGGER.info("STATUS: "+status+"\n CODIGO: "+codResp);
		
		if (codResp != 0) {
			return null;
		} else {
				
			statusResp.setCanal(docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaValidarSesion/CANAL").getText());
			statusResp.setCorrelativo(docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaValidarSesion/CORRELATIVO").getText());
			statusResp.setStatusIngreso(docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaValidarSesion/ESTATUS_INGRESO").getText());
			statusResp.setUsuario(docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaValidarSesion/USUARIO").getText());
			statusResp.setUltimoLogin(docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaValidarSesion/FECHA_ULTIMO_INGRESO").getText());		
		}
		return statusResp;
	}
}
