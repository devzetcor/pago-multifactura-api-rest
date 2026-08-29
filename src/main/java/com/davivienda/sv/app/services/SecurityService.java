package com.davivienda.sv.app.services;

import com.davivienda.sv.app.dto.EnrolamientoColectorDTO;
import com.davivienda.sv.app.dto.colecturia.detalle.AtributoColectorFull;
import com.davivienda.sv.app.entities.db2.DrefTransaccion;
import com.davivienda.sv.app.entities.db2.EnrolamientoColector;
import com.davivienda.sv.app.entities.db2.TransaccionDTO;
import com.davivienda.sv.app.entities.sqlserver.Cliente;
import com.davivienda.sv.app.entities.sqlserver.Cuenta;
import com.davivienda.sv.app.entities.sqlserver.UsuarioWC;
import com.davivienda.sv.app.repositories.sqlserver.ClienteRepo;
import com.davivienda.sv.app.repositories.sqlserver.CuentaRepository;
import com.davivienda.sv.app.repositories.sqlserver.UsuarioWCRepo;
import com.davivienda.sv.app.util.AppException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SecurityService {

	private static final Logger LOGGER = LogManager.getLogger(SecurityService.class);

	private static final String MSG_DATOS_INCOMPLETOS = "Datos incompletos, revisar la petición";
	private static final String MSG_NO_PERTENECE = "Recurso no encontrado o no pertenece a la sesion activa";

	@Autowired
	UsuarioWCRepo findByUsername;
	@Autowired
	ClienteRepo ClienteRepo;
	@Autowired
	EnrolamientoColectorService enrolamientoColectorService;
	@Autowired
	private EnrolamientoColectorRestClient enrolamientoColectorRestClient;
	@Autowired
	private CuentaRepository cuentaRepository;
	@Autowired
	DrefTransaccionRestClient drefTransaccionRestClient;

	// ─── Helpers privados ────────────────────────────────────────────────────────

	/** Lanza AppException 403 con el mensaje indicado. */
	private void forbidden(String msg) {
		throw new AppException(msg, 403);
	}

	/** Valida que ningún string sea nulo/vacío; lanza 403 si falla. */
	private void requireNonBlank(String... values) {
		for (String v : values) {
			if (v == null || v.isEmpty()) {
				forbidden(MSG_DATOS_INCOMPLETOS);
			}
		}
	}

	/**
	 * Busca los clientes por DNI y lanza 403 si no se encuentran.
	 */
	private List<Cliente> getClientesOrFail(String dni) {
		List<Cliente> clientes = ClienteRepo.findByNumeroDocumento(dni);
		if (clientes == null || clientes.isEmpty()) {
			forbidden(MSG_NO_PERTENECE);
		}
		return clientes;
	}

	/**
	 * Devuelve los ids de empresa (getCliente) de una lista de clientes.
	 */
	private List<Long> extractIdsEmpresas(List<Cliente> clientes) {
		return clientes.stream().map(Cliente::getCliente).collect(Collectors.toList());
	}

	/**
	 * Verifica que idEmpresa esté entre los ids de la lista de clientes.
	 */
	private boolean empresaPertenece(List<Cliente> clientes, Long idEmpresa) {
		return clientes.stream().map(Cliente::getCliente).anyMatch(id -> id.intValue() == idEmpresa.intValue());
	}

	// ─── Métodos públicos ────────────────────────────────────────────────────────

	public void perteneceUsuarioEmpresa(String dni, String usuario, Integer empresa) {
		if (dni == null || dni.isEmpty() || usuario == null || usuario.isEmpty() || empresa == null || empresa < 0) {
			LOGGER.warn("Datos incompletos, revisar la petición: " + dni + " - " + usuario + " - " + empresa);
			forbidden(MSG_DATOS_INCOMPLETOS);
		}

		Optional<UsuarioWC> usr = findByUsername.findByUsername(usuario, empresa);
		if (!usr.isPresent()) {
			LOGGER.error("Recurso no pertenece a la sesion activa: " + dni + " - " + usuario + " - " + empresa);
			forbidden(MSG_NO_PERTENECE);
		}

		if (!usr.get().getNumeroDocumento().trim().equals(dni)) {
			LOGGER.error("Recurso no pertenece a la sesion activa: " + dni + " - " + usuario + " - " + empresa);
			throw new AppException("Recurso no pertenece a la sesion activa", 403);
		}
	}

	public void perteneceUsuario(String dnijwt, String dni) {
		// Nota: la condición original comprueba dni dos veces (dni == null), se
		// conserva el comportamiento
		if (dni == null || dni.isEmpty()) {
			LOGGER.warn("Datos incompletos, revisar la petición: " + dnijwt);
			forbidden(MSG_DATOS_INCOMPLETOS);
		}

		if (!dnijwt.equals(dni)) {
			LOGGER.error("Recurso no pertenece a la sesion activa: " + dnijwt + " - " + dni);
			forbidden(MSG_NO_PERTENECE);
		}
	}

	public void perteneceEmpresa(String dni, Long empresa) {
		if (dni == null || dni.isEmpty() || empresa == null || empresa < 0) {
			LOGGER.warn("Datos incompletos, revisar la petición: " + dni + " - " + empresa);
			forbidden(MSG_DATOS_INCOMPLETOS);
		}

		List<Cliente> clientes = getClientesOrFail(dni);

		if (!empresaPertenece(clientes, empresa)) {
			LOGGER.error("Recurso no pertenece a la sesion activa: " + dni + " - " + empresa);
			forbidden(MSG_NO_PERTENECE);
		}
	}

	public void perteneceEmpresaLista(String dni, List<EnrolamientoColectorDTO> empresa) {
		if (dni == null || dni.isEmpty() || empresa == null || empresa.isEmpty()) {
			LOGGER.warn("Datos incompletos, revisar la petición: " + dni + " - " + empresa);
			forbidden(MSG_DATOS_INCOMPLETOS);
		}

		List<Cliente> clientes = getClientesOrFail(dni);

		for (EnrolamientoColectorDTO item : empresa) {
			long idItem = item.getIdEmpresa();
			if (!empresaPertenece(clientes, idItem)) {
				LOGGER.error("Recurso no pertenece a la sesion activa: " + dni);
				forbidden(MSG_NO_PERTENECE);
			}
		}
	}

	public void perteneceEmpresaListaEnrolamiento(String dni, List<EnrolamientoColectorDTO> empresa) {
		if (dni == null || dni.isEmpty() || empresa == null || empresa.isEmpty()) {
			LOGGER.warn("Datos incompletos, revisar la petición: " + dni + " - " + empresa);
			forbidden(MSG_DATOS_INCOMPLETOS);
		}
		List<Integer> idsRequest = empresa.stream().map(e -> e.getId().intValue()).collect(Collectors.toList());
this.perteneceEmpresaListaEnrolamientoIDs(dni, idsRequest);
//		List<Cliente> clientes = getClientesOrFail(dni);
//		LOGGER.warn("CLIENTES:" + dni + " - " + clientes);
//		for (EnrolamientoColectorDTO item : empresa) {
//			if (!empresaPertenece(clientes, item.getIdEmpresa())) {
//				LOGGER.error("Recurso no pertenece a la sesion activa: " + dni);
//				forbidden(MSG_NO_PERTENECE);
//			}
//		}
//
//		List<EnrolamientoColectorDTO> list = enrolamientoColectorService
//				.consultarPorEmpresaYColector(empresa.get(0).getIdEmpresa(), empresa.get(0).getIdColector());
//
//		// Convertir los IDs del request a un Set para búsqueda O(1)
//		java.util.Set<Integer> idsRequest = empresa.stream().map(e -> e.getId().intValue()).collect(Collectors.toSet());
//
//		for (EnrolamientoColectorDTO item : list) {
//			if (!idsRequest.contains(item.getId().intValue())) {
//				LOGGER.error("Recurso no pertenece a la sesion activa: " + dni);
//				forbidden(MSG_NO_PERTENECE);
//			}
//		}
	}

	public void perteneceEmpresaListaEnrolamientoIDs(String dni, List<Integer> empresa) {
		if (dni == null || dni.isEmpty() || empresa == null || empresa.isEmpty()) {
			LOGGER.warn("Datos incompletos, revisar la petición: " + dni + " - " + empresa);
			forbidden(MSG_DATOS_INCOMPLETOS);
		}

		List<Cliente> clientes = getClientesOrFail(dni);
		LOGGER.warn("CLIENTES: " + dni + " - " + clientes);
		List<EnrolamientoColector> enrolamientos = enrolamientoColectorRestClient.findAllByIds(empresa);
		for (EnrolamientoColector item : enrolamientos) {
			if (!empresaPertenece(clientes, item.getIdEmpresa())) {
				LOGGER.error("Recurso no pertenece a la sesion activa: " + dni);
				forbidden(MSG_NO_PERTENECE);
			}
		}
	}

	public void perteneceTarjeta(String dni, TransaccionDTO transaccionDTO) {
		List<Cuenta> cuentas = cuentaRepository.findCuentasByClienteAndUsuario(transaccionDTO.getEmpresa(),
				transaccionDTO.getUsuarioCreacion());

		boolean existeCuenta = cuentas.stream()
				.anyMatch(c -> c.getCuenta().trim().equals(transaccionDTO.getCuentaCargo().trim()));

		if (!existeCuenta) {
			forbidden(MSG_NO_PERTENECE);
		}
	}

	public void perteneceIdentificador(String dni, List<AtributoColectorFull> atributos, Long idColector) {
		// Buscar el primer atributo que pide pantalla
		AtributoColectorFull atributoRequerido = atributos.stream().filter(a -> "S".equals(a.getPedirPantalla()))
				.findFirst().orElse(null);

		if (atributoRequerido == null || atributoRequerido.getIdAtributo() == null
				|| atributoRequerido.getValorAtributoPantalla() == null) {
			LOGGER.error("No se encontró atributo requerido en la lista para el DNI: " + dni);
			throw new AppException("Atributo requerido no encontrado", 400);
		}

		final String key = atributoRequerido.getIdAtributo();
		final String valor = atributoRequerido.getValorAtributoPantalla();

		List<Cliente> clientes = ClienteRepo.findByNumeroDocumento(dni);
		List<Long> idsClientes = extractIdsEmpresas(clientes);

		List<EnrolamientoColector> enrolamientos = new ArrayList<>();
		for (Long idCliente : idsClientes) {
			enrolamientos.addAll(enrolamientoColectorRestClient.findByIdEmpresaAndIdColector(idCliente, idColector));
		}

		boolean pertenece = enrolamientos.stream().anyMatch(e -> Objects.equals(e.getIdAtributo(), Long.valueOf(key))
				&& Objects.equals(e.getValor(), valor) && Objects.equals(e.getIdColector(), idColector));

		if (!pertenece) {
			LOGGER.error("Recurso no pertenece a la sesión activa. DNI: " + dni + ", Colector: " + idColector);
			forbidden(MSG_NO_PERTENECE);
		}
	}

	public void perteneceTransacciones(String dni, String usuario, List<Long> idsTransacciones) {
		LOGGER.info("[perteneceTransacciones] Inicio - dni: " + dni + ", usuario: " + usuario + ", cantidad IDs: "
				+ (idsTransacciones != null ? idsTransacciones.size() : "null"));

		if (dni == null || dni.isEmpty() || usuario == null || usuario.isEmpty() || idsTransacciones == null
				|| idsTransacciones.isEmpty()) {
			LOGGER.warn("[perteneceTransacciones] Datos incompletos - dni: " + dni + ", usuario: " + usuario + ", ids: "
					+ idsTransacciones);
			forbidden(MSG_DATOS_INCOMPLETOS);
		}

		LOGGER.info("[perteneceTransacciones] Buscando clientes por número de documento: " + dni);
		List<Cliente> clientes = ClienteRepo.findByNumeroDocumento(dni);
		if (clientes == null || clientes.isEmpty()) {
			LOGGER.warn("[perteneceTransacciones] No se encontraron clientes para el DNI: " + dni);
			forbidden(MSG_NO_PERTENECE);
		}

		List<Long> idsEmpresas = extractIdsEmpresas(clientes);
		LOGGER.info("[perteneceTransacciones] Empresas asociadas al DNI: " + idsEmpresas);

		LOGGER.info("[perteneceTransacciones] Consultando transacciones asociadas a las empresas del usuario");
		List<DrefTransaccion> transaccionesDelUsuario = drefTransaccionRestClient.findAllByIds(idsTransacciones);
		int totalTx = transaccionesDelUsuario != null ? transaccionesDelUsuario.size() : 0;
		LOGGER.info("[perteneceTransacciones] Se obtuvieron " + totalTx + " transaccion(es) asociadas al usuario");

		if (transaccionesDelUsuario == null || transaccionesDelUsuario.isEmpty()) {
			LOGGER.warn("[perteneceTransacciones] No se encontraron transacciones para las empresas: " + idsEmpresas);
			forbidden(MSG_NO_PERTENECE);
		}

		// Set para búsqueda O(1) en lugar de List.contains O(n)
		java.util.Set<Integer> empresasEnTransacciones = transaccionesDelUsuario.stream()
				.map(DrefTransaccion::getEmpresa).collect(Collectors.toSet());
		LOGGER.info("[perteneceTransacciones] IDs de empresas en transacciones: " + empresasEnTransacciones);

		LOGGER.info("[perteneceTransacciones] Validando que cada ID enviado pertenezca al usuario");
		for (DrefTransaccion idTransaccion : transaccionesDelUsuario) {
			LOGGER.info("[perteneceTransacciones] Verificando ID transacción: " + idTransaccion);
			if (!idsEmpresas.contains(idTransaccion.getEmpresa().longValue())) {
				LOGGER.error("[perteneceTransacciones] ID transacción: " + idTransaccion
						+ " no pertenece al usuario - dni: " + dni);
				forbidden(MSG_NO_PERTENECE);
			}
			LOGGER.info("[perteneceTransacciones] ID transacción: " + idTransaccion + " validado correctamente");
		}

		LOGGER.info("[perteneceTransacciones] Validación completada exitosamente para " + idsTransacciones.size()
				+ " transaccion(es)");
	}

	public void perteneceTransacciones(String dni, String usuario, List<Long> idsTransacciones, Long empresa) {
		LOGGER.info("[perteneceTransacciones] Inicio - dni: " + dni + ", usuario: " + usuario + ", cantidad IDs: "
				+ (idsTransacciones != null ? idsTransacciones.size() : "null"));

		if (dni == null || dni.isEmpty() || usuario == null || usuario.isEmpty() || idsTransacciones == null
				|| idsTransacciones.isEmpty()) {
			LOGGER.warn("[perteneceTransacciones] Datos incompletos - dni: " + dni + ", usuario: " + usuario + ", ids: "
					+ idsTransacciones);
			forbidden(MSG_DATOS_INCOMPLETOS);
		}

		LOGGER.info("[perteneceTransacciones] Buscando clientes por número de documento: " + dni);

		LOGGER.info("[perteneceTransacciones] Empresas asociadas al DNI: " + empresa);

		LOGGER.info("[perteneceTransacciones] Consultando transacciones asociadas a las empresas del usuario");
		List<DrefTransaccion> transaccionesDelUsuario = drefTransaccionRestClient.findAllByIds(idsTransacciones);
		int totalTx = transaccionesDelUsuario != null ? transaccionesDelUsuario.size() : 0;
		LOGGER.info("[perteneceTransacciones] Se obtuvieron " + totalTx + " transaccion(es) asociadas al usuario");

		if (transaccionesDelUsuario == null || transaccionesDelUsuario.isEmpty()) {
			LOGGER.warn("[perteneceTransacciones] No se encontraron transacciones para las empresas: " + empresa);
			forbidden(MSG_NO_PERTENECE);
		}

		// Set para búsqueda O(1) en lugar de List.contains O(n)
		java.util.Set<Integer> empresasEnTransacciones = transaccionesDelUsuario.stream()
				.map(DrefTransaccion::getEmpresa).collect(Collectors.toSet());
		LOGGER.info("[perteneceTransacciones] IDs de empresas en transacciones: " + empresasEnTransacciones);

		LOGGER.info("[perteneceTransacciones] Validando que cada ID enviado pertenezca al usuario");
		for (DrefTransaccion idTransaccion : transaccionesDelUsuario) {
			LOGGER.info("[perteneceTransacciones] Verificando ID transacción: " + idTransaccion);
			if (empresa.longValue() != idTransaccion.getEmpresa().longValue()) {
				LOGGER.error("[perteneceTransacciones] ID transacción: " + idTransaccion
						+ " no pertenece al usuario - dni: " + dni);
				forbidden(MSG_NO_PERTENECE);
			}
			LOGGER.info("[perteneceTransacciones] ID transacción: " + idTransaccion + " validado correctamente");
		}

		LOGGER.info("[perteneceTransacciones] Validación completada exitosamente para " + idsTransacciones.size()
				+ " transaccion(es)");
	}

	public void validarMontoNpe(String npe, BigDecimal montoParametro) {
		// Extraer monto del NPE: posiciones 5-12 (índices 4-11)
		String montoStr = npe.substring(4, 12); // "00001386"

		// Convertir: 6 dígitos enteros + 2 decimales → "000013.86"
		String montoFormateado = montoStr.substring(0, 6) + "." + montoStr.substring(6, 8);
		BigDecimal montoNpe = new BigDecimal(montoFormateado);

		if (montoNpe.compareTo(montoParametro) != 0) {
			throw new AppException("Recurso no pertenece a la sesion activa - monto no pertenece a npe", 403);
		}
	}
}