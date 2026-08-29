package com.davivienda.sv.app.controllers;

import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.Response;
import com.davivienda.sv.app.dto.ConsultaCuentaCargoRequestDto;
import com.davivienda.sv.app.dto.ConsultaTransaccionesPorDocumentoRequestDto;
import com.davivienda.sv.app.dto.CuentaCargoResponseDto;
import com.davivienda.sv.app.dto.TransaccionesPorEmpresaResponseDto;
import com.davivienda.sv.app.services.JWTService;
import com.davivienda.sv.app.services.SecurityService;
import com.davivienda.sv.app.services.TransaccionConsultaService;
import com.davivienda.sv.app.services.validator.JWTValidator;
import io.jsonwebtoken.Claims;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultas")
public class TransaccionConsultaController {

    private static final Logger LOGGER = LogManager.getLogger(TransaccionConsultaController.class);

    @Autowired
    private TransaccionConsultaService transaccionConsultaService;

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
    /**
     * Endpoint 1: Consulta transacciones pendientes por número de documento
     * Busca las empresas relacionadas con el usuario y devuelve transacciones pendientes agrupadas por colector
     */
    @PostMapping("/transaccionesPorDocumento")
    public ResponseEntity<Object> consultarTransaccionesPorDocumento(
    		@Validated @RequestBody Request<ConsultaTransaccionesPorDocumentoRequestDto> request
    		,@RequestHeader("Authorization") String token
    		, BindingResult valid) {
    	if (valid.hasErrors()) {
			String message = valid.getAllErrors().get(0).getDefaultMessage();
			int codigo = Integer.parseInt(valid.getAllErrors().get(0).getCode());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Response<String>(request, new String(), codigo, message));
		}
        LOGGER.info("Iniciando consulta de transacciones por documento: " + request.getBody().getNumeroDocumento());
        
        Response<TransaccionesPorEmpresaResponseDto> resp = new Response<>(request, new TransaccionesPorEmpresaResponseDto());
        
        try {
            // Validar parámetros de entrada
            if (request.getBody().getNumeroDocumento() == null || 
                request.getBody().getNumeroDocumento().trim().isEmpty()) {
                resp.getHeader().setCodigo(400);
                resp.getHeader().setDescripcion("El número de documento es requerido");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
            }
            token=token.replace("Bearer ", "").trim();
        	Claims claims=jwtService.verifyToken(token);
        	String dni=(String) claims.get("dni");
        	
        	securityService.perteneceUsuario(
        			dni
        			, request.getBody().getNumeroDocumento());
            // Consultar transacciones
            TransaccionesPorEmpresaResponseDto resultado = transaccionConsultaService
                    .consultarTransaccionesPorDocumento(request.getBody().getNumeroDocumento().trim());
            
            resp.setBody(resultado);
            
            LOGGER.info("Consulta exitosa. Registros encontrados: " + resultado.getResumenTransacciones().size());
            
            return ResponseEntity.ok(resp);
            
        } catch (Exception e) {
            LOGGER.error("Error al consultar transacciones por documento: " + e.getMessage(), e);
            resp.getHeader().setCodigo(500);
            resp.getHeader().setDescripcion("Error interno del servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }

    /**
     * Endpoint 2: Consulta cuenta de cargo de la última transacción por colector, empresa y usuario
     */
    @PostMapping("/cuentaCargoUltimaTransaccion")
    public ResponseEntity<Object> consultarCuentaCargoUltimaTransaccion(
    		@Validated @RequestBody Request<ConsultaCuentaCargoRequestDto> request
    		,@RequestHeader("Authorization") String token,
    		BindingResult valid) {
    	if (valid.hasErrors()) {
			String message = valid.getAllErrors().get(0).getDefaultMessage();
			int codigo = Integer.parseInt(valid.getAllErrors().get(0).getCode());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Response<String>(request, new String(), codigo, message));
		}
        LOGGER.info("Iniciando consulta de cuenta de cargo para colector: " + request.getBody().getIdColector() + 
                   ", empresa: " + request.getBody().getIdEmpresa() + 
                   ", usuario: " + request.getBody().getUsuario());
        
        Response<CuentaCargoResponseDto> resp = new Response<>(request, new CuentaCargoResponseDto());
        
        try {
            // Validar parámetros de entrada
            ConsultaCuentaCargoRequestDto requestData = request.getBody();
            if (requestData.getIdColector() == null || requestData.getIdColector() <= 0) {
                resp.getHeader().setCodigo(400);
                resp.getHeader().setDescripcion("El ID del colector es requerido y debe ser válido");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
            }
            
            if (requestData.getIdEmpresa() == null || requestData.getIdEmpresa() <= 0) {
                resp.getHeader().setCodigo(400);
                resp.getHeader().setDescripcion("El ID de la empresa es requerido y debe ser válido");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
            }
            
            if (requestData.getUsuario() == null || requestData.getUsuario().trim().isEmpty()) {
                resp.getHeader().setCodigo(400);
                resp.getHeader().setDescripcion("El usuario es requerido");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
            }
            token=token.replace("Bearer ", "").trim();
        	Claims claims=jwtService.verifyToken(token);
        	String dni=(String) claims.get("dni");
        	
        	securityService.perteneceUsuarioEmpresa(
        			dni
        			, request.getBody().getUsuario(),request.getBody().getIdEmpresa().intValue());
            // Consultar cuenta de cargo
            CuentaCargoResponseDto resultado = transaccionConsultaService.consultarCuentaCargo(
                requestData.getIdColector(),
                requestData.getIdEmpresa(),
                requestData.getUsuario().trim()
            );
            
            if (resultado == null) {
                resp.getHeader().setCodigo(404);
                resp.getHeader().setDescripcion("No se encontró ninguna transacción para los parámetros especificados");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
            }
            
            resp.setBody(resultado);
            
            LOGGER.info("Consulta exitosa. Cuenta de cargo encontrada: " + resultado.getCuentaCargo());
            
            return ResponseEntity.ok(resp);
            
        } catch (Exception e) {
            LOGGER.error("Error al consultar cuenta de cargo: " + e.getMessage(), e);
            resp.getHeader().setCodigo(500);
            resp.getHeader().setDescripcion("Error interno del servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }
}