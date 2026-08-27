package com.davivienda.sv.app.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.davivienda.sv.app.data.beans.BasicRequest;
import com.davivienda.sv.app.data.beans.BasicResponse;
import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.Response;
import com.davivienda.sv.app.data.beans.Status;
import com.davivienda.sv.app.data.beans.StatusRequest;
import com.davivienda.sv.app.data.beans.sesion.CambioClaveRequest;
import com.davivienda.sv.app.data.beans.sesion.LoginRequest;
import com.davivienda.sv.app.data.beans.sesion.LoginResponse;
import com.davivienda.sv.app.dto.ContenedorDTO;
import com.davivienda.sv.app.dto.MigrarSesionUsuarioDataRequest;
import com.davivienda.sv.app.services.ConsultaSesionService;
import com.davivienda.sv.app.services.JWTService;
import com.davivienda.sv.app.services.operaciones.CambioClaveService;
import com.davivienda.sv.app.services.operaciones.LoginService;
import com.davivienda.sv.app.services.operaciones.LogoutService;
import com.davivienda.sv.app.services.operaciones.MigrarSesion;
import com.davivienda.sv.app.services.operaciones.RegistraWRINTASService;
import com.davivienda.sv.app.services.operaciones.StatusService;

@RestController
@RequestMapping(path = "/sesion")
public class SesionController {
	private static final Logger LOGGER = LogManager.getLogger(SesionController.class);

	@Autowired
	private JWTService jwtSrv;

	@Autowired
	private LoginService loginSrv;

	@Autowired
	private LogoutService logoutSrv;

	@Autowired
	private CambioClaveService claveSrv;

	@Autowired
	private StatusService statusSrv;

	@Autowired
	private ConsultaSesionService consultaSesionService;

	@Autowired
	private MigrarSesion migrarSesion;

	@Autowired
	RegistraWRINTASService reWrintasService;

	@PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> login(@RequestBody Request<LoginRequest> request) throws Throwable {
		Response<LoginResponse> resp = loginSrv.ejecutar(request, "INICIAR_SESION_USUARIO");
		if (resp.getHeader().getCodigo() == 0) {
			String jwt = jwtSrv.generateToken(
				resp.getHeader().getIdSesion(),
				resp.getBody().getNombre(),
				resp.getBody().getNumeroDocumento(),
				String.valueOf(resp.getBody().getNiu())
			);
			reWrintasService.loginExitoso(request.getBody().getUsuario().toUpperCase(), resp.getBody().getNiu(),
					request.getHeader().getIp(), request.getBody().getImei(), request.getBody().getSistemaOperativo(),
					request.getHeader().getIdTransaccion(), resp.getHeader().getIdSesion(),
					resp.getHeader().getCodigo());
			return new ResponseEntity<>(resp, jwtSrv.generateHeaders(jwt), HttpStatus.OK);

		}
		Request<BasicRequest> requestSet = new Request<BasicRequest>(request);
		requestSet.getHeader().setIdSesion(resp.getHeader().getIdSesion());
		requestSet.getHeader().setUsuario(request.getBody().getUsuario());
		if(resp.getHeader().getCodigo()==1018 ||resp.getHeader().getCodigo()==1014) {
			resp.getHeader().setDescripcion("CREDENCIALES INVALIDAS");
		}
		if (resp.getHeader().getCodigo() == 1) {
			LOGGER.error("ERROR CODIGO #1: " + resp.getHeader().getDescripcion());
			reWrintasService.loginChangePassword(request.getBody().getUsuario().toUpperCase(), resp.getBody().getNiu(),
					request.getHeader().getIp(), request.getBody().getImei(), request.getBody().getSistemaOperativo(),
					request.getHeader().getIdTransaccion(), resp.getHeader().getIdSesion(),
					resp.getHeader().getCodigo());
			logoutSrv.ejecutar(requestSet, "CERRAR_SESION_USUARIO");
			String jwt = jwtSrv.generateToken(request.getBody().getUsuario());
			return new ResponseEntity<>(resp, jwtSrv.generateHeaders(jwt), HttpStatus.OK);

		} else if (resp.getHeader().getCodigo() == 1014) {
			reWrintasService.loginFallidoPassword(request.getBody().getUsuario().toUpperCase(), resp.getBody().getNiu(),
					request.getHeader().getIp(), request.getBody().getImei(), request.getBody().getSistemaOperativo(),
					request.getHeader().getIdTransaccion(), resp.getHeader().getIdSesion(),
					resp.getHeader().getCodigo());
			logoutSrv.ejecutar(requestSet, "CERRAR_SESION_USUARIO");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);

		} else if (resp.getHeader().getCodigo() == 1015) {
			reWrintasService.loginSuspendido(request.getBody().getUsuario().toUpperCase(), resp.getBody().getNiu(),
					request.getHeader().getIp(), request.getBody().getImei(), request.getBody().getSistemaOperativo(),
					request.getHeader().getIdTransaccion(), resp.getHeader().getIdSesion(),
					resp.getHeader().getCodigo());
			logoutSrv.ejecutar(requestSet, "CERRAR_SESION_USUARIO");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);

		} else if (resp.getHeader().getCodigo() == 1018) {
			reWrintasService.loginInvalidUser(request.getBody().getUsuario().toUpperCase(), resp.getBody().getNiu(),
					request.getHeader().getIp(), request.getBody().getImei(), request.getBody().getSistemaOperativo(),
					request.getHeader().getIdTransaccion(), resp.getHeader().getIdSesion(),
					resp.getHeader().getCodigo());
			logoutSrv.ejecutar(requestSet, "CERRAR_SESION_USUARIO");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);

		} else if (resp.getHeader().getCodigo() == 1026) {
			reWrintasService.loginInactividad(request.getBody().getUsuario().toUpperCase(), resp.getBody().getNiu(),
					request.getHeader().getIp(), request.getBody().getImei(), request.getBody().getSistemaOperativo(),
					request.getHeader().getIdTransaccion(), resp.getHeader().getIdSesion(),
					resp.getHeader().getCodigo());
			logoutSrv.ejecutar(requestSet, "CERRAR_SESION_USUARIO");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);

		} else if (resp.getHeader().getCodigo() == 1016) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);

		} else {
			logoutSrv.ejecutar(requestSet, "CERRAR_SESION_USUARIO");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
		}

	}

	@PostMapping(path = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> logout(@RequestBody Request<BasicRequest> request) throws Throwable {
		Response<BasicResponse> resp = logoutSrv.ejecutar(request, "CERRAR_SESION_USUARIO");

		if (request.getBody().getModoOperacion() == 1) {
			logoutSrv.ejecutar(request, "CERRAR_SESION_USUARIO");
			reWrintasService.logout(request.getHeader().getUsuario().toUpperCase(), request.getHeader().getIp(),
					request.getHeader().getIdTransaccion(), resp.getHeader().getIdSesion(),
					resp.getHeader().getCodigo());
		} else {
			reWrintasService.logout(request.getHeader().getUsuario().toUpperCase(), request.getHeader().getIp(),
					request.getHeader().getIdTransaccion(), resp.getHeader().getIdSesion(),
					resp.getHeader().getCodigo());
		}

		if (resp.getHeader().getCodigo() == 0) {
			return ResponseEntity.ok(resp);
		} else if (resp.getHeader().getCodigo() == 1002 || resp.getHeader().getCodigo() == 1004) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
		}

	}

	@GetMapping(path = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> status(@RequestBody Request<StatusRequest> request) throws Throwable {
		Response<BasicResponse> resp = new Response<>(request, new BasicResponse());
		Response<Status> respService = statusSrv.ejecutar(request, "ESTATUS_SESION_USUARIO");
		if (respService.getHeader().getCodigo() == 0) {
			return ResponseEntity.ok(resp);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new Response<BasicResponse>(request, new BasicResponse(), 1017));
		}

	}

	@PostMapping(path = "/ConsultaSesion")
	public ResponseEntity<?> consultaSesion(@RequestBody Request<MigrarSesionUsuarioDataRequest> request) {
		Response<LoginResponse> response = null;
		try {
			if (request.getBody().getToken().isEmpty()) {
				if (request.getBody().getCanal().isEmpty())
					throw new Throwable("Canal esta vacio o es nulo");

				if ((request.getBody().getCorrelativo().isEmpty()) && request.getBody().getUsuario().isEmpty())
					throw new Throwable("Correlativo y Usuario vacio o nulos");

				LOGGER.info("Validando Request... OK");

				LOGGER.info("Ejecutando servicio migrar session...");
				response = migrarSesion.ejecutar(request, "MIGRAR_SESION_USUARIO");
				LOGGER.info("Ejecutando servicio migrar session... OK");
			} else {
				ContenedorDTO contenedorDTO = new ContenedorDTO();
				contenedorDTO.setIdSesion(request.getHeader().getIdSesion());
				contenedorDTO.setIdTransaccion(request.getHeader().getIdTransaccion());
				contenedorDTO.setIp(request.getHeader().getIp());
				contenedorDTO.setToken(request.getBody().getToken());

				LOGGER.info("Ejecutando servicio gestion cache...");
				String usuario = consultaSesionService.ejecutar(contenedorDTO);
				LOGGER.info("usuario::" + usuario);
				LOGGER.info("Ejecutando servicio gestion cache... OK");

				request.getBody().setCanal("WEB");
				request.getBody().setCorrelativo("");
				request.getBody().setUsuario(usuario);

				LOGGER.info("Ejecutando servicio migrar session... Gestion cache");
				response = migrarSesion.ejecutar(request, "MIGRAR_SESION_USUARIO");
				LOGGER.info("Ejecutando servicio migrar session... Gestion Cache OK");
			}

			LOGGER.info("Asignando Canal a Respuesta...");
			if (response.getHeader().getCodigo() == 0) {
				
			}
			LOGGER.info("Asignando Canal a Respuesta... OK");
		} catch (Exception e) {
			LOGGER.info("Ejecutando Login... ERROR:" + e.getMessage(),e);
		} catch (Throwable e) {
			LOGGER.fatal("Ejecutando Login... ERROR:" + e.getMessage(), e);
		}

		
		LOGGER.info("Response:");
		if (response.getHeader().getCodigo()==0) {
			String jwt = jwtSrv.generateToken(response.getHeader().getIdSesion(),
					response.getBody().getUsuario(),
					response.getBody().getNumeroDocumento(),
					String.valueOf(response.getBody().getNiu()));
			return new ResponseEntity<>(response, jwtSrv.generateHeaders(jwt), HttpStatus.OK);
			
		} else
		return new ResponseEntity<>(response, null, HttpStatus.UNAUTHORIZED);

	}

	@PostMapping(path = "/cambiarClave", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> cambiarClave(@RequestBody Request<CambioClaveRequest> request) throws Throwable {
		Response<BasicResponse> resp = this.claveSrv.ejecutar(request, "CAMBIAR_CLAVE_ACCESO");
		if (request.getBody().getModoOperacion() == 1) {
			reWrintasService.recuperarClave(request.getBody().getUsuario().toUpperCase(), request.getHeader().getIp(),
					request.getBody().getImei(), request.getBody().getSistemaOperativo(),
					request.getHeader().getIdTransaccion(), resp.getHeader().getIdSesion(),
					resp.getHeader().getCodigo());
		} else {
			reWrintasService.cambiarClave(request.getHeader().getUsuario().toUpperCase(), request.getHeader().getIp(),
					request.getHeader().getIdTransaccion(), resp.getHeader().getIdSesion(),
					resp.getHeader().getCodigo());
		}
		if (resp.getHeader().getCodigo() == 0) {
			return ResponseEntity.ok(resp);
		} else if (resp.getHeader().getCodigo() >= 1007 && resp.getHeader().getCodigo() <= 1013) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
		}

	}

}