package com.davivienda.sv.app.services;

import com.davivienda.sv.app.dto.ConsultaEnrolamientoDTO;
import com.davivienda.sv.app.entities.db2.DrefTransaccion;
import com.davivienda.sv.app.entities.db2.TransaccionDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.List;

@Component
public class DrefTransaccionRestClient {
	private static final Logger LOGGER = LogManager.getLogger(DrefTransaccionRestClient.class);
	 @Autowired
	    private RestTemplate restTemplate;

	    @Value("${api.tactico.depositos.base.url}")
	    private String baseUrl;
	    /**
	     * Eliminar múltiples transacciones
	     * @param drefTransacciones Lista de transacciones a eliminar
	     * @return Mensaje de confirmación
	     */
	    public String deleteAll(List<DrefTransaccion> drefTransacciones) {
	        String url = baseUrl + "/dref-transacciones/all";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<List<DrefTransaccion>> request = new HttpEntity<>(drefTransacciones, headers);

	        try {
	            ResponseEntity<String> response = restTemplate.exchange(
	                url,
	                HttpMethod.DELETE,
	                request,
	                String.class
	            );

	            if (response.getStatusCode().is2xxSuccessful()) {
	                return response.getBody();
	            } else {
	                throw new RuntimeException("Error al eliminar transacciones: código " + response.getStatusCode());
	            }

	        } catch (org.springframework.web.client.HttpServerErrorException e) {
	        	 LOGGER.info("código de estado "+url+ " " + e.getMessage(),e);
	            throw new RuntimeException("Error del servidor al eliminar transacciones: " + e.getResponseBodyAsString(), e);
	        } catch (Exception e) {
	        	 LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error al eliminar transacciones: " + e.getMessage(), e);
	        }
	    }
		    
	    
	    public DrefTransaccion findById(Integer id) {
	        String url = baseUrl + "/dref-transacciones/" + id + "/findById";

	        try {
	            ResponseEntity<DrefTransaccion> response = restTemplate.getForEntity(url, DrefTransaccion.class);
	            
	            if (response.getStatusCode().is2xxSuccessful()) {
	                return response.getBody();
	            } else {
	                return null;
	            }

	        } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
	            // 404 Not Found - la transacción no existe
	            return null;
	        } catch (Exception e) {
	        	 LOGGER.info("código de estado "+url+ " " + e.getMessage(),e);
	            throw new RuntimeException("Error al buscar transacción con ID " + id + ": " + e.getMessage(), e);
	        }
	    }
	    
	    
	    /**
	     * Guardar DrefTransaccion
	     * @param drefTransaccion Datos de la transacción a guardar
	     * @return DrefTransaccion guardada
	     */
	    public DrefTransaccion save(DrefTransaccion drefTransaccion) {
	        String url = baseUrl + "/dref-transacciones/save";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<DrefTransaccion> request = new HttpEntity<>(drefTransaccion, headers);

	        try {
	            ResponseEntity<DrefTransaccion> response = restTemplate.exchange(
	                url,
	                HttpMethod.POST,
	                request,
	                DrefTransaccion.class
	            );

	            if (response.getStatusCode() == HttpStatus.CREATED) {
	                return response.getBody();
	            } else {
	                throw new RuntimeException("Error al guardar: código de estado " + response.getStatusCode());
	            }

	        } catch (org.springframework.web.client.HttpServerErrorException e) {
	        	LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error del servidor al guardar transacción: " + e.getResponseBodyAsString(), e);
	        } catch (Exception e) {
	        	LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error al guardar transacción: " + e.getMessage(), e);
	        }
	    }
	 
	    /**
	     * Listar transacciones por estado
	     * @param consultaEnrolamientoDTO Datos de consulta de enrolamiento
	     * @param estado Estado de las transacciones a consultar
	     * @return Lista de transacciones con el estado especificado
	     */
	    public List<TransaccionDTO> listarTransaccionesPorEstado(ConsultaEnrolamientoDTO consultaEnrolamientoDTO, String estado) {
	        String url = baseUrl + "/jdbc/enrolamiento-colector/transacciones/por-estado?estado=" + estado;

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<ConsultaEnrolamientoDTO> request = new HttpEntity<>(consultaEnrolamientoDTO, headers);

	        try {
	            ResponseEntity<List<TransaccionDTO>> response = restTemplate.exchange(
	                url,
	                HttpMethod.POST,
	                request,
	                new ParameterizedTypeReference<List<TransaccionDTO>>() {}
	            );

	            if (response.getStatusCode().is2xxSuccessful()) {
	                return response.getBody();
	            } else {
	                throw new RuntimeException("Error en la consulta: código " + response.getStatusCode());
	            }

	        } catch (Exception e) {
	        	LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error al consultar transacciones por estado '" + estado + "': " + e.getMessage(), e);
	        }
	    }
	    /**
	     * Listar transacciones por IDs y estado
	     * @param transactionsIds Lista de IDs de transacciones
	     * @param status Estado de las transacciones a consultar
	     * @return Lista de transacciones que coinciden con los IDs y estado
	     */
	    public List<TransaccionDTO> listarTransaccionesPorEstadoIds(List<Long> transactionsIds, String status) {
	        String url = baseUrl + "/jdbc/enrolamiento-colector/transacciones/por-ids-estado?status=" + status;

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<List<Long>> request = new HttpEntity<>(transactionsIds, headers);

	        try {
	            ResponseEntity<List<TransaccionDTO>> response = restTemplate.exchange(
	                url,
	                HttpMethod.POST,
	                request,
	                new ParameterizedTypeReference<List<TransaccionDTO>>() {}
	            );

	            if (response.getStatusCode().is2xxSuccessful()) {
	                return response.getBody();
	            } else {
	                throw new RuntimeException("Error en la consulta: código " + response.getStatusCode());
	            }

	        } catch (org.springframework.web.client.HttpClientErrorException.BadRequest e) {
	        	LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Lista de IDs inválida o vacía: " + e.getResponseBodyAsString(), e);
	        } catch (Exception e) {
	        	LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error al consultar transacciones por IDs y estado '" + status + "': " + e.getMessage(), e);
	        }
	    }
	    
	    /**
	     * Obtener transacción con facturas por ID
	     * @param idTransaccion ID de la transacción a obtener
	     * @return TransaccionDTO con facturas incluidas o null si no existe
	     */
	    public TransaccionDTO obtenerTransaccionConFacturas(Long idTransaccion) {
	        String url = baseUrl + "/jdbc/enrolamiento-colector/transacciones/" + idTransaccion;

	        try {
	            ResponseEntity<TransaccionDTO> response = restTemplate.getForEntity(url, TransaccionDTO.class);
	            
	            if (response.getStatusCode() == HttpStatus.OK) {
	                return response.getBody();
	            } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
	                return null;
	            } else {
	                throw new RuntimeException("Respuesta inesperada: " + response.getStatusCode());
	            }

	        } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
	            return null;
	        } catch (org.springframework.web.client.HttpServerErrorException e) {
	        	 LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error del servidor al obtener transacción ID " + idTransaccion + 
	                ": " + e.getResponseBodyAsString(), e);
	        } catch (Exception e) {
	        	 LOGGER.info("código de estado "+url+ "" + e.getMessage());
	            throw new RuntimeException("Error al obtener transacción ID " + idTransaccion + 
	                ": " + e.getMessage(), e);
	        }
	    }
	    
	    /**
	     * Listar transacciones pendientes
	     * @param consultaEnrolamientoDTO Datos de consulta de enrolamiento
	     * @return Lista de transacciones en estado pendiente
	     */
	    public List<TransaccionDTO> listarTransaccionesPendientes(ConsultaEnrolamientoDTO consultaEnrolamientoDTO) {
	        String url = baseUrl + "/transacciones/pendientes";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<ConsultaEnrolamientoDTO> request = new HttpEntity<>(consultaEnrolamientoDTO, headers);

	        try {
	            ResponseEntity<List<TransaccionDTO>> response = restTemplate.exchange(
	                url,
	                HttpMethod.POST,
	                request,
	                new ParameterizedTypeReference<List<TransaccionDTO>>() {}
	            );

	            if (response.getStatusCode().is2xxSuccessful()) {
	                return response.getBody();
	            } else {
	                throw new RuntimeException("Error en la consulta: código " + response.getStatusCode());
	            }

	        } catch (Exception e) {
	        	LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error al consultar transacciones pendientes: " + e.getMessage(), e);
	        }
	    }
	    /**
	     * Listar transacciones
	     * @param consultaEnrolamientoDTO Datos de consulta de enrolamiento
	     * @return Lista de transacciones según los criterios de consulta
	     */
	    public List<TransaccionDTO> listarTransacciones(ConsultaEnrolamientoDTO consultaEnrolamientoDTO) {
	        String url = baseUrl + "/jdbc/enrolamiento-colector/transacciones/listar";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<ConsultaEnrolamientoDTO> request = new HttpEntity<>(consultaEnrolamientoDTO, headers);

	        try {
	            ResponseEntity<List<TransaccionDTO>> response = restTemplate.exchange(
	                url,
	                HttpMethod.POST,
	                request,
	                new ParameterizedTypeReference<List<TransaccionDTO>>() {}
	            );

	            if (response.getStatusCode().is2xxSuccessful()) {
	                return response.getBody();
	            } else {
	                throw new RuntimeException("Error en la consulta: código " + response.getStatusCode());
	            }

	        } catch (Exception e) {
	        	LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error al listar transacciones: " + e.getMessage(), e);
	        }
	    }
	    /**
	     * Listar transacciones con paginación
	     * @param consultaEnrolamientoDTO Datos de consulta de enrolamiento
	     * @param page Número de página
	     * @param size Tamaño de página
	     * @return Lista paginada de transacciones según los criterios de consulta
	     */
	    public List<TransaccionDTO> listarTransaccionesPaginado(ConsultaEnrolamientoDTO consultaEnrolamientoDTO, int page, int size) {
	        String url = baseUrl + "/jdbc/enrolamiento-colector/transacciones/listar-paginado?page=" + page + "&size=" + size;

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<ConsultaEnrolamientoDTO> request = new HttpEntity<>(consultaEnrolamientoDTO, headers);

	        try {
	            ResponseEntity<List<TransaccionDTO>> response = restTemplate.exchange(
	                url,
	                HttpMethod.POST,
	                request,
	                new ParameterizedTypeReference<List<TransaccionDTO>>() {}
	            );

	            if (response.getStatusCode().is2xxSuccessful()) {
	                return response.getBody();
	            } else {
	                throw new RuntimeException("Error en la consulta: código " + response.getStatusCode());
	            }

	        } catch (Exception e) {
	        	LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error al listar transacciones paginado: " + e.getMessage(), e);
	        }
	    }
	    /**
	     * Listar transacciones por estado con paginación
	     * @param consultaEnrolamientoDTO Datos de consulta de enrolamiento
	     * @param estado Estado de las transacciones a consultar
	     * @param page Número de página
	     * @param size Tamaño de página
	     * @return Lista paginada de transacciones con el estado especificado
	     */
	    public List<TransaccionDTO> listarTransaccionesPorEstadoPaginado(ConsultaEnrolamientoDTO consultaEnrolamientoDTO, String estado, Integer page, Integer size) {
	        String url = baseUrl + "/jdbc/enrolamiento-colector/transacciones/por-estado-paginado?estado=" + estado + "&page=" + page + "&size=" + size;

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<ConsultaEnrolamientoDTO> request = new HttpEntity<>(consultaEnrolamientoDTO, headers);

	        try {
	            ResponseEntity<List<TransaccionDTO>> response = restTemplate.exchange(
	                url,
	                HttpMethod.POST,
	                request,
	                new ParameterizedTypeReference<List<TransaccionDTO>>() {}
	            );

	            if (response.getStatusCode().is2xxSuccessful()) {
	                return response.getBody();
	            } else {
	                throw new RuntimeException("Error en la consulta: código " + response.getStatusCode());
	            }

	        } catch (Exception e) {
	        	LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error al listar transacciones por estado paginado: " + e.getMessage(), e);
	        }
	    }
	    /**
	     * Buscar transacciones por lista de IDs
	     * @param ids Lista de IDs de transacciones a buscar
	     * @return Lista de DrefTransaccion encontradas
	     */
	    public List<DrefTransaccion> findAllByIds(List<Long> ids) {
	        String url = baseUrl + "/dref-transacciones/by-ids";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<List<Long>> request = new HttpEntity<>(ids, headers);

	        try {
	            ResponseEntity<List<DrefTransaccion>> response = restTemplate.exchange(
	                url,
	                HttpMethod.POST,
	                request,
	                new ParameterizedTypeReference<List<DrefTransaccion>>() {}
	            );

	            if (response.getStatusCode().is2xxSuccessful()) {
	                return response.getBody();
	            } else {
	                throw new RuntimeException("Error en la consulta: código " + response.getStatusCode());
	            }

	        } catch (org.springframework.web.client.HttpClientErrorException.BadRequest e) {
	        	LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Lista de IDs inválida o vacía: " + e.getResponseBodyAsString(), e);
	        } catch (Exception e) {
	        	LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error al buscar transacciones por IDs: " + e.getMessage(), e);
	        }
	    }
	   
	    /**
	     * Guardar múltiples DrefTransaccion
	     * @param drefTransacciones Lista de transacciones a guardar
	     * @return Lista de DrefTransaccion guardadas
	     */
	    public List<DrefTransaccion> saveAll(List<DrefTransaccion> drefTransacciones) {
	        String url = baseUrl + "/dref-transacciones/saveAll";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<List<DrefTransaccion>> request = new HttpEntity<>(drefTransacciones, headers);

	        try {
	            ResponseEntity<List<DrefTransaccion>> response = restTemplate.exchange(
	                url,
	                HttpMethod.POST,
	                request,
	                new ParameterizedTypeReference<List<DrefTransaccion>>() {}
	            );

	            if (response.getStatusCode() == HttpStatus.CREATED) {
	                return response.getBody();
	            } else {
	                throw new RuntimeException("Error al guardar transacciones: código " + response.getStatusCode());
	            }

	        } catch (org.springframework.web.client.HttpServerErrorException e) {
	        	LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error del servidor al guardar transacciones: " + e.getResponseBodyAsString(), e);
	        } catch (Exception e) {
	        	LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error al guardar transacciones: " + e.getMessage(), e);
	        }
	    }
	   
	   
	    /**
	     * Buscar transacciones por usuario y lista de IDs de transacciones
	     * @param usuario Nombre de usuario
	     * @param transacciones Lista de IDs de transacciones
	     * @return Lista de DrefTransaccion encontradas
	     */
	    public List<DrefTransaccion> findAllByUsernameAndTransactionIdIn(String usuario, List<Long> transacciones) {
	        String url = baseUrl + "/dref-transacciones/by-username-and-ids?usuario=" + usuario;

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<List<Long>> request = new HttpEntity<>(transacciones, headers);

	        try {
	            ResponseEntity<List<DrefTransaccion>> response = restTemplate.exchange(
	                url,
	                HttpMethod.POST,
	                request,
	                new ParameterizedTypeReference<List<DrefTransaccion>>() {}
	            );

	            if (response.getStatusCode().is2xxSuccessful()) {
	                return response.getBody();
	            } else {
	                throw new RuntimeException("Error en la consulta: código " + response.getStatusCode());
	            }

	        } catch (org.springframework.web.client.HttpClientErrorException.BadRequest e) {
	        	LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Lista de transacciones inválida o vacía: " + e.getResponseBodyAsString(), e);
	        } catch (Exception e) {
	        	LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error al consultar transacciones por usuario '" + usuario + "': " + e.getMessage(), e);
	        }
	    }
	    
	    
	   
	    /**
	     * Buscar transacciones por rango de fechas y estados
	     * @param desde Fecha y hora de inicio del rango
	     * @param hasta Fecha y hora de fin del rango
	     * @param estados Lista de estados a filtrar
	     * @return Lista de DrefTransaccion encontradas en el rango de fechas
	     */
	    public List<DrefTransaccion> findByFechaCreacionBetweenOrFechaAprobacionBetween( List<String> estados,Timestamp desde, Timestamp hasta) {
	        String estadosParam = String.join(",", estados);
	        String url = baseUrl + "/transacciones/by-fecha-range" +
	                    "?desde=" + desde.toString() +
	                    "&hasta=" + hasta.toString() +
	                    "&estados=" + estadosParam;

	        try {
	            ResponseEntity<List<DrefTransaccion>> response = restTemplate.exchange(
	                url,
	                HttpMethod.GET,
	                null,
	                new ParameterizedTypeReference<List<DrefTransaccion>>() {}
	            );

	            if (response.getStatusCode().is2xxSuccessful()) {
	                return response.getBody();
	            } else {
	                throw new RuntimeException("Error en la consulta: código " + response.getStatusCode());
	            }

	        } catch (Exception e) {
	        	LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error al buscar transacciones por rango de fechas: " + e.getMessage(), e);
	        }
	    }  
	    public List<TransaccionDTO> listarTransaccionesPendientesPorEmpresas(List<Long> empresas) {
	        String url = baseUrl + "/jdbc/enrolamiento-colector/transacciones/pendientes-empresas";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<List<Long>> request = new HttpEntity<>(empresas, headers);

	        try {
	            ResponseEntity<List<TransaccionDTO>> response = restTemplate.exchange(
	                url,
	                HttpMethod.POST,
	                request,
	                new ParameterizedTypeReference<List<TransaccionDTO>>() {}
	            );

	            if (response.getStatusCode().is2xxSuccessful()) {
	                return response.getBody();
	            } else {
	                throw new RuntimeException("Error en la consulta: código " + response.getStatusCode());
	            }

	        } catch (org.springframework.web.client.HttpClientErrorException.BadRequest e) {
	        	LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Lista de empresas inválida o vacía: " + e.getResponseBodyAsString(), e);
	        } catch (Exception e) {
	        	LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error al consultar transacciones pendientes por empresas: " + e.getMessage(), e);
	        }
	    }
	    /**
	     * Aprobar transacción
	     * @param idTransaccion ID de la transacción a aprobar
	     * @param usuarioAprobacion Usuario que aprueba la transacción
	     * @param estado Estado a asignar a la transacción
	     */
	    public void aprobarTransaccion(Long idTransaccion, String usuarioAprobacion, String estado) {
	        String url = baseUrl + "/jdbc/enrolamiento-colector/transacciones/" + idTransaccion + "/aprobar" +
	                    "?usuarioAprobacion=" + usuarioAprobacion + "&estado=" + estado;

	        try {
	            ResponseEntity<Void> response = restTemplate.exchange(
	                url,
	                HttpMethod.PUT,
	                null,
	                Void.class
	            );

	            if (!response.getStatusCode().is2xxSuccessful()) {
	                throw new RuntimeException("Error al aprobar transacción: código " + response.getStatusCode());
	            }

	        } catch (org.springframework.web.client.HttpServerErrorException e) {
	        	LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error del servidor al aprobar transacción ID " + idTransaccion + 
	                ": " + e.getResponseBodyAsString(), e);
	        } catch (Exception e) {
	        	LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error al aprobar transacción ID " + idTransaccion + 
	                ": " + e.getMessage(), e);
	        }
	    }
	    public TransaccionDTO crearTransaccion(TransaccionDTO transaccion) {
	        String url = baseUrl + "/jdbc/enrolamiento-colector/transacciones";

	        // Configurar headers
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        // Crear la petición HTTP
	        HttpEntity<TransaccionDTO> request = new HttpEntity<>(transaccion, headers);

	        try {
	            // Ejecutar POST request
	            ResponseEntity<TransaccionDTO> response = restTemplate.exchange(
	                url,
	                HttpMethod.POST,
	                request,
	                TransaccionDTO.class
	            );

	            if (response.getStatusCode().is2xxSuccessful()) {
	                return response.getBody();
	            } else {
	                throw new RuntimeException("Error al crear transacción. Status: " + response.getStatusCode());
	            }

	        } catch (Exception e) {
	        	LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error en la comunicación con el servicio de transacciones: " + e.getMessage(), e);
	        }
	    } 
	    /**
	     * Obtener última transacción por colector, empresa y usuario
	     * @param idColector ID del colector
	     * @param idEmpresa ID de la empresa
	     * @param usuario Usuario creador
	     * @return Última transacción encontrada o null si no existe
	     */
	    public TransaccionDTO obtenerUltimaTransaccionPorColectorEmpresaUsuario(Long idColector, Long idEmpresa, String usuario) {
	    	LOGGER.info("Consultando ultima TRX -> Colector: " + idColector + ", Empresa: " + idEmpresa + ", Usuario: " + usuario);
	        String url = baseUrl + "/jdbc/enrolamiento-colector/transacciones/ultima" +
	                    "?idColector=" + idColector +
	                    "&idEmpresa=" + idEmpresa +
	                    "&usuario=" + usuario;

	        try {
	            ResponseEntity<TransaccionDTO> response = restTemplate.getForEntity(url, TransaccionDTO.class);
	            
	            if (response.getStatusCode() == HttpStatus.OK) {
	                return response.getBody();
	            } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
	                return null;
	            } else {
	                throw new RuntimeException("Respuesta inesperada: " + response.getStatusCode());
	            }

	        } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
	            return null;
	        } catch (Exception e) {
	        	LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error al obtener última transacción para colector " + idColector + 
	                ", empresa " + idEmpresa + ", usuario " + usuario + ": " + e.getMessage(), e);
	        }
	    }
}
