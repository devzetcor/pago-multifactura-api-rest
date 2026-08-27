package com.davivienda.sv.app.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.davivienda.sv.app.data.beans.BasicResponse;
import com.davivienda.sv.app.data.beans.ModosAutenticacion;
import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.Response;
import com.davivienda.sv.app.data.beans.token.GenerarOtpRequest;
import com.davivienda.sv.app.data.beans.token.ValidarOtpRequest;
import com.davivienda.sv.app.data.beans.usuario.InfoUsuarioRequest;
import com.davivienda.sv.app.data.beans.usuario.InfoUsuarioResponse;
import com.davivienda.sv.app.services.JWTService;
import com.davivienda.sv.app.services.operaciones.GenerarOtpService;
import com.davivienda.sv.app.services.operaciones.InfoUsuarioService;
import com.davivienda.sv.app.services.operaciones.RegistraWRINTASService;
import com.davivienda.sv.app.services.operaciones.ValidarHardTokenService;
import com.davivienda.sv.app.services.operaciones.ValidarOtpService;
import com.davivienda.sv.app.services.operaciones.ValidarSoftTokenService;
import com.davivienda.sv.app.services.validator.JWTValidator;
import io.jsonwebtoken.Claims;

@RestController
@RequestMapping(path = "/token")
public class TokenController {

	private static final Logger LOGGER = LogManager.getLogger(TokenController.class);

	@Autowired
	private GenerarOtpService genOtpSrv;

	@Autowired
	private ValidarOtpService valOtpSrv;

	@Autowired
	private ValidarHardTokenService hardSrv;

	@Autowired
	private ValidarSoftTokenService softSrv;

	@Autowired
	private InfoUsuarioService usrSrv;

	@Autowired
	RegistraWRINTASService reWrintasService;

	@Autowired
	private JWTValidator validJWT;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(validJWT);
	}
	@Autowired
	JWTService jwtService;

	@PostMapping(path = "/generar", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> generarOtp(@Validated @RequestBody Request<GenerarOtpRequest> request,@RequestHeader("Authorization") String token,
			BindingResult valid) throws Throwable {
		if (valid.hasErrors()) {
			String message = valid.getAllErrors().get(0).getDefaultMessage();
			int codigo = Integer.parseInt(valid.getAllErrors().get(0).getCode());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Response<String>(request, new String(), codigo, message));
		}
		token=token.replace("Bearer ", "").trim();
    	Claims claims=jwtService.verifyToken(token);
    	// 1. Extraemos los valores de forma segura
    	String niu = String.valueOf(claims.getOrDefault("niu", ""));
    	String dni = String.valueOf(claims.getOrDefault("dni", ""));

    	// 2. Definimos la lógica de negocio con nombres claros
    	boolean esOperadorPyme = "-1".equals(niu);
    	String identificadorAcceso = esOperadorPyme ? dni : niu;
    	request.getBody().setEsOperador(String.valueOf(esOperadorPyme));
    	request.getBody().setNiuString(identificadorAcceso);

    	// 3. Ejecutamos el servicio
    	Response<BasicResponse> resp = genOtpSrv.ejecutar(
    	    request, 
    	    "GENERAR_OTP_PYME"
    	);
		reWrintasService.generarOTP(request.getHeader().getUsuario().toUpperCase(), request.getBody().getNiu(),
				request.getHeader().getIp(), request.getHeader().getIdTransaccion(), resp.getHeader().getIdSesion(),
				resp.getHeader().getCodigo());
		if (resp.getHeader().getCodigo() == 0) {
			return ResponseEntity.ok(resp);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
		}
	}

	@PostMapping(path = "/validar", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> validarOtp(@Validated @RequestBody Request<ValidarOtpRequest> request,@RequestHeader("Authorization") String token,
			BindingResult valid) throws Throwable {
		if (valid.hasErrors()) {
			String message = valid.getAllErrors().get(0).getDefaultMessage();
			int codigo = Integer.parseInt(valid.getAllErrors().get(0).getCode());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Response<String>(request, new String(), codigo, message));
		}
		Response<BasicResponse> resp = null;

		int modoAutenticacion = this.getModoAutenticacion(request);
		token=token.replace("Bearer ", "").trim();
    	
		Claims claims=jwtService.verifyToken(token);
    	// 1. Extraemos los valores de forma segura
    	String niu = String.valueOf(claims.getOrDefault("niu", ""));
    	String dni = String.valueOf(claims.getOrDefault("dni", ""));

    	// 2. Definimos la lógica de negocio con nombres claros
    	boolean esOperadorPyme = "-1".equals(niu);
    	String identificadorAcceso = esOperadorPyme ? dni : niu;
    	
    	request.getBody().setIsOperador(String.valueOf(esOperadorPyme));
    	request.getBody().setNiuString(identificadorAcceso);
		switch (modoAutenticacion) {
		case ModosAutenticacion.SMS_OTP:
			resp = valOtpSrv.ejecutar(request, "VALIDACION_OTP_SMS_DID");
			reWrintasService.validarOTP(request.getHeader().getUsuario().toUpperCase(), request.getBody().getNiu(),
					request.getHeader().getIp(), request.getHeader().getIdTransaccion(), resp.getHeader().getIdSesion(),
					resp.getHeader().getCodigo());
			break;
		case ModosAutenticacion.HARD_TOKEN:
			resp = hardSrv.ejecutar(request, "VALIDA_TOKEN_USUARIO");
			break;
		case ModosAutenticacion.SOFT_TOKEN:
			resp = softSrv.ejecutar(request, "AUTENTICAR_TRANSACCION");
			break;
		default:
			resp = valOtpSrv.ejecutar(request, "VALIDACION_OTP_SMS_DID");
			break;
		}
		if (resp.getHeader().getCodigo() == 0) {
			return ResponseEntity.ok(resp);
		} else if (resp.getHeader().getCodigo() == 1101) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
		}

	}

	public int getModoAutenticacion(Request<ValidarOtpRequest> request) throws Throwable {
		Request<InfoUsuarioRequest> req = new Request<InfoUsuarioRequest>(request);
		req.setBody(new InfoUsuarioRequest());
		req.getBody().setUsuario(request.getHeader().getUsuario());

		Response<InfoUsuarioResponse> resp = null;
		try {
			resp = usrSrv.ejecutar(req, "OBTENER_INFO_USUARIO");
			if (resp.getHeader().getCodigo() == 0) {
				return resp.getBody().getModoAutenticacion();
			} else {
				return ModosAutenticacion.SMS_OTP;
			}
		} catch (Exception e) {
			LOGGER.error("Excepcion:" + e.getMessage(), e);
			throw e;
		}
	}
}
