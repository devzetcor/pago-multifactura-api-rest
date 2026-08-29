package com.davivienda.sv.app.services.datasource;

import com.davivienda.sv.app.dto.BatchOptimizedRequest;
import com.davivienda.sv.app.dto.TransaccionesDefinicionesRequest;
import com.davivienda.sv.app.entities.db2.DetalleTransaccionAutorizacion;
import com.davivienda.sv.app.entities.db2.DrefTransaccion;
import com.davivienda.sv.app.entities.db2.TransaccionDTO;
import com.davivienda.sv.app.entities.sqlserver.*;
import com.davivienda.sv.app.repositories.sqlserver.*;
import com.davivienda.sv.app.services.BatchHelperRestClient;
import com.davivienda.sv.app.services.DetalleTransaccionAutorizacionRestClient;
import com.davivienda.sv.app.services.DrefTransaccionRestClient;
import com.davivienda.sv.app.services.EnrolamientoColectorJdbcRestClient;
import com.davivienda.sv.app.util.AppException;
import com.davivienda.sv.app.util.TransactionStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConsultaAutorizacion {
	@Autowired
	BatchHelperRestClient batchHelperRestClient; // Agregar esta dependencia

	@Autowired
	EnrolamientoColectorJdbcRestClient enrolamientoColectorJdbcRestClient;

	@Autowired
	DrefTransaccionRestClient drefTransaccionRestClient;

	@Autowired
	DetalleTransaccionAutorizacionRestClient detalleTransaccionAutorizacionRestClient;

	private static final Logger LOGGER = LogManager.getLogger(ConsultaAutorizacion.class);

	final ClienteRepo clienteRepo;
	final DefinicionAutorizacionRepo definicionAutorizacionRepo;
	final DetalleAutorizacionRepo detalleAutorizacionRepo;
	final DetalleTokenAutorizacionRepo detalleTokenAutorizacionRepo;
	final MovimientoEfectivoRepo movimientoEfectivoRepo;
	final RolUsuarioRepo rolUsuarioRepo;
	final UsuarioWCRepo usuarioWCRepo;

	public ConsultaAutorizacion(ClienteRepo clienteRepo, DefinicionAutorizacionRepo definicionAutorizacionRepo,
			DetalleAutorizacionRepo detalleAutorizacionRepo, DetalleTokenAutorizacionRepo detalleTokenAutorizacionRepo,
			MovimientoEfectivoRepo movimientoEfectivoRepo, RolUsuarioRepo rolUsuarioRepo, UsuarioWCRepo usuarioWCRepo) {
		this.clienteRepo = clienteRepo;
		this.definicionAutorizacionRepo = definicionAutorizacionRepo;
		this.detalleAutorizacionRepo = detalleAutorizacionRepo;
		this.detalleTokenAutorizacionRepo = detalleTokenAutorizacionRepo;
		this.movimientoEfectivoRepo = movimientoEfectivoRepo;
		this.rolUsuarioRepo = rolUsuarioRepo;
		this.usuarioWCRepo = usuarioWCRepo;
	}

	public void checkTransactionAuthorization(Integer idTransaction) {
		Optional<DrefTransaccion> transactionOpt = Optional.of(drefTransaccionRestClient.findById(idTransaction));
		LOGGER.info("transactionOpt.isPresent(:)" + transactionOpt.isPresent());
		if (transactionOpt.isPresent()) {
			DrefTransaccion transaction = transactionOpt.get();
			String username = transaction.getUsuarioCreacion();
			Integer client = transaction.getEmpresa();

			UsuarioWC usuarioWC = usuarioWCRepo.findByUsuarioAndCliente(username, client.longValue());
			if (usuarioWC == null) {
				throw new AppException("Usuario no encontrado");
			}
			List<RolUsuario> roles = usuarioWC.getRolesUsuarios().parallelStream().collect(Collectors.toList());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Verificando autorización ID: " + idTransaction + ". Roles usuario: " + roles);
			}
			List<Integer> rolesIds = roles.stream().map(RolUsuario::getRolWC).map(RolWC::getRol)
					.collect(Collectors.toList());

			Optional<Integer> rolOpt = rolesIds.stream().findFirst();
			LOGGER.info("rolOpt.isPresent(): " + rolOpt.isPresent());
			if (!rolOpt.isPresent())
				return;

			List<DefinicionAutorizacion> definiciones = roles.stream().findFirst().get().getRolWC().getNombre()
					.toLowerCase().trim().equals("operador")
							? definicionAutorizacionRepo.findAllByClienteAndProducto(client.longValue(), 2L)
							: definicionAutorizacionRepo.findAllByClienteAndRolAndProducto(client.longValue(),
									rolOpt.get(), 2L);
			LOGGER.info("definiciones: " + definiciones);
			List<Integer> definicionesIds = definiciones.stream().map(DefinicionAutorizacion::getDefinicionAutorizacion)
					.map(Long::intValue).collect(Collectors.toList());
			List<DetalleTransaccionAutorizacion> detallesDb2 = detalleTransaccionAutorizacionRestClient
					.findAllByTransaccionAndDefinicionesIn(idTransaction, definicionesIds);
			LOGGER.info("detallesDb2: " + detallesDb2);
			for (DefinicionAutorizacion definicion : definiciones) {
				List<RolWC> rolesNeeded = definicion.getDetalles().stream().map(e -> e.getRol())
						.collect(Collectors.toList());

				Integer rolCountNeeded = rolesNeeded.size();

				if (!rolesNeeded.isEmpty()) {
					List<DetalleTransaccionAutorizacion> currentDetails = detallesDb2.stream().filter(
							e -> e.getDefinicionAutorizacion() == definicion.getDefinicionAutorizacion().intValue())
							.collect(Collectors.toList());
					if (!currentDetails.isEmpty()) {
						List<Integer> currentRoles = currentDetails.stream()
								.map(DetalleTransaccionAutorizacion::getIdRol).collect(Collectors.toList());

						Integer rolCurrentCount = currentRoles.size();

						if (rolCountNeeded != rolCurrentCount)
							break;

						rolesNeeded.sort((a, b) -> a.getRol().compareTo(b.getRol()));
						currentRoles.sort((a, b) -> a.compareTo(b));

						Boolean isAuthorized = false;

						for (int i = 0; i < currentRoles.size(); i++) {
							Integer currentRol = currentRoles.get(i);
							RolWC rolNeeded = rolesNeeded.get(i);
							LOGGER.debug(
									"Evaluando Rol Necesario: " + rolNeeded.getRol() + " vs Rol Actual: " + currentRol);

							if (currentRol != rolNeeded.getRol())
								break;

							isAuthorized = true;
						}
						LOGGER.info("isAuthorized: " + isAuthorized);
						if (isAuthorized) {
							transaction.setEstado("APROBADA");
							drefTransaccionRestClient.save(transaction);
							break;
						}
					}

				}
			}
			return;
		}
	}

	public void saveTransactionAuthorization(Integer idTransaction, String username) {
		try {
			Optional<DrefTransaccion> transactionOpt = Optional.of(drefTransaccionRestClient.findById(idTransaction));
			LOGGER.info("Optional<DrefTransaccion>:" + transactionOpt.isPresent());
			if (transactionOpt.isPresent()) {
				DrefTransaccion transaction = transactionOpt.get();
				Integer clientId = transaction.getEmpresa();
				List<RolUsuario> rolesUsuario = rolUsuarioRepo.findAllByUsuarioAndCliente(username, clientId);

				List<DefinicionAutorizacion> definiciones = definicionAutorizacionRepo
						.findAllByClienteAndProducto(clientId.longValue(), 2L);
				LOGGER.info("definiciones:" + definiciones.size());
				List<Integer> definicionesIds = definiciones.stream()
						.map(DefinicionAutorizacion::getDefinicionAutorizacion).map(Long::intValue)
						.collect(Collectors.toList());
				LOGGER.info("definicionesIds:" + definicionesIds.size());
				List<Integer> rolesIds = rolesUsuario.stream().map(RolUsuario::getRolWC).map(RolWC::getRol)
						.collect(Collectors.toList());
				LOGGER.info("rolesIds:" + rolesIds);
				if (rolesUsuario.stream().allMatch(e -> e.getRolWC().getNombre().trim().equalsIgnoreCase("operador")))
					return;

				List<DetalleAutorizacion> detalleOpt = detalleAutorizacionRepo
						.findAllByRolesAndDefinicionAutorizacionIn(rolesIds, definicionesIds);

				LOGGER.info("detalleOpt.isEmpty():" + detalleOpt);
				if (!detalleOpt.isEmpty()) {
					for (DetalleAutorizacion detalle : detalleOpt) {
						DetalleTransaccionAutorizacion detalleTransaccionAutorizacion = new DetalleTransaccionAutorizacion(
								transaction, detalle.getRol().getRol(), username, clientId,
								detalle.getDefinicion().getDefinicionAutorizacion().intValue());
						detalleTransaccionAutorizacionRestClient.save(detalleTransaccionAutorizacion);
					}
					return;
				}
				LOGGER.warn("Fallo guardado de autorización. Usuario: " + username + " | Roles ID: " + rolesIds
						+ " | Definiciones ID encontradas: " + definicionesIds);
			} else {
				LOGGER.info("Transaccion no encontrada");
				throw new AppException("Transaccion no encontrada");
			}
		} catch (Exception e) {
			LOGGER.info("Transaccion no encontrada: " + e.getMessage());
			throw new AppException("Error al guardar la autorización de la transacción: " + e.getMessage(), 500);
		}
	}

	public List<DrefTransaccion> approveAndSaveStatusOnTransactionSigns1(List<Long> transactionsIds, String username) {
		List<DrefTransaccion> approvedTransactions = new ArrayList<>();
		List<DetalleTransaccionAutorizacion> authorizationRecords = new ArrayList<>();
		List<DrefTransaccion> transacciones = drefTransaccionRestClient.findAllByIds(transactionsIds);

		if (!transacciones.isEmpty()) {
			List<Integer> clientes = transacciones.stream().map(e -> e.getEmpresa()).collect(Collectors.toList());

			List<DefinicionAutorizacion> definiciones = definicionAutorizacionRepo.findAllByClientsAndUsername(clientes,
					username);
			List<Long> definicionesIds = definiciones.stream().map(e -> e.getDefinicionAutorizacion())
					.collect(Collectors.toList());

			List<DetalleTransaccionAutorizacion> detallesDb2 = (!transactionsIds.isEmpty()
					&& !definicionesIds.isEmpty()) ? detalleTransaccionAutorizacionRestClient
							.findAllByTransaccionesAndDefinicionesIn(new TransaccionesDefinicionesRequest(
									transactionsIds.stream().map(e -> e.intValue()).collect(Collectors.toList()),
									definicionesIds.stream().map(e -> e.intValue()).collect(Collectors.toList())))
							: new ArrayList<>();

			for (DefinicionAutorizacion definicion : definiciones) {
				DetalleTransaccionAutorizacion currentSign = detallesDb2.stream().filter(
						e -> e.getDefinicionAutorizacion().equals(definicion.getDefinicionAutorizacion().intValue()))
						.findFirst().orElse(null);

				DrefTransaccion currentTransaction = currentSign != null ? currentSign.getTransaccion()
						: transacciones.stream().filter(e -> e.getFirmas().isEmpty()).findFirst().orElse(null);

				if (currentTransaction != null && currentTransaction.getFirmas().isEmpty()) {
					Set<DetalleTransaccionAutorizacion> firmas = new HashSet<>();

					UsuarioWC currentUserWC = definicion.getCliente().getUsuarios().stream()
							.filter(e -> e.getUsuarioPk().toLowerCase().trim().equals(username)).findFirst().get();
					List<Integer> currentUserWCRoles = currentUserWC.getRolesUsuarios().stream()
							.map(e -> e.getRolWC().getRol()).collect(Collectors.toList());

					Integer currentRol = definicion.getDetalles().stream()
							.filter(e -> currentUserWCRoles.contains(e.getRol().getRol())).findFirst().get().getRol()
							.getRol();

					currentSign = new DetalleTransaccionAutorizacion(currentTransaction, currentRol, username,
							definicion.getCliente().getCliente().intValue(),
							definicion.getDefinicionAutorizacion().intValue());

					firmas.add(currentSign);
					currentTransaction.setFirmas(firmas);
				}

				Integer currentTransactionId = currentTransaction != null ? currentTransaction.getIdTransaccion() : -1;

				DrefTransaccion transaction = transacciones.stream()
						.filter(e -> e.getIdTransaccion().equals(currentTransactionId)).findFirst()
						.orElseThrow(() -> new AppException("Transaccion no encontrada"));

				Integer clientId = transaction.getEmpresa();
				List<RolUsuario> rolesUsuario = definicion.getCliente().getUsuarios().stream()
						.filter(e -> e.getUsuarioPk().trim().toLowerCase().equals(username)).findFirst().get()
						.getRolesUsuarios().stream().collect(Collectors.toList());

				if (rolesUsuario.stream().allMatch(e -> e.getRolWC().getNombre().trim().equalsIgnoreCase("operador"))) {
					return approvedTransactions;
				}

				List<DetalleAutorizacion> detalles = definicion.getDetalles().stream().collect(Collectors.toList());
				List<Integer> userRolesIds = rolesUsuario.stream().map(e -> e.getRolWC().getRol().intValue())
						.collect(Collectors.toList());
				if (!detalles.isEmpty()) {
					for (DetalleAutorizacion detalle : detalles) {
						if (userRolesIds.contains(detalle.getRol().getRol())) {
							DetalleTransaccionAutorizacion detalleTransaccionAutorizacion = new DetalleTransaccionAutorizacion(
									transaction, detalle.getRol().getRol(), username, clientId,
									detalle.getDefinicion().getDefinicionAutorizacion().intValue());
							authorizationRecords.add(detalleTransaccionAutorizacion);
						}
					}
					break;
				}

				List<RolWC> rolesNeeded = definicion.getDetalles().stream().map(e -> e.getRol())
						.collect(Collectors.toList());

				Integer rolCountNeeded = rolesNeeded.size();

				if (!rolesNeeded.isEmpty()) {
					List<DetalleTransaccionAutorizacion> currentDetails = detallesDb2.stream().filter(
							e -> e.getDefinicionAutorizacion() == definicion.getDefinicionAutorizacion().intValue())
							.collect(Collectors.toList());
					if (!currentDetails.isEmpty()) {
						List<Integer> currentRoles = currentDetails.stream()
								.map(DetalleTransaccionAutorizacion::getIdRol).collect(Collectors.toList());

						Integer rolCurrentCount = currentRoles.size();

						if (rolCountNeeded != rolCurrentCount)
							break;

						rolesNeeded.sort((a, b) -> a.getRol().compareTo(b.getRol()));
						currentRoles.sort((a, b) -> a.compareTo(b));

						Boolean isAuthorized = false;

						for (int i = 0; i < currentRoles.size(); i++) {
							Integer currentRol = currentRoles.get(i);
							RolWC rolNeeded = rolesNeeded.get(i);

							if (currentRol != rolNeeded.getRol())
								break;

							isAuthorized = true;
						}

						if (isAuthorized) {
							transaction.setEstado("APROBADA");
							approvedTransactions.add(transaction);
							break;
						}
					}

				}
			}
		}

		detalleTransaccionAutorizacionRestClient.saveAll(authorizationRecords);
		drefTransaccionRestClient.saveAll(approvedTransactions);
		return approvedTransactions;
	}

	public List<TransaccionDTO> checkIfRequiresUserSignature(String username, Integer client,
			List<TransaccionDTO> transacciones) {
		if (transacciones.isEmpty())
			return transacciones;
		Optional<UsuarioWC> currentUserOpt = usuarioWCRepo.findByUsername(username.trim().toLowerCase(), client);

		if (!currentUserOpt.isPresent())
			throw new AppException("Usuario no encontrado");

		UsuarioWC currentUser = currentUserOpt.get();

		List<TransaccionDTO> transaccionesPendientes = transacciones.stream()
				.filter(transaccion -> transaccion.getEstado().equals(TransactionStatus.PENDIENTE.getStatus()))
				.map(transaccion -> {
					transaccion.setRechazo(false);
					return transaccion;
				}).collect(Collectors.toList());

		List<TransaccionDTO> transaccionesNoPendientes = transacciones.stream()
				.filter(transaccion -> !transaccion.getEstado().equals(TransactionStatus.PENDIENTE.getStatus()))
				.map(transaccion -> {
					transaccion.setReadOnly(true);
					transaccion.setRequiresUserSignature(false);
					return transaccion;
				}).collect(Collectors.toList());

		List<Long> transaccionesRechazadasIds = transaccionesNoPendientes.stream()
				.filter(e -> e.getEstado().equals(TransactionStatus.RECHAZADA.getStatus()))
				.map(e -> e.getIdTransaccion()).collect(Collectors.toList());

		List<DrefTransaccion> transaccionesRechazadasPorUsuarioActual = transaccionesRechazadasIds.isEmpty()
				? new ArrayList<>()
				: drefTransaccionRestClient.findAllByUsernameAndTransactionIdIn(
						currentUser.getUsuarioPk().trim().toLowerCase(), transaccionesRechazadasIds);

		List<Long> transaccionesNoRechazadasIds = transaccionesNoPendientes.stream()
				.filter(e -> e.getEstado().equals(TransactionStatus.APROBADA.getStatus()))
				.map(e -> e.getIdTransaccion()).collect(Collectors.toList());

		List<Long> transaccionesPendientesIds = transaccionesPendientes.stream().map(e -> e.getIdTransaccion())
				.collect(Collectors.toList());

		transaccionesNoRechazadasIds.addAll(transaccionesPendientesIds);

		List<DetalleTransaccionAutorizacion> transaccionesAprobadasPorUsuarioActual = transaccionesNoRechazadasIds
				.isEmpty() ? new ArrayList<>()
						: detalleTransaccionAutorizacionRestClient.findAllByUsernameAndTransactionIdIn(
								currentUser.getUsuarioPk().trim().toLowerCase(), transaccionesNoRechazadasIds);

		transaccionesNoPendientes.forEach(currentTransaction -> {
			Boolean rechazo = transaccionesRechazadasPorUsuarioActual.stream()
					.anyMatch(e -> e.getIdTransaccion().equals(currentTransaction.getIdTransaccion().intValue()));
			Boolean aprobo = transaccionesAprobadasPorUsuarioActual.stream().anyMatch(e -> e.getTransaccion()
					.getIdTransaccion().equals(currentTransaction.getIdTransaccion().intValue()));

			currentTransaction.setRechazo(rechazo);
			currentTransaction.setAprobo(aprobo);
			currentTransaction.setRequiresUserSignature(false);
			currentTransaction.setReadOnly(true);
		});

		transaccionesPendientes.forEach(currentTransaction -> {
			Boolean aprobo = transaccionesAprobadasPorUsuarioActual.stream().anyMatch(e -> e.getTransaccion()
					.getIdTransaccion().equals(currentTransaction.getIdTransaccion().intValue()));
			currentTransaction.setAprobo(aprobo);
			currentTransaction.setRechazo(false);
			currentTransaction.setRequiresUserSignature(!aprobo);
			currentTransaction.setReadOnly(aprobo);
		});

		Boolean isOperatorRol = currentUser.getRolesUsuarios().stream()
				.allMatch(e -> e.getRolWC().getNombre().trim().toLowerCase().equals("operador"));

		if (isOperatorRol) {
			transaccionesPendientes.forEach(transaccion -> {
				transaccion.setRequiresUserSignature(false);
				transaccion.setReadOnly(true);
			});

			transacciones.clear();
			transacciones.addAll(transaccionesNoPendientes);
			transacciones.addAll(transaccionesPendientes);

			return transacciones;
		}

		transacciones.clear();
		transacciones.addAll(transaccionesNoPendientes);
		transacciones.addAll(transaccionesPendientes);

		return transacciones;
	}

//        @Transactional("sqlServerTransactionManager") // Asegurar transacción correcta
	public List<DrefTransaccion> approveAndSaveStatusOnTransactionSigns(List<Long> transactionsIds, String username) {

		LOGGER.info("=== INICIO APROBACIÓN MASIVA ===");
		LOGGER.info("Transacciones a procesar: " + transactionsIds.size() + ", Usuario: " + username);
		long startTime = System.currentTimeMillis();

		try {
			// PASO 1: VALIDACIONES BÁSICAS
			if (transactionsIds == null || transactionsIds.isEmpty()) {
				LOGGER.warn("Lista de transacciones vacía");
				return new ArrayList<>();
			}

			// PASO 2: OBTENER TRANSACCIONES (CON ÍNDICE OPTIMIZADO)
			LOGGER.info("Obteniendo transacciones...");
			List<DrefTransaccion> transacciones = drefTransaccionRestClient.findAllByIds(transactionsIds);
			LOGGER.info("Transacciones encontradas: {}" + transacciones.size());

			if (transacciones.isEmpty()) {
				LOGGER.warn("No se encontraron transacciones para los IDs proporcionados");
				return new ArrayList<>();
			}

			// PASO 3: FILTRAR SOLO PENDIENTES
			List<DrefTransaccion> transaccionesPendientes = transacciones.stream()
					.filter(t -> "PENDIENTE".equals(t.getEstado())).collect(Collectors.toList());

			LOGGER.info("Transacciones pendientes a procesar: {}" + transaccionesPendientes.size());

			if (transaccionesPendientes.isEmpty()) {
				LOGGER.warn("No hay transacciones en estado PENDIENTE");
				return new ArrayList<>();
			}

			// PASO 4: OBTENER EMPRESAS ÚNICAS
			List<Integer> empresas = transaccionesPendientes.stream().map(DrefTransaccion::getEmpresa).distinct()
					.collect(Collectors.toList());

			LOGGER.info("Empresas involucradas: {}" + empresas);

			// PASO 5: OBTENER DEFINICIONES DE AUTORIZACIÓN (CON ÍNDICE OPTIMIZADO)
			LOGGER.info("Obteniendo definiciones de autorización...");
			List<DefinicionAutorizacion> definiciones = definicionAutorizacionRepo.findAllByClientsAndUsername(empresas,
					username);

			LOGGER.info("Definiciones encontradas: {}" + definiciones.size());

			if (definiciones.isEmpty()) {
				LOGGER.warn("No se encontraron definiciones de autorización para usuario: {} y empresas: {}" + username
						+ empresas);
				return new ArrayList<>();
			}

			// PASO 6: PROCESAR APROBACIONES
			ApprovalResult result = processApprovals(transaccionesPendientes, definiciones, username);

			// PASO 7: GUARDAR CAMBIOS EN BATCH (OPTIMIZADO)
			LOGGER.info("Guardando cambios...");
			if (!result.newAuthorizations.isEmpty()) {
				LOGGER.info("Insertando {} nuevas autorizaciones " + result.newAuthorizations.size());
				batchHelperRestClient.insertAuthorizationsBatch(result.newAuthorizations);
			}

			if (!result.approvedTransactions.isEmpty()) {
				LOGGER.info("Actualizando {} transacciones aprobadas " + result.approvedTransactions.size());
				batchHelperRestClient.updateTransactionStatusBatch(result.approvedTransactions);
			}

			long endTime = System.currentTimeMillis();
			LOGGER.info("=== APROBACIÓN COMPLETADA ===");
			LOGGER.info("Tiempo total: {}ms " + (endTime - startTime));
			LOGGER.info("Transacciones aprobadas: {} " + result.approvedTransactions.size());

			return result.approvedTransactions;

		} catch (Exception e) {
			LOGGER.error("Error en aprobación masiva: {}" + e.getMessage(), e);
			throw new RuntimeException("Error en aprobación masiva: " + e.getMessage(), e);
		}
	}

	/**
	 * Clase auxiliar para resultados
	 */
	private static class ApprovalResult {
		List<DrefTransaccion> approvedTransactions = new ArrayList<>();
		List<DetalleTransaccionAutorizacion> newAuthorizations = new ArrayList<>();
	}

	private ApprovalResult processApprovals(List<DrefTransaccion> transacciones,
			List<DefinicionAutorizacion> definiciones, String username) {

		ApprovalResult result = new ApprovalResult();

		// Obtener IDs para consulta batch
		List<Integer> transactionIds = transacciones.stream().map(DrefTransaccion::getIdTransaccion)
				.collect(Collectors.toList());

		List<Integer> definitionIds = definiciones.stream().map(d -> d.getDefinicionAutorizacion().intValue())
				.collect(Collectors.toList());

		// CONSULTA OPTIMIZADA: Una sola query para todas las autorizaciones existentes
		LOGGER.info("Consultando autorizaciones existentes...");
		List<DetalleTransaccionAutorizacion> autorizacionesExistentes = detalleTransaccionAutorizacionRestClient
				.findAuthorizationsBatchOptimized(new BatchOptimizedRequest(transactionIds, definitionIds));

		LOGGER.info("Autorizaciones existentes encontradas: {}" + autorizacionesExistentes.size());

		// Agrupar autorizaciones por transacción para búsqueda rápida
		Map<Integer, List<DetalleTransaccionAutorizacion>> authsByTransaction = autorizacionesExistentes.stream()
				.collect(Collectors.groupingBy(auth -> auth.getTransaccion().getIdTransaccion()));

		// Procesar cada transacción
		for (DrefTransaccion transaccion : transacciones) {
			LOGGER.debug("Procesando transacción: {} " + transaccion.getIdTransaccion());

			if (shouldApproveTransaction(transaccion, definiciones, username, authsByTransaction,
					result.newAuthorizations)) {

				// Marcar como aprobada
				transaccion.setEstado("APROBADA");
				transaccion.setFechaAprobacion(LocalDateTime.now());
				transaccion.setUsuarioAprobacion(username);
				result.approvedTransactions.add(transaccion);

				LOGGER.debug("Transacción {} marcada para aprobación " + transaccion.getIdTransaccion());
			}
		}

		LOGGER.info("Resultado: {} transacciones para aprobar, {} nuevas autorizaciones"
				+ result.approvedTransactions.size() + result.newAuthorizations.size());

		return result;
	}

	private boolean shouldApproveTransaction(DrefTransaccion transaccion, List<DefinicionAutorizacion> definiciones,
			String username, Map<Integer, List<DetalleTransaccionAutorizacion>> existingAuths,
			List<DetalleTransaccionAutorizacion> newAuthorizations) {

		Integer empresaId = transaccion.getEmpresa();

		// Filtrar definiciones para esta empresa
		Set<DefinicionAutorizacion> definicionesEmpresa = definiciones.stream()
				.filter(def -> def.getCliente().getCliente().equals(empresaId.longValue())).collect(Collectors.toSet());

		if (definicionesEmpresa.isEmpty()) {
			LOGGER.debug("No hay definiciones para empresa: {}" + empresaId);
			return false;
		}

		for (DefinicionAutorizacion definicion : definicionesEmpresa) {

			// VERIFICAR SI ES OPERADOR (APROBACIÓN AUTOMÁTICA)
			if (isOperatorUser(definicion, username)) {
				LOGGER.debug("Usuario {} es operador, aprobando automáticamente" + username);
				return true;
			}

			// OBTENER ROLES DEL USUARIO
			List<Integer> userRoles = getUserRoles(definicion, username);
			if (userRoles.isEmpty()) {
				continue;
			}

			// OBTENER ROLES REQUERIDOS
			List<Integer> requiredRoles = definicion.getDetalles().stream().map(d -> d.getRol().getRol())
					.collect(Collectors.toList());

			if (requiredRoles.isEmpty()) {
				continue;
			}

			// CREAR NUEVAS AUTORIZACIONES PARA ROLES VÁLIDOS
			for (DetalleAutorizacion detalle : definicion.getDetalles()) {
				if (userRoles.contains(detalle.getRol().getRol())) {
					DetalleTransaccionAutorizacion nuevaAuth = new DetalleTransaccionAutorizacion(transaccion,
							detalle.getRol().getRol(), username, empresaId,
							definicion.getDefinicionAutorizacion().intValue());
					newAuthorizations.add(nuevaAuth);
				}
			}

			// VERIFICAR SI SE COMPLETARON TODAS LAS AUTORIZACIONES
			List<DetalleTransaccionAutorizacion> currentAuths = existingAuths
					.getOrDefault(transaccion.getIdTransaccion(), new ArrayList<>());

			// Combinar autorizaciones existentes + nuevas para esta transacción
			List<Integer> allAuthorizedRoles = new ArrayList<>();

			// Roles ya autorizados
			currentAuths.stream().filter(
					auth -> auth.getDefinicionAutorizacion().equals(definicion.getDefinicionAutorizacion().intValue()))
					.forEach(auth -> allAuthorizedRoles.add(auth.getIdRol()));

			// Roles de las nuevas autorizaciones
			newAuthorizations.stream().filter(
					auth -> auth.getTransaccion().getIdTransaccion().equals(transaccion.getIdTransaccion()) && auth
							.getDefinicionAutorizacion().equals(definicion.getDefinicionAutorizacion().intValue()))
					.forEach(auth -> allAuthorizedRoles.add(auth.getIdRol()));

			// Verificar si todos los roles requeridos están cubiertos
			if (allAuthorizedRoles.containsAll(requiredRoles)) {
				LOGGER.debug(
						"Todos los roles requeridos autorizados para transacción: {}" + transaccion.getIdTransaccion());
				return true;
			} else {
				LOGGER.debug("Transacción " + transaccion.getIdTransaccion() + " NO aprobada con definición "
						+ definicion.getDefinicionAutorizacion() + ". Roles requeridos: " + requiredRoles
						+ ", Roles acumulados: " + allAuthorizedRoles);
			}
		}

		return false;
	}

	/**
	 * Verifica si es usuario operador (LÓGICA SIMPLIFICADA)
	 */
	private boolean isOperatorUser(DefinicionAutorizacion definicion, String username) {
		try {
			return definicion.getCliente().getUsuarios().stream()
					.filter(u -> u.getUsuarioPk().trim().equalsIgnoreCase(username)).findFirst()
					.map(usuario -> usuario.getRolesUsuarios().stream()
							.allMatch(rol -> "operador".equalsIgnoreCase(rol.getRolWC().getNombre().trim())))
					.orElse(false);
		} catch (Exception e) {
			LOGGER.warn("Error verificando operador para: {}" + username + e);
			return false;
		}
	}

	/**
	 * Obtiene roles del usuario (LÓGICA SIMPLIFICADA)
	 */
	private List<Integer> getUserRoles(DefinicionAutorizacion definicion, String username) {
		try {
			return definicion.getCliente().getUsuarios().stream()
					.filter(u -> u.getUsuarioPk().trim().equalsIgnoreCase(username)).findFirst()
					.map(usuario -> usuario.getRolesUsuarios().stream().map(rol -> rol.getRolWC().getRol())
							.collect(Collectors.toList()))
					.orElse(new ArrayList<>());
		} catch (Exception e) {
			LOGGER.warn("Error obteniendo roles para: {}" + username + e);
			return new ArrayList<>();
		}
	}

}
