package com.davivienda.sv.app.controllers;

import com.davivienda.sv.app.data.beans.*;
import com.davivienda.sv.app.services.JWTService;
import com.davivienda.sv.app.services.operaciones.StatusService;
import io.jsonwebtoken.Claims;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping(path = "/jwt")
public class JWTController {

	private static final Logger LOGGER = LogManager.getLogger(JWTController.class);

	@Autowired
	private JWTService jwtSrv;

	@Autowired
	private StatusService statusSrv;

	@PostMapping(path = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> generarToken(
		@RequestBody Request<BasicRequest> request,
		@RequestHeader("Authorization") String authorizationHeader
	) {
			String authJwt = authorizationHeader.substring(7);

			Claims claims = jwtSrv.verifyToken(authJwt);

			String dni = claims.getOrDefault("dni", "").toString();
			String username = claims.getOrDefault("username", "").toString();
			String niu = claims.getOrDefault("niu", "").toString();

			Response<BasicResponse> resp = new Response<>(request, new BasicResponse());

			String jwt = jwtSrv.generateToken(
				request.getHeader().getIdSesion(),
				username,
				dni,
				niu
			);

			Status status = statusSrv.ejecutar(request.getHeader().getUsuario());
			LOGGER.debug("STATUS: " + status);
			if (status == null) {
				resp.getHeader().setCodigo(120);
				resp.getHeader().setDescripcion("NO FUE POSIBLE VALIDAR LA SESION");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
			} else if (!status.getCorrelativo().trim().equals(request.getHeader().getIdSesion().trim())) {
				resp.getHeader().setCodigo(121);
				resp.getHeader().setDescripcion("SESION INVALIDA");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
			} else if (!status.isActive()) {
				resp.getHeader().setCodigo(1017);
				resp.getHeader().setDescripcion("SESION NO ACTIVA");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
			}

			return new ResponseEntity<>(resp, jwtSrv.generateHeaders(jwt), HttpStatus.OK);

	}
}
