package com.davivienda.sv.app.controllers;

import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.Response;
import com.davivienda.sv.app.dto.CuentaRequestDto;
import com.davivienda.sv.app.dto.CuentaResponseDto;
import com.davivienda.sv.app.services.CuentaService;
import com.davivienda.sv.app.services.JWTService;
import com.davivienda.sv.app.services.SecurityService;
import com.davivienda.sv.app.services.validator.JWTValidator;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

	@Autowired
	private JWTValidator validJWT;
	
	@Autowired
	JWTService jwtService;
	@Autowired
	SecurityService securityService;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(validJWT);
	}
	
    @PostMapping("/buscar")
    public ResponseEntity<Object> getCuentasByClienteAndUsuario(
        @RequestBody Request<CuentaRequestDto> request,
    	@RequestHeader("Authorization") String token
        
    ) {
        Response<List<CuentaResponseDto>> resp = new Response<>(request, new ArrayList<CuentaResponseDto>());

        // Validar parámetros de entrada
        if (request.getBody().getCliente() == null || request.getBody().getCliente() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
        }

        if (request.getBody().getUsuario() == null || request.getBody().getUsuario().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
        }
        token=token.replace("Bearer ", "").trim();
    	Claims claims=jwtService.verifyToken(token);
    	String dni=(String) claims.get("dni");
    	securityService.perteneceUsuarioEmpresa(dni, request.getBody().getUsuario(), request.getBody().getCliente().intValue());
        // Buscar las cuentas
        List<CuentaResponseDto> cuentas = cuentaService
                .getCuentasByClienteAndUsuario(request.getBody());
        resp.setBody(cuentas);

        return ResponseEntity.ok(resp);

    }

}