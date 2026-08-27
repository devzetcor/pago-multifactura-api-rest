package com.davivienda.sv.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.Response;
import com.davivienda.sv.app.dto.ListaEmpresasRequest;
import com.davivienda.sv.app.services.JWTService;
import com.davivienda.sv.app.services.SecurityService;
import com.davivienda.sv.app.services.datasource.ConsultaClientes;
import com.davivienda.sv.app.services.validator.JWTValidator;
import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/colecturia")
public class EmpresaController {

	final ConsultaClientes consultaClientes;

	@Autowired
	private JWTValidator validJWT;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(validJWT);
	}
	@Autowired
	JWTService jwtService;

	@Autowired
	SecurityService securityService;

	EmpresaController(ConsultaClientes consultaClientes) {
		this.consultaClientes = consultaClientes;
	}

	@PostMapping("/listaEmpresas")
	public ResponseEntity<?> obtenerEmpresaPorDocumentoYUsuario(
			@Validated
			@RequestBody Request<ListaEmpresasRequest> request,
			@RequestHeader("Authorization") String token, BindingResult valid) {
		if (valid.hasErrors()) {
			String message = valid.getAllErrors().get(0).getDefaultMessage();
			int codigo = Integer.parseInt(valid.getAllErrors().get(0).getCode());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Response<String>(request, new String(), codigo, message));
		}
		
			token=token.replace("Bearer ", "").trim();
        	Claims claims=jwtService.verifyToken(token);
        	String dni=(String) claims.get("dni");
        	securityService.perteneceUsuario(
        			dni
        			, request.getBody().getDocumento().trim());
        	
			return ResponseEntity.ok(consultaClientes.findByUsername(request));
		
	}
}
