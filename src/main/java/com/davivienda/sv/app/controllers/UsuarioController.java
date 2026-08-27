package com.davivienda.sv.app.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.Response;
import com.davivienda.sv.app.data.beans.usuario.InfoUsuarioRequest;
import com.davivienda.sv.app.data.beans.usuario.InfoUsuarioResponse;
import com.davivienda.sv.app.services.operaciones.InfoUsuarioService;
import com.davivienda.sv.app.services.validator.JWTValidator;

@RestController
@RequestMapping(path = "/usuario")
public class UsuarioController {
	
	@Autowired
	private InfoUsuarioService usrSrv;
	
	@Autowired
	private JWTValidator validJWT;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(validJWT);
	}

	@PostMapping(path = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<InfoUsuarioResponse>> infoUsuario(@RequestBody Request<InfoUsuarioRequest> request) throws Throwable {

		Response<InfoUsuarioResponse> resp = usrSrv.ejecutar(request, "OBTENER_INFO_USUARIO");
			if (resp.getHeader().getCodigo() == 0) {
				return ResponseEntity.ok(resp);
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
			}

	}
}
