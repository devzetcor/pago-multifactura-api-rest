package com.davivienda.sv.app.controllers;

import com.davivienda.sv.app.data.beans.BasicResponse;
import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.Response;
import com.davivienda.sv.app.dto.*;
import com.davivienda.sv.app.dto.colecturia.detalle.PeticionConsultaColectorDto;
import com.davivienda.sv.app.dto.colecturia.pagar.PeticionPagoFactura;
import com.davivienda.sv.app.dto.colecturia.pagar.RespuestaPagoFactura;
import com.davivienda.sv.app.dto.colecturia.validar.RespuestaValidarDatosPago;
import com.davivienda.sv.app.entities.db2.EnrolamientoColector;
import com.davivienda.sv.app.services.EnrolamientoColectorService;
import com.davivienda.sv.app.services.JWTService;
import com.davivienda.sv.app.services.SecurityService;
import com.davivienda.sv.app.services.cysce.ConsultaColectoresServiceCysceImpl;
import com.davivienda.sv.app.services.cysce.ListaColectoresServiceCysceImpl;
import com.davivienda.sv.app.services.cysce.PagoFacturaServiceCysceImpl;
import com.davivienda.sv.app.services.cysce.ValidarDatosPagoServiceCysceImpl;
import com.davivienda.sv.app.services.validator.JWTValidator;
import io.jsonwebtoken.Claims;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colecturia")
public class ColecturiaController {
	private static final Logger LOGGER = LogManager.getLogger(ColecturiaController.class);
	@Autowired
	ConsultaColectoresServiceCysceImpl consultaColectoresServiceImpl;
	@Autowired
	ListaColectoresServiceCysceImpl listaColectoresServiceCysceImpl;
	@Autowired
	ValidarDatosPagoServiceCysceImpl validarDatosPagoServiceCysceImpl;
	@Autowired
	PagoFacturaServiceCysceImpl pagoFacturaServiceCysceImpl;
	@Autowired
	EnrolamientoColectorService enrolamientoColectorService;
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

	@PostMapping("/editarDatosEnrolamiento")
	public ResponseEntity<?> editarColectores(
		@Validated @RequestBody Request<EnrolamientoColectorListDTO> request
		,@RequestHeader("Authorization") String token
		,BindingResult valid
	) {
		if (valid.hasErrors()) {
			String message = valid.getAllErrors().get(0).getDefaultMessage();
			int codigo = Integer.parseInt(valid.getAllErrors().get(0).getCode());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Response<String>(request, new String(), codigo, message));
		}
		token=token.replace("Bearer ", "").trim();
    	Claims claims=jwtService.verifyToken(token);
    	String dni=(String) claims.get("dni");
    	
    	securityService.perteneceEmpresaListaEnrolamiento(
    			dni
    			, request.getBody().getEnrolamientos());
    	
		List<EnrolamientoColectorDTO> enrolamientos = enrolamientoColectorService.editarColectores(request.getBody().getEnrolamientos());

		Response<List<EnrolamientoColectorDTO>> resp = new Response<>(request);
		resp.setBody(enrolamientos);
		return new ResponseEntity<>(resp, HttpStatus.CREATED);
	}

	@PostMapping(path = "/eliminarDatosEnrolamiento", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> eliminarColectores(
			@Validated @RequestBody Request<List<Integer>> request
			,@RequestHeader("Authorization") String token,
			BindingResult valid) {

		if (valid.hasErrors()) {
			String message = valid.getAllErrors().get(0).getDefaultMessage();
			int codigo = Integer.parseInt(valid.getAllErrors().get(0).getCode());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Response<String>(request, new String(), codigo, message));
		}
		token=token.replace("Bearer ", "").trim();
    	Claims claims=jwtService.verifyToken(token);
    	String dni=(String) claims.get("dni");
		securityService.perteneceEmpresaListaEnrolamientoIDs(
    			dni
    			, request.getBody());
		enrolamientoColectorService.eliminarColectores(request.getBody());

		Response<BasicResponse> resp = new Response<>(request, new BasicResponse());
		return new ResponseEntity<>(resp, HttpStatus.CREATED);
	}

	@PostMapping(path = "/listaColectores", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listaColectores(@Validated @RequestBody Request<PeticionListaColectoresDto> request, BindingResult valid) {
		if (valid.hasErrors()) {
			String message = valid.getAllErrors().get(0).getDefaultMessage();
			int codigo = Integer.parseInt(valid.getAllErrors().get(0).getCode());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Response<String>(request, new String(), codigo, message));
		}
		Response<com.davivienda.sv.app.dto.colecturia.lista.RespuestaConsultaColector> respuestaListaColectoresDto = listaColectoresServiceCysceImpl
				.process(request);
		if (respuestaListaColectoresDto.getHeader().getCodigo() == 0) {
			return ResponseEntity.ok(respuestaListaColectoresDto);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaListaColectoresDto);
		}
	}

	@PostMapping(path = "/consultaColector", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> consultaColectores(@Validated @RequestBody Request<PeticionConsultaColectorDto> request, BindingResult valid) {
		if (valid.hasErrors()) {
			String message = valid.getAllErrors().get(0).getDefaultMessage();
			int codigo = Integer.parseInt(valid.getAllErrors().get(0).getCode());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Response<String>(request, new String(), codigo, message));
		}
		Response<com.davivienda.sv.app.dto.colecturia.detalle.RespuestaConsultaColector> respuestaConsultaColectoresDto = consultaColectoresServiceImpl
				.process(request);
		if (respuestaConsultaColectoresDto.getHeader().getCodigo() == 0) {
			return ResponseEntity.ok(respuestaConsultaColectoresDto);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaConsultaColectoresDto);
		}
	}

	@PostMapping(path = "/validarDatosPago", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> validarDatosPago(@Validated
			@RequestBody Request<com.davivienda.sv.app.dto.colecturia.validar.ValidarDatosPago> request
			,@RequestHeader("Authorization") String token
			, BindingResult valid) {
		if (valid.hasErrors()) {
			String message = valid.getAllErrors().get(0).getDefaultMessage();
			int codigo = Integer.parseInt(valid.getAllErrors().get(0).getCode());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Response<String>(request, new String(), codigo, message));
		}
		token=token.replace("Bearer ", "").trim();
    	Claims claims=jwtService.verifyToken(token);
    	String dni=(String) claims.get("dni");
		securityService.perteneceIdentificador(dni,request.getBody().getRespuestaInfoColector().getAtributos(),Long.parseLong( request.getBody().getRespuestaInfoColector().getIdColector()));
		Response<RespuestaValidarDatosPago> respuestaValidarDatosPagoDto = validarDatosPagoServiceCysceImpl
				.process(request);

		if (respuestaValidarDatosPagoDto.getHeader().getCodigo() == 0) {
			return ResponseEntity.ok(respuestaValidarDatosPagoDto);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaValidarDatosPagoDto);
		}
	}

	@PostMapping(path = "/pagoFactura", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> pagoFactura(@Validated @RequestBody Request<PeticionPagoFactura> request,
			
			BindingResult valid) {
		if (valid.hasErrors()) {
			String message = valid.getAllErrors().get(0).getDefaultMessage();
			int codigo = Integer.parseInt(valid.getAllErrors().get(0).getCode());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Response<String>(request, new String(), codigo, message));
		}
		
		Response<RespuestaPagoFactura> respuestaPagoFacturaDto = pagoFacturaServiceCysceImpl.process(request);
		if (respuestaPagoFacturaDto.getHeader().getCodigo() == 0) {
			return ResponseEntity.ok(respuestaPagoFacturaDto);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaPagoFacturaDto);
		}
	}

	@PostMapping("/guardarDatosEnrolamiento")
	public ResponseEntity<?> guardarEnrolamientos(@Validated  @RequestBody Request<EnrolamientoColectorListDTO> request
			,@RequestHeader("Authorization") String token, BindingResult valid) {
		if (valid.hasErrors()) {
			String message = valid.getAllErrors().get(0).getDefaultMessage();
			int codigo = Integer.parseInt(valid.getAllErrors().get(0).getCode());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Response<String>(request, new String(), codigo, message));
		}
		token=token.replace("Bearer ", "").trim();
    	Claims claims=jwtService.verifyToken(token);
    	String dni=(String) claims.get("dni");
    	
    	securityService.perteneceEmpresaLista(
    			dni
    			, request.getBody().getEnrolamientos());
		List<EnrolamientoColector> enrolamientosGuardados = enrolamientoColectorService
				.guardarEnrolamientos(request.getBody().getEnrolamientos());
		Response<List<EnrolamientoColector>> resp = new Response<>(request, enrolamientosGuardados);
		return new ResponseEntity<>(resp, HttpStatus.CREATED);
	}

	@PostMapping("/consultaDatosEnrolamiento")
	public ResponseEntity<?> consultarEnrolamientos(@Validated  @RequestBody Request<ConsultaEnrolamientoDTO> request,@RequestHeader("Authorization") String token, BindingResult valid) {
		Response<RespuetaEnrolamientoColector> respuesta = new Response<>(request, new RespuetaEnrolamientoColector());
		if (valid.hasErrors()) {
			String message = valid.getAllErrors().get(0).getDefaultMessage();
			int codigo = Integer.parseInt(valid.getAllErrors().get(0).getCode());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Response<String>(request, new String(), codigo, message));
		}
		LOGGER.info("consultaDatosEnrolamiento->" + request.getBody());
		token=token.replace("Bearer ", "").trim();
    	Claims claims=jwtService.verifyToken(token);
    	String dni=(String) claims.get("dni");
    	
    	securityService.perteneceEmpresa(
    			dni
    			, request.getBody().getIdEmpresa());
		List<EnrolamientoColectorDTO> resultado = enrolamientoColectorService
				.consultarPorEmpresaYColector(request.getBody().getIdEmpresa(), request.getBody().getIdColector());
		respuesta.getBody().setAtributos(resultado);
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}

}
