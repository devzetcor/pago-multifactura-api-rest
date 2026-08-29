package com.davivienda.sv.app.services.operaciones;

import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.Response;
import com.davivienda.sv.app.data.beans.sesion.LoginRequest;
import com.davivienda.sv.app.data.beans.sesion.LoginResponse;
import com.davivienda.sv.app.services.CryptoService;
import com.davivienda.sv.app.services.JEncryptorService;
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
public class LoginService extends TransaccionService<LoginRequest, LoginResponse>
{
    private SimpleDateFormat dFmt = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Logger LOGGER = LogManager.getLogger(LoginService.class);
    @Autowired
    private CryptoService cryptoService;
    @Autowired
    @Qualifier(R.MQCliente.PFS.NAME)
    MQCliente mqcService;
    @Autowired
    JEncryptorService jEncryptoService;

    public Optional<String> construirPeticion(Request<LoginRequest> request) {
        String clave = request.getBody().getClave().trim();
//		LOGGER.info("Obteniendo clave front-end... "+clave);
        try {
//            clave = this.jEncryptoService.desencriptar(clave);
//			LOGGER.info("Obteniendo clave desencriptada... "+clave);
            clave = this.cryptoService.encriptar(clave, this.cryptoService.obtenerSemilla(request.getBody().getUsuario()));
            
            String xmlPeticion = "<peticionEntorno><header><fabrica>"+R.Fabricas.ESBeBanca+"</fabrica><servicio>INICIAR_SESION_USUARIO</servicio></header><body><contenedor><peticionIniciarSesion><usuario><nombre>"
                    + request.getBody().getUsuario() + "</nombre><clave>" + clave + "</clave><ip>" + request.getHeader().getIp() + "</ip>"
                    + "<imei>"+request.getBody().getImei()+"</imei><sistemaOperativo>"+request.getBody().getSistemaOperativo()+"</sistemaOperativo>"
                    + "<idTransaccion>"+request.getHeader().getIdTransaccion()+"</idTransaccion><idSesion>"+request.getHeader().getIdSesion()+"</idSesion>"
                    + "</usuario></peticionIniciarSesion></contenedor></body></peticionEntorno>";
            return Optional.of(xmlPeticion);
        }
        catch (Exception e) {
            this.LOGGER.error("Exception: Error al desencriptar contraseña... "+e.getMessage());
            return Optional.empty();
        }
    }

    public Response<LoginResponse> evaluarRespuesta(Request<LoginRequest> request, Document docResp) {
        Response<LoginResponse> resp = new Response<LoginResponse>(request, new LoginResponse());
        LOGGER.info("Obteniendo respuesta servicio LoginService...");

        long codResp = Long.parseLong(docResp.selectSingleNode("/respuestaEntorno/header/codigo").getText());
        String descResp = docResp.selectSingleNode("/respuestaEntorno/header/descripcion").getText();
        String correlativo = docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaIniciarSesion/correlativo")==null?"0":docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaIniciarSesion/correlativo").getText();

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
        }else if (codResp != 0L) {
            resp = new Response<LoginResponse>(request, new LoginResponse(), 1003,  "INICIAR_SESION_USUARIO", descResp);
            resp.getHeader().setIdSesion(correlativo);
            return resp;
        }
      String niuString =  docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaIniciarSesion/niu").getText();
        if (niuString != null && !niuString.isEmpty()) {
			Long niu = Long.parseLong(niuString);
			resp.getBody().setNiu(niu);
		}else {
			resp.getBody().setNiu(-1);
		}
        String nombre = docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaIniciarSesion/nombre").getText();
        int tipoAutorizacion = Integer.parseInt(docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaIniciarSesion/tipoAutorizacion").getText());
        String tipoDocumento = docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaIniciarSesion/tipoIdentificacion").getText();
        String numeroDocumento = docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaIniciarSesion/numeroIndentificacion").getText();
        resp.getHeader().setIdSesion(correlativo);
        resp.getBody().setNombre(nombre);
        resp.getBody().setModoAutenticacion(tipoAutorizacion);
        resp.getBody().setTipoDocumento(tipoDocumento);
        resp.getBody().setNumeroDocumento(numeroDocumento);

        try {
            String fecha = docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaIniciarSesion/ultimoLogin").getText();
            if (fecha == null || fecha.trim().isEmpty()) {
                this.LOGGER.info("No se obtuvo fecha del ultimo login");
                resp.getBody().setUltimoLogin("");
            }
            else {
                resp.getBody().setUltimoLogin(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.dFmt.parse(fecha)));
            }
        }
        catch (Throwable e) {
            this.LOGGER.error("Excepción parseando la fecha " + docResp.selectSingleNode("/respuestaEntorno/body/contenedor/respuestaIniciarSesion/ultimoLogin").getText() + ": " + e.getMessage(), (Throwable)e);
        }
        LOGGER.info("Obteniendo respuesta servicio LoginService...OK");

        return resp;
    }

    public Response<LoginResponse> ejecutar(Request<LoginRequest> request, String nombreServicio) throws Throwable {
        return super.ejecutar(request, nombreServicio, this.mqcService);
    }
}
