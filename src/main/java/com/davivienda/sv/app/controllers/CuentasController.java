package com.davivienda.sv.app.controllers;

import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.Response;
import com.davivienda.sv.app.data.beans.token.CuentasRequest;
import com.davivienda.sv.app.dto.ListaCuenta;
import com.davivienda.sv.app.dto.ListaTarjeta;
import com.davivienda.sv.app.services.operaciones.CuentasService;
import com.davivienda.sv.app.services.operaciones.RegistraWRINTASService;
import com.davivienda.sv.app.services.operaciones.TarjetasService;
import com.davivienda.sv.app.services.validator.JWTValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/productos")
public class CuentasController {

	@Autowired
	private CuentasService cuentasService;

	@Autowired
	private TarjetasService tarjetasService;

	@Autowired
	RegistraWRINTASService reWrintasService;

	@Autowired
	private JWTValidator validJWT;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(validJWT);
	}

	@PostMapping(path = "/cuentas", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listar(@Validated @RequestBody Request<CuentasRequest> request, BindingResult valid)
			throws Throwable {
		if (valid.hasErrors()) {
			String message = valid.getAllErrors().get(0).getDefaultMessage();
			int codigo = Integer.parseInt(valid.getAllErrors().get(0).getCode());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Response<String>(request, new String(), codigo, message));
		}
		Response<ListaCuenta> resp = cuentasService.ejecutar(request, "LISTA_CUENTAS_CLIENTE");
		if (resp.getHeader().getCodigo() == 0) {
			return ResponseEntity.ok(resp);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
		}
	}

	@PostMapping(path = "/tarjetas", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> tarjetas(@Validated @RequestBody Request<CuentasRequest> request, BindingResult valid)
			throws Throwable {
		if (valid.hasErrors()) {
			String message = valid.getAllErrors().get(0).getDefaultMessage();
			int codigo = Integer.parseInt(valid.getAllErrors().get(0).getCode());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Response<String>(request, new String(), codigo, message));
		}
		Response<ListaTarjeta> resp = tarjetasService.ejecutar(request, "LISTA_CUENTAS_CLIENTE");
		if (resp.getHeader().getCodigo() == 0) {
			return ResponseEntity.ok(resp);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
		}
	}

}
