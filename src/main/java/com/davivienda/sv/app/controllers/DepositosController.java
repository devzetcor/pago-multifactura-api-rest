package com.davivienda.sv.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.Response;
import com.davivienda.sv.app.services.validator.JWTValidator;

@RestController
@RequestMapping("/depositos")
public class DepositosController {

	@Autowired
	private JWTValidator validJWT;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(validJWT);
	}

	 @GetMapping(path = "/ejecutar", produces = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity<Object>  ejecutar(@Validated @RequestBody Request<Object> request, BindingResult valid) {
			if (valid.hasErrors()) {
				String message = valid.getAllErrors().get(0).getDefaultMessage();
				int codigo = Integer.parseInt(valid.getAllErrors().get(0).getCode());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(new Response<String>(request, new String(), codigo, message));
			}
			return ResponseEntity.status(HttpStatus.OK)
					.body(new Response<String>(request));
	    }

}
