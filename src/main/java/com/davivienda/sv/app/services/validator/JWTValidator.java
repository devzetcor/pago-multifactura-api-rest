package com.davivienda.sv.app.services.validator;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.Status;
import com.davivienda.sv.app.services.JWTService;
import com.davivienda.sv.app.services.operaciones.StatusService;
import com.davivienda.sv.app.util.AppException;

import io.jsonwebtoken.Claims;

@Component
public class JWTValidator implements Validator {

	private static final Logger LOGGER = LogManager.getLogger(JWTValidator.class);

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private StatusService statusSrv;

	@Autowired
	private JWTService jwtSrv;

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(Request.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void validate(Object target, Errors errors) {
		LOGGER.debug("INGRESA AL VALIDADOR: ");
		Request<Object> peticion = (Request<Object>) target;
		Claims claims = null;
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (token == null || !token.trim().startsWith(JWTService.AUTH_PREFIX)) {
			errors.reject("1005", "TOKEN DE SESION FALTANTE");
			throw new AppException("TOKEN DE SESION FALTANTE", 1005);
		}
		// quitamos "Bearer"
		token = token.trim().replace(JWTService.AUTH_PREFIX, "");
		claims = jwtSrv.verifyToken(token);

		if (claims == null || claims.getSubject() == null || claims.getSubject().trim().isEmpty()) {
			errors.reject("1006", "TOKEN DE SESION INVALIDO");
			throw new AppException("TOKEN DE SESION INVALIDO", 1006);
		}
		// obtenemos el usuario
		token = claims.getSubject().trim();
		if (peticion.getHeader().getIdSesion() == null && !peticion.getHeader().getIdSesion().trim().equals(token)) {
			errors.reject("1021", "SESION INVALIDA");
			throw new AppException("SESION INVALIDA", 1021);
		}
		// call estatus
		Status status = statusSrv.ejecutar(peticion.getHeader().getUsuario());
		LOGGER.debug("STATUS OBJECT: " + status);
		if (status == null) {
			errors.reject("1020", "NO FUE POSIBLE VALIDAR LA SESION");
			throw new AppException("NO FUE POSIBLE VALIDAR LA SESION", 1020);
		} else if (!status.getCorrelativo().trim().equals(peticion.getHeader().getIdSesion().trim())) {
			errors.reject("1021", "SESION INVALIDA");
			throw new AppException("SESION INVALIDA", 1021);
		} else if (!status.isActive()) {
			errors.reject("1017", "SESION NO ACTIVA");
			throw new AppException("SESION NO ACTIVA", 1017);
		}
	}

}
