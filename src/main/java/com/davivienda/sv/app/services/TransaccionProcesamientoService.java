package com.davivienda.sv.app.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.RequestHeader;
import com.davivienda.sv.app.data.beans.Response;
import com.davivienda.sv.app.dto.ActualizarFacturaCompletaRequest;
import com.davivienda.sv.app.dto.ActualizarFacturaDescripcionRequest;
import com.davivienda.sv.app.dto.ConsultaEnrolamientoDTO;
import com.davivienda.sv.app.dto.colecturia.detalle.PeticionConsultaColectorDto;
import com.davivienda.sv.app.dto.colecturia.detalle.RespuestaConsultaColector;
import com.davivienda.sv.app.dto.colecturia.pagar.PeticionPagoFactura;
import com.davivienda.sv.app.dto.colecturia.pagar.RespuestaPagoFactura;
import com.davivienda.sv.app.dto.colecturia.validar.DatoEnLinea;
import com.davivienda.sv.app.dto.colecturia.validar.DatosValidar;
import com.davivienda.sv.app.dto.colecturia.validar.RespuestaInfoColector;
import com.davivienda.sv.app.dto.colecturia.validar.RespuestaValidarDatosPago;
import com.davivienda.sv.app.dto.colecturia.validar.ValidarDatosPago;
import com.davivienda.sv.app.entities.db2.EUser;
import com.davivienda.sv.app.entities.db2.EnrolamientoColector;
import com.davivienda.sv.app.entities.db2.FacturaTransaccion;
import com.davivienda.sv.app.entities.db2.TransaccionDTO;
import com.davivienda.sv.app.services.cysce.ConsultaColectoresServiceCysceImpl;
import com.davivienda.sv.app.services.cysce.PagoFacturaServiceCysceImpl;
import com.davivienda.sv.app.services.cysce.ValidarDatosPagoServiceCysceImpl;
import com.davivienda.sv.app.services.operaciones.RegistraWRINTASService;
import com.davivienda.sv.app.util.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.ToString;

@Service
public class TransaccionProcesamientoService {
	
	// Logger
	private static final Logger LOGGER = LogManager.getLogger(TransaccionProcesamientoService.class);

	private static final String CANAL_COLECTOR_TIGO = "MULTI";
	@Value("${colector.id}")
	private String COLECTOR_TIGO;

	// Estados
	private static final String ESTADO_PROCESANDO = "PROCESANDO";
	private static final String ESTADO_PAGADA = "PAGADA";
	private static final String ESTADO_PAGADA_CON_ERROR = "PAGADA_CON_ERROR";
	private static final String ESTADO_FACTURA_PAGADA = "PAGADA";
	private static final String ESTADO_FACTURA_ERROR = "ERROR";

	// Inyecciones
	@Autowired
	private ConsultaColectoresServiceCysceImpl consultaColectoresService;

	@Autowired
	private ValidarDatosPagoServiceCysceImpl validarDatosPagoService;

	@Autowired
	private PagoFacturaServiceCysceImpl pagoFacturaService;

	@Autowired
	RegistraWRINTASService reWrintasService;
	
	@Autowired
	DrefTransaccionRestClient drefTransaccionRestClient;
	
	@Autowired
	DreFacturaTransaccionRestClient dreFacturaTransaccionRestClient;
	
	@Autowired
	EuserRestClient euserRestClient;
	
	@Autowired
	EnrolamientoColectorRestClient enrolamientoColectorRestClient;
	
	private final ObjectMapper objectMapper = new ObjectMapper();

	// --------------------------------------------------------------------------------------------
	// PUNTOS DE ENTRADA ASÍNCRONOS (Estos son llamados por el Controller)
	// --------------------------------------------------------------------------------------------

	@Async("taskExecutor")
	public void procesarTransacciones(Request<ConsultaEnrolamientoDTO> request) {
		LOGGER.info("Inicio procesamiento asíncrono (Por Request).");

		List<TransaccionDTO> transacciones = drefTransaccionRestClient.listarTransaccionesPorEstado(request.getBody(),
				TransactionStatus.APROBADA.getStatus());

		if (transacciones == null || transacciones.isEmpty()) {
			LOGGER.info("No se encontraron transacciones aprobadas para procesar.");
			return;
		}

		// Filtrar las que vienen en el request
		List<TransaccionDTO> transaccionesFiltradas = transacciones.stream().filter(
				transaccion -> request.getBody().getTransacciones().getIds().contains(transaccion.getIdTransaccion()))
				.collect(Collectors.toList());

		// Iniciar procesamiento lineal
		procesarListaTransaccionesSecuencial(transaccionesFiltradas, request.getHeader());
	}

	@Async("taskExecutor")
	public void procesarTransacciones(List<Long> transactionsIds, RequestHeader requestHeader) {
		LOGGER.info("Inicio procesamiento asíncrono (Por IDs): " + transactionsIds);

		if (transactionsIds == null || transactionsIds.isEmpty()) return;

		List<TransaccionDTO> transacciones = drefTransaccionRestClient.listarTransaccionesPorEstadoIds(transactionsIds,
				TransactionStatus.APROBADA.getStatus());

		if (transacciones == null || transacciones.isEmpty()) {
			LOGGER.info("No se encontraron transacciones aprobadas.");
			return;
		}

		// Completar info de usuario si es necesario
		completarInformacionUsuario(requestHeader);

		// Iniciar procesamiento lineal
		procesarListaTransaccionesSecuencial(transacciones, requestHeader);
	}

	@Async("taskExecutor")
	public void procesarTransacciones(List<TransaccionDTO> transactions, Request request) {
		if (transactions == null || transactions.isEmpty()) {
			LOGGER.info("Lista de transacciones vacía.");
			return;
		}

		// Completar info de usuario
		completarInformacionUsuario(request.getHeader());

		// Iniciar procesamiento lineal
		procesarListaTransaccionesSecuencial(transactions, request.getHeader());
	}

	// --------------------------------------------------------------------------------------------
	// LÓGICA SECUENCIAL (Sin CompletableFuture, todo en el mismo hilo del @Async)
	// --------------------------------------------------------------------------------------------

	private void procesarListaTransaccionesSecuencial(List<TransaccionDTO> transacciones, RequestHeader requestHeader) {
		LOGGER.info(">>> Iniciando bucle secuencial para " + transacciones.size() + " transacciones.");

		for (int i = 0; i < transacciones.size(); i++) {
			TransaccionDTO transaccion = transacciones.get(i);
			try {
				LOGGER.info(">>> Procesando Transacción " + (i + 1) + "/" + transacciones.size() 
						+ " [ID: " + transaccion.getIdTransaccion() + "]");
				
				procesarTransaccionIndividual(transaccion, requestHeader);

			} catch (Exception e) {
				LOGGER.error("Error general procesando transacción ID: " + transaccion.getIdTransaccion(), e);
				// Intentar marcar la transacción como error si falló algo crítico fuera del flujo normal
				try {
					drefTransaccionRestClient.aprobarTransaccion(transaccion.getIdTransaccion(), "SISTEMA_AUTO", 
							"ERROR_INESPERADO: " + e.getMessage());
				} catch (Exception ex) {
					LOGGER.error("No se pudo actualizar estado de error en BD.", ex);
				}
			}
			LOGGER.info(">>> Resumen procesamiento: Total procesadas: " + transacciones.size() + ". Hora fin: " + java.time.LocalDateTime.now());
		}
		LOGGER.info(">>> Fin del procesamiento de la lista de transacciones.");
	}

	private void procesarTransaccionIndividual(TransaccionDTO transaccion, RequestHeader requestHeader) {
		// 1. Marcar como PROCESANDO
		drefTransaccionRestClient.aprobarTransaccion(transaccion.getIdTransaccion(), "SISTEMA_AUTO", ESTADO_PROCESANDO);

		// 2. Llamar Servicio 1 (Consulta Colectores) - Síncrono
		Response<RespuestaConsultaColector> respServicio1 = ejecutarServicioConsultaColector(transaccion, requestHeader);
		
		if (respServicio1 == null || respServicio1.getHeader().getCodigo() != 0) {
			String error = (respServicio1 != null) ? respServicio1.getHeader().getDescripcion() : "Respuesta Nula";
			LOGGER.error("Fallo Servicio 1 para TX " + transaccion + ": " + error);
			abortarTransaccion(transaccion, "ERROR OBTENIENDO INFORMACION DEL COLECTOR");
			return;
		}

		// 3. Llamar Servicio 2 (Validar Datos) - Síncrono
		ValidarDatosPago peticionVal = crearPeticionValidarDatosPago(transaccion, respServicio1.getBody());
		Response<RespuestaValidarDatosPago> respServicio2 = ejecutarServicioValidarDatos(peticionVal, requestHeader);

		if (respServicio2 == null || respServicio2.getHeader().getCodigo() != 0) {
			String error = (respServicio2 != null) ? respServicio2.getHeader().getDescripcion() : "Respuesta Nula";
			LOGGER.error("Fallo Servicio 2 para TX " + transaccion.getIdTransaccion() + ": " + error);
			abortarTransaccion(transaccion, "ERROR OBTENIENDO DEUDAS DESDE EL COLECTOR");
			return;
		}

		// 4. Procesar Facturas (Loop Secuencial)
		int totalFacturas = transaccion.getFacturas().size();
		int exitosas = 0;
		int errores = 0;

		for (FacturaTransaccion factura : transaccion.getFacturas()) {
			boolean resultado = procesarFactura(transaccion, factura, requestHeader, peticionVal, respServicio2.getBody(),respServicio1);
			if (resultado) {
				exitosas++;
			} else {
				errores++;
			}
		}

		// 5. Actualizar Estado Final de la Transacción
		String estadoFinal = (errores > 0) ? ESTADO_PAGADA_CON_ERROR : ESTADO_PAGADA;
		drefTransaccionRestClient.aprobarTransaccion(transaccion.getIdTransaccion(), "SISTEMA_AUTO", estadoFinal);
		
		LOGGER.info("TX " + transaccion.getIdTransaccion() + " Finalizada. Estado: " + estadoFinal 
				+ " (OK: " + exitosas + ", Error: " + errores + ")");
	}

	private boolean procesarFactura(TransaccionDTO transaccion, FacturaTransaccion factura, RequestHeader header,
			ValidarDatosPago peticionS2, RespuestaValidarDatosPago respuestaS2, Response<RespuestaConsultaColector> respServicio1) {
		
		LOGGER.info("   -> Procesando factura: " + factura);
		LOGGER.info("   -> Procesando respuestaS2: " + respuestaS2);

		try {
			// A. Validar en Datos en Línea
			if (!validarFacturaEnDatosEnLinea(factura, respuestaS2)) {
				LOGGER.warn("      Factura no encontrada en respuesta del validador.");
				marcarFacturaError(factura, "FACTURA NO ENCONTRADA EN DEUDAS OBTENIDAS DEL COLECTOR");
				return false;
			}

			// B. Ejecutar Servicio 3 (Pago)
			PeticionPagoFactura peticionPago = crearPeticionPagoFactura(transaccion, factura, peticionS2,respServicio1);
			Request<PeticionPagoFactura> requestPago = new Request<>();
			requestPago.setBody(peticionPago);
			requestPago.setHeader(header);
			LOGGER.info("   -> Procesando requestPago: " + requestPago);
			Response<RespuestaPagoFactura> respPago = pagoFacturaService.process(requestPago);

			// C. Registrar WRINTAS (Log de auditoría/actividad)
			registrarWrintas(transaccion, factura, header, respPago);

			// D. Evaluar respuesta
			if (respPago != null && respPago.getHeader().getCodigo() == 0) {
				// Verificar si hubo reversa
				String reversa = respPago.getBody().getReversaEfectuada();
				if ("S".equalsIgnoreCase(reversa)) {
					String ref = respPago.getBody().getCodigosConfirmacion().getRespuestaCargoAbonoCuenta().getConfirmacionCargoAbono();
					dreFacturaTransaccionRestClient.actualizarEstadoFacturaCompleto(factura.getIdDetalle(),
							new ActualizarFacturaCompletaRequest(ESTADO_PAGADA_CON_ERROR, ref, "Reversa efectuada"));
					return false; // Cuenta como error para el estado global
				}

				// Éxito
				String ref = respPago.getBody().getCodigosConfirmacion().getRespuestaCargoAbonoCuenta().getConfirmacionCargoAbono();
				dreFacturaTransaccionRestClient.actualizarEstadoFacturaConReferencia(factura.getIdDetalle(), ESTADO_FACTURA_PAGADA, ref);
				return true;

			} else {
				// Error en el servicio de pago
				String msg = (respPago != null) ? respPago.getHeader().getDescripcion() : "Sin respuesta";
				marcarFacturaError(factura, "ERROR_PAGO: " + msg);
				return false;
			}

		} catch (Exception e) {
			LOGGER.error("      Excepción procesando factura " + factura.getNumeroFactura(), e);
			marcarFacturaError(factura, "EXCEPCION_SISTEMA");
			return false;
		}
	}

	// --------------------------------------------------------------------------------------------
	// MÉTODOS AUXILIARES Y LLAMADAS A SERVICIOS
	// --------------------------------------------------------------------------------------------

	private Response<RespuestaConsultaColector> ejecutarServicioConsultaColector(TransaccionDTO transaccion, RequestHeader header) {
		try {
			LOGGER.info("ejecutarServicioConsultaColector::transaccion: "+transaccion.toString());
			PeticionConsultaColectorDto peticion = new PeticionConsultaColectorDto();
			peticion.setIdcolector(COLECTOR_TIGO);
			
			Request<PeticionConsultaColectorDto> req = new Request<>();
			req.setBody(peticion);
			req.setHeader(header);
			
			return consultaColectoresService.process(req);
		} catch (Exception e) {
			LOGGER.error("Error invocando Servicio Consulta Colector", e);
			return null;
		}
	}

	private Response<RespuestaValidarDatosPago> ejecutarServicioValidarDatos(ValidarDatosPago peticion, RequestHeader header) {
		LOGGER.info("ejecutarServicioValidarDatos::peticion: "+peticion.toString());
		try {
			Request<ValidarDatosPago> req = new Request<>();
			req.setBody(peticion);
			req.setHeader(header);
			return validarDatosPagoService.process(req);
		} catch (Exception e) {
			LOGGER.error("Error invocando Servicio Validar Datos", e);
			return null;
		}
	}

	private boolean validarFacturaEnDatosEnLinea(FacturaTransaccion factura, RespuestaValidarDatosPago respS2) {
		LOGGER.info("RESPS2: "+respS2.toString());
		LOGGER.info("FACTURA: "+factura.toString());
		if (respS2 == null || respS2.getInfoEnLinea() == null || respS2.getInfoEnLinea().getDatosEnLinea() == null) {
			return false;
		}
		for (DatoEnLinea dato : respS2.getInfoEnLinea().getDatosEnLinea()) {
			try {
				// Mapeo manual del JSON interno
				DatosEnLineaInfo info = objectMapper.readValue(dato.getValor(), DatosEnLineaInfo.class);
				LOGGER.info("info: "+info.toString());
				if (factura.getNpe() != null && factura.getNpe().equals(info.getNpe())) {
					return true;
				}
			} catch (Exception e) {
				LOGGER.error("Error Mapeo manual del JSON interno info, razon", e);
			}
		}
		return false;
	}

	private void marcarFacturaError(FacturaTransaccion factura, String motivo) {
		try {
			dreFacturaTransaccionRestClient.actualizarEstadoFacturaConDescripcion(factura.getIdDetalle(),
					new ActualizarFacturaDescripcionRequest(ESTADO_FACTURA_ERROR, motivo));
		} catch (Exception e) {
			LOGGER.error("Error DB al marcar factura como error", e);
		}
	}

	private void abortarTransaccion(TransaccionDTO transaccion, String motivoGlobal) {
		// Marcar todas las facturas como error
		for (FacturaTransaccion f : transaccion.getFacturas()) {
			marcarFacturaError(f, motivoGlobal);
		}
		// Marcar transacción como error
		try {
			drefTransaccionRestClient.aprobarTransaccion(transaccion.getIdTransaccion(), "SISTEMA_AUTO", ESTADO_PAGADA_CON_ERROR);
		} catch (Exception e) {
			LOGGER.error("Error DB al abortar transacción", e);
		}
	}

	private void registrarWrintas(TransaccionDTO tx, FacturaTransaccion fac, RequestHeader header, Response<RespuestaPagoFactura> resp) {
		try {
			int codResp = (resp != null) ? resp.getHeader().getCodigo() : -1;
			reWrintasService.realizarPagoDeServicio(header.getUsuario(), tx.getCuentaCargo(),
					tx.getIdColector().toString(), fac.getMonto(), header.getIp(),
					header.getIdTransaccion(), header.getIdSesion(),
					codResp, header.getFechaHora().toString(),
					(header.getNiu() != null ? header.getNiu().longValue() : 0L));
		} catch (Exception e) {
			LOGGER.error("Error registrando WRINTAS", e);
		}
	}

	private void completarInformacionUsuario(RequestHeader header) {
		try {
			if (header.getUsuario() != null) {
				Optional<EUser> eUserOpt = Optional.ofNullable(
						euserRestClient.findByUsername(header.getUsuario().trim().toUpperCase()));
				eUserOpt.ifPresent(u -> header.setNiu(u.getEusCun()));
			}
		} catch (Exception e) {
			LOGGER.warn("No se pudo completar info de usuario (NIU)", e);
		}
	}

	// --- CONSTRUCTORES DE PETICIONES (Sin cambios lógicos, solo copiados para contexto) ---

	private ValidarDatosPago crearPeticionValidarDatosPago(TransaccionDTO transaccion, RespuestaConsultaColector datosColector) {
		ValidarDatosPago peticion = new ValidarDatosPago();
		peticion.setDatosValidar(new DatosValidar());
		peticion.setRespuestaInfoColector(new RespuestaInfoColector());

		peticion.getDatosValidar().setCategoria(datosColector.getRespuestaInfoColector().getIdCategoriaColector());
		peticion.getDatosValidar().setCodigoCanal(CANAL_COLECTOR_TIGO);
		peticion.getRespuestaInfoColector().setIdColector(datosColector.getRespuestaInfoColector().getIdColector());
		peticion.getRespuestaInfoColector().setAtributos(datosColector.getRespuestaInfoColector().getAtributos());

		if (transaccion.getEnrolamientoColectorId() != null) {
			try {
				EnrolamientoColector enrolamiento = enrolamientoColectorRestClient.findById(transaccion.getEnrolamientoColectorId());
				if (enrolamiento != null) {
					Map<String, String> enrolamientoMap = new HashMap<>();
					enrolamientoMap.put(enrolamiento.getIdAtributo().toString(), enrolamiento.getValor());
					
					if (peticion.getRespuestaInfoColector().getAtributos() != null) {
						peticion.getRespuestaInfoColector().getAtributos().forEach(atributo -> {
							String val = enrolamientoMap.get(atributo.getIdAtributo());
							if (val != null) atributo.setValorAtributoPantalla(val);
						});
					}
				}
			} catch (Exception e) {
				LOGGER.warn("Error recuperando enrolamiento", e);
			}
		}
		return peticion;
	}

	private PeticionPagoFactura crearPeticionPagoFactura(TransaccionDTO transaccion, FacturaTransaccion factura, ValidarDatosPago peticionS2, Response<RespuestaConsultaColector> respServicio1) {
		PeticionPagoFactura peticion = new PeticionPagoFactura();
		peticion.setIdentificadorCanal(CANAL_COLECTOR_TIGO);
		peticion.setIdColector(COLECTOR_TIGO);
		peticion.setNombre(transaccion.getNombreColector());
		peticion.setCodCanal(CANAL_COLECTOR_TIGO);
		peticion.setNpe(factura.getNpe());
		peticion.setCuentaAbono(respServicio1.getBody().getRespuestaInfoColector().getNumeroCuentaAbono());
		peticion.setTipoCuentaAbono(transaccion.getTipoCuentaAbono());
		peticion.setCuentaCargo(transaccion.getCuentaCargo());
		peticion.setTipoCuentaCargo(transaccion.getTipoCuentaCargo());
		peticion.setMontoTotal(factura.getMonto().doubleValue());
		peticion.setMontoParcial(factura.getMonto().doubleValue());
		peticion.setUsuario(transaccion.getUsuarioCreacion());
		peticion.setOnline("0");
		peticion.setCodigotranIbs("9360");

		if (peticionS2.getRespuestaInfoColector() != null) {
			peticion.setAtributos(peticionS2.getRespuestaInfoColector().getAtributos());
		}
		return peticion;
	}

	// Clases internas para mapeo de JSON (Datos en linea)
	@JsonIgnoreProperties(ignoreUnknown = true)
	@ToString
	private static class DatosEnLineaInfo {
		private String npe;
		// Getters y setters necesarios para Jackson
		public String getNpe() { return npe; }
		public void setNpe(String npe) { this.npe = npe; }
	}
}