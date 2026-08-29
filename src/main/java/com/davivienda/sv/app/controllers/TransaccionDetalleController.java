package com.davivienda.sv.app.controllers;

import com.davivienda.sv.app.data.beans.BasicRequest;
import com.davivienda.sv.app.data.beans.BasicResponse;
import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.Response;
import com.davivienda.sv.app.dto.AplicarPagoResponse;
import com.davivienda.sv.app.dto.ConsultaEnrolamientoDTO;
import com.davivienda.sv.app.dto.Transacciones;
import com.davivienda.sv.app.entities.db2.DrefTransaccion;
import com.davivienda.sv.app.entities.db2.FacturaTransaccion;
import com.davivienda.sv.app.entities.db2.TransaccionDTO;
import com.davivienda.sv.app.services.*;
import com.davivienda.sv.app.services.datasource.ConsultaAutorizacion;
import com.davivienda.sv.app.services.validator.JWTValidator;
import com.davivienda.sv.app.util.TransactionStatus;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transacciones")
public class TransaccionDetalleController {

	private static final Logger LOGGER = LogManager.getLogger(TransaccionDetalleController.class);

	@Autowired
	DrefTransaccionRestClient drefTransaccionRestClient;
	private final TransaccionDetalleService transaccionService;
	private final ConsultaAutorizacion consultaAutorizacion;
	private final TransaccionProcesamientoService transaccionProcesamientoService;
	private final ErrorService errorService;

	@Autowired
	private JWTValidator validJWT;
	@Autowired
	JWTService jwtService;
	@Autowired
	SecurityService securityService;
	@Autowired
	ExcelGeneratorService excelGeneratorService;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(validJWT);
	}

	public TransaccionDetalleController(TransaccionDetalleService transaccionService,
			ConsultaAutorizacion consultaAutorizacion, TransaccionProcesamientoService transaccionProcesamientoService,
			ErrorService errorService) {
		this.transaccionService = transaccionService;
		this.consultaAutorizacion = consultaAutorizacion;
		this.transaccionProcesamientoService = transaccionProcesamientoService;
		this.errorService = errorService;
	}

	@PostMapping("/crear")
	public ResponseEntity<?> crearTransaccion(@Validated @RequestBody Request<TransaccionDTO> request,
			@RequestHeader("Authorization") String token, BindingResult valid) {
		if (valid.hasErrors()) {
			String message = valid.getAllErrors().get(0).getDefaultMessage();
			int codigo = Integer.parseInt(valid.getAllErrors().get(0).getCode());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Response<String>(request, new String(), codigo, message));
		}
		LOGGER.info("Payload recibido para crear transacción: " + request.getBody().toString());
		token = token.replace("Bearer ", "").trim();
		Claims claims = jwtService.verifyToken(token);
		String dni = (String) claims.get("dni");
		LOGGER.warn("Revisar petición - dni: " + dni + " perteneceUsuarioEmpresa/perteneceTarjeta");
		securityService.perteneceUsuarioEmpresa(dni, request.getBody().getUsuarioCreacion(),
				request.getBody().getEmpresa().intValue());

		securityService.perteneceTarjeta(dni, request.getBody());
		for (FacturaTransaccion item : request.getBody().getFacturas()) {
			securityService.validarMontoNpe(item.getNpe(), item.getMonto());
		}

		TransaccionDTO transaccion = transaccionService.crearTransaccion(request.getBody());
		LOGGER.info("Transacción creada con ID: " + transaccion.getIdTransaccion() + " - " + transaccion);

		consultaAutorizacion.checkTransactionAuthorization(transaccion.getIdTransaccion().intValue());

		List<TransaccionDTO> transaccionesAprobadas = new ArrayList<>();

		Response<TransaccionDTO> resp = new Response<>(request, transaccion);

		Optional<DrefTransaccion> transactionOpt = Optional
				.of(drefTransaccionRestClient.findById(transaccion.getIdTransaccion().intValue()));
		if (transactionOpt.isPresent()) {
			DrefTransaccion x = transactionOpt.get();
			transaccion.setEstado(x.getEstado());
		}

		LOGGER.info("Estado transacción ID " + transaccion.getIdTransaccion() + ": " + transaccion.getEstado());
		if (transaccion.getEstado().equals(TransactionStatus.APROBADA.getStatus())) {
			transaccionesAprobadas.add(transaccion);
		} else {
			return ResponseEntity.ok(resp);
		}

		try {
			transaccionProcesamientoService.procesarTransacciones(transaccionesAprobadas, request);
		} catch (Exception e) {
			LOGGER.error(errorService.getMensajeError(1041L) + ": " + e.getMessage(), e);
		}
		return ResponseEntity.ok(resp);
	}

	@PostMapping("/obtener/{id}")
	public ResponseEntity<Object> obtenerTransaccion(
	        @PathVariable("id") Long idTransaccion,
	        @RequestParam(name = "generarExcel", required = false, defaultValue = "false") boolean generarExcel,
	        @Validated @RequestBody Request<BasicRequest> request, 
	        @RequestHeader("Authorization") String token,
	        BindingResult valid){
		if (valid.hasErrors()) {
			String message = valid.getAllErrors().get(0).getDefaultMessage();
			int codigo = Integer.parseInt(valid.getAllErrors().get(0).getCode());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Response<String>(request, new String(), codigo, message));
		}
		token = token.replace("Bearer ", "").trim();
		Claims claims = jwtService.verifyToken(token);
		String dni = (String) claims.get("dni");
		LOGGER.warn("Revisar petición - dni: " + dni + " /obtener/{id}");
		Response<TransaccionDTO> resp = new Response<TransaccionDTO>(request, new TransaccionDTO());
		try {
			TransaccionDTO transaccion = transaccionService.obtenerTransaccion(idTransaccion);
				if(generarExcel) {
					transaccion.setTransactionDetails(excelGeneratorService.generarExcelBase64ConLogo(transaccion));
				}
				securityService.perteneceEmpresa(dni, transaccion.getEmpresa());
				resp.setBody(transaccion);
				return ResponseEntity.ok(resp);
		} catch (Exception e) {
			LOGGER.error("Error al consultar transacción con ID " + idTransaccion + ": " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
		}
	}

	@PostMapping({ "", "/" })
	public ResponseEntity<?> listarTodasLasTransacciones(
			@Validated @RequestBody Request<ConsultaEnrolamientoDTO> request,
			@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "10") Integer size, BindingResult valid,
			@RequestHeader("Authorization") String token) {
		if (valid.hasErrors()) {
			String message = valid.getAllErrors().get(0).getDefaultMessage();
			int codigo = Integer.parseInt(valid.getAllErrors().get(0).getCode());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Response<String>(request, new String(), codigo, message));
		}
		token = token.replace("Bearer ", "").trim();
		Claims claims = jwtService.verifyToken(token);
		String dni = (String) claims.get("dni");

		securityService.perteneceUsuarioEmpresa(dni, request.getBody().getUsuario(),
				request.getBody().getIdEmpresa().intValue());
		Response<Transacciones> resp = new Response<>(request, new Transacciones());
		try {
			List<TransaccionDTO> transacciones = transaccionService.listarTransacciones(request.getBody(), page, size);
			Transacciones trxs = new Transacciones();
			trxs.setTransacciones(transacciones);
			resp.setBody(trxs);
			LOGGER.info("Se encontraron " + transacciones.size() + " transacciones");
			return ResponseEntity.ok(resp);
		} catch (Exception e) {
			LOGGER.error("Error al consultar transacciones: " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
		}
	}

	@PostMapping("/{ESTADO}")
	public ResponseEntity<Object> listarTransaccionesPorEstado(
			@Validated @RequestBody Request<ConsultaEnrolamientoDTO> request,
			@RequestHeader("Authorization") String token,
			@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
			@PathVariable("ESTADO") String estado, BindingResult valid) {
		if (valid.hasErrors()) {
			String message = valid.getAllErrors().get(0).getDefaultMessage();
			int codigo = Integer.parseInt(valid.getAllErrors().get(0).getCode());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Response<String>(request, new String(), codigo, message));
		}
		TransactionStatus transactionStatus = TransactionStatus.fromPathKey(estado);

		Response<Transacciones> resp = new Response<>(request, new Transacciones());
		token = token.replace("Bearer ", "").trim();
		Claims claims = jwtService.verifyToken(token);
		String dni = (String) claims.get("dni");

		securityService.perteneceUsuarioEmpresa(dni, request.getBody().getUsuario(),
				request.getBody().getIdEmpresa().intValue());
		List<TransaccionDTO> transacciones = transaccionService.listarTransaccionesPorEstado(request.getBody(),
				transactionStatus.getStatus(), page, size);
		Transacciones trxs = new Transacciones();
		trxs.setTransacciones(transacciones);
		resp.setBody(trxs);
		LOGGER.info("Se encontraron " + transacciones.size() + " transacciones con estado: " + estado);
		return ResponseEntity.ok(resp);
	}

	@PutMapping("/aprobar")
	public ResponseEntity<?> aprobarTransacciones(@Validated @RequestBody Request<ConsultaEnrolamientoDTO> request,
			@RequestHeader("Authorization") String token, BindingResult valid) {
		if (valid.hasErrors()) {
			String message = valid.getAllErrors().get(0).getDefaultMessage();
			int codigo = Integer.parseInt(valid.getAllErrors().get(0).getCode());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Response<String>(request, new String(), codigo, message));
		}
		LOGGER.info("Iniciando aprobación de transacciones");
		token = token.replace("Bearer ", "").trim();
		Claims claims = jwtService.verifyToken(token);
		String dni = (String) claims.get("dni");

		securityService.perteneceUsuarioEmpresa(dni, request.getBody().getUsuarioAprobacion(),
				request.getBody().getIdEmpresa().intValue());

		securityService.perteneceTransacciones(dni, request.getBody().getUsuarioAprobacion(),
				request.getBody().getTransacciones().getIds(), request.getBody().getIdEmpresa());

		List<DrefTransaccion> transaccionesAprobadas = consultaAutorizacion.approveAndSaveStatusOnTransactionSigns(
				request.getBody().getTransacciones().getIds(), request.getBody().getUsuarioAprobacion());

		try {
			transaccionProcesamientoService.procesarTransacciones(transaccionesAprobadas.stream()
					.map(e -> e.getIdTransaccion().longValue()).collect(Collectors.toList()), request.getHeader());
		} catch (Exception e) {
			LOGGER.error(errorService.getMensajeError(1041L) + ": " + e.getMessage(), e);
		}

		Response<AplicarPagoResponse> resp = new Response<AplicarPagoResponse>(request, new AplicarPagoResponse(
				"Sus transcciones se estan procesando, consulte el estado de la operacion en un momento"));
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}

	@PutMapping("/{id}/aprobar")
	public ResponseEntity<?> aprobarTransaccion(@PathVariable("id") Long idTransaccion,
			@Validated @RequestBody Request<ConsultaEnrolamientoDTO> request,
			@RequestHeader("Authorization") String token, BindingResult valid) {
		if (valid.hasErrors()) {
			String message = valid.getAllErrors().get(0).getDefaultMessage();
			int codigo = Integer.parseInt(valid.getAllErrors().get(0).getCode());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Response<String>(request, new String(), codigo, message));
		}
		LOGGER.info("Iniciando aprobación de transacción con ID: " + idTransaccion);
		token = token.replace("Bearer ", "").trim();
		Claims claims = jwtService.verifyToken(token);
		String dni = (String) claims.get("dni");

		securityService.perteneceUsuarioEmpresa(dni, request.getBody().getUsuarioAprobacion(),
				request.getBody().getIdEmpresa().intValue());

		securityService.perteneceTransacciones(dni, request.getBody().getUsuarioAprobacion(),
				Arrays.asList(idTransaccion), request.getBody().getIdEmpresa());

		Response<BasicResponse> resp = new Response<>(request, new BasicResponse());
		consultaAutorizacion.saveTransactionAuthorization(idTransaccion.intValue(),
				request.getBody().getUsuarioAprobacion());
		consultaAutorizacion.checkTransactionAuthorization(idTransaccion.intValue());

		try {
			transaccionProcesamientoService.procesarTransacciones(Arrays.asList(idTransaccion), request.getHeader());
		} catch (Exception e) {
			LOGGER.error(errorService.getMensajeError(1041L) + ": " + e.getMessage(), e);
		}

		LOGGER.info("Transacción aprobada exitosamente con ID: " + idTransaccion);
		return ResponseEntity.ok(resp);
	}

	@PostMapping("/{id}/rechazar")
	public ResponseEntity<Object> rechazarTransaccion(@Validated @PathVariable("id") Long idTransaccion,
			@RequestBody Request<ConsultaEnrolamientoDTO> request, BindingResult valid,
			@RequestHeader("Authorization") String token) {
		if (valid.hasErrors()) {
			String message = valid.getAllErrors().get(0).getDefaultMessage();
			int codigo = Integer.parseInt(valid.getAllErrors().get(0).getCode());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Response<String>(request, new String(), codigo, message));
		}
		LOGGER.info("Iniciando rechazo de transacción con ID: " + idTransaccion);
		Response<BasicResponse> resp = new Response<>(request, new BasicResponse());
		try {
			token = token.replace("Bearer ", "").trim();
			Claims claims = jwtService.verifyToken(token);
			String dni = (String) claims.get("dni");

			securityService.perteneceUsuarioEmpresa(dni, request.getBody().getUsuarioAprobacion(),
					request.getBody().getIdEmpresa().intValue());

			securityService.perteneceTransacciones(dni, request.getBody().getUsuarioAprobacion(),
					Arrays.asList(idTransaccion), request.getBody().getIdEmpresa());

			transaccionService.rechazarTransaccion(idTransaccion, request.getBody().getMotivoRechazo(),
					request.getBody().getUsuarioAprobacion());

			LOGGER.info("Transacción rechazada exitosamente con ID: " + idTransaccion);
			return ResponseEntity.ok(resp);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Transacción no encontrada con ID " + idTransaccion + ": " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
		} catch (IllegalStateException e) {
			LOGGER.error("Estado inválido para rechazar transacción con ID " + idTransaccion + ": " + e.getMessage(),
					e);
			return ResponseEntity.status(HttpStatus.CONFLICT).body(resp);
		} catch (Exception e) {
			LOGGER.error("Error interno al rechazar transacción con ID " + idTransaccion + ": " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
		}
	}

	@PostMapping("/rechazar")
	public ResponseEntity<Object> rechazarTransaccion(@RequestBody Request<ConsultaEnrolamientoDTO> request,
			@RequestHeader("Authorization") String token, BindingResult valid) {
		if (valid.hasErrors()) {
			String message = valid.getAllErrors().get(0).getDefaultMessage();
			int codigo = Integer.parseInt(valid.getAllErrors().get(0).getCode());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Response<String>(request, new String(), codigo, message));
		}
		LOGGER.info("Iniciando rechazo de transacciones");
		Response<BasicResponse> resp = new Response<>(request, new BasicResponse());
		token = token.replace("Bearer ", "").trim();
		Claims claims = jwtService.verifyToken(token);
		String dni = (String) claims.get("dni");

		securityService.perteneceTransacciones(dni, request.getBody().getUsuarioAprobacion(),
				request.getBody().getTransacciones().getIds());

		transaccionService.rechazarTransacciones(request.getBody().getTransacciones().getIds(),
				request.getBody().getMotivoRechazo(), request.getBody().getUsuarioAprobacion());

		LOGGER.info("Transacciones rechazadas exitosamente");
		return ResponseEntity.ok(resp);
	}

	@GetMapping("/facturas")
	public ResponseEntity<?> buscarFacturasPorColector(@RequestParam("colector") String colector) {
		LOGGER.info("Buscando facturas para el colector: " + colector);
		try {
			List<FacturaTransaccion> facturas = transaccionService.buscarFacturasPorColector(colector);
			LOGGER.info("Se encontraron " + facturas.size() + " facturas para el colector: " + colector);
			return new ResponseEntity<>(facturas, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Error al buscar facturas para el colector " + colector + ": " + e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}