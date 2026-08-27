package com.davivienda.sv.app.controllers;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.Response;
import com.davivienda.sv.app.dto.InfoUsuarioDto;
import com.davivienda.sv.app.dto.UsuarioClienteRequestDto;
import com.davivienda.sv.app.services.JWTService;
import com.davivienda.sv.app.services.SecurityService;
import com.davivienda.sv.app.services.UsuarioWCService;
import com.davivienda.sv.app.services.validator.JWTValidator;
import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/colecturia/usuario")
public class UsuarioWCController {
    
    final UsuarioWCService usuarioWCService;

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
	
    public UsuarioWCController(UsuarioWCService usuarioWCService){
        this.usuarioWCService = usuarioWCService;
    }

    @PostMapping("/info")
    public ResponseEntity<?> getUserInfo(
        @RequestBody Request<UsuarioClienteRequestDto> request,
        @RequestHeader("Authorization")String token, BindingResult valid
    ) {
    	if (valid.hasErrors()) {
			String message = valid.getAllErrors().get(0).getDefaultMessage();
			int codigo = Integer.parseInt(valid.getAllErrors().get(0).getCode());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Response<String>(request, new String(), codigo, message));
		}
        try {
        	token=token.replace("Bearer ", "").trim();
        	Claims claims=jwtService.verifyToken(token);
        	String dni=(String) claims.get("dni");
        	
        	securityService.perteneceUsuarioEmpresa(
        			dni
        			, request.getBody().getUsuario().trim()
        			, request.getBody().getCliente().intValue());
        	
            Response<InfoUsuarioDto> resultado = usuarioWCService.getInfoUsuario(request);
            return ResponseEntity.ok(resultado);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + ex.getMessage());
        }
    }

}
