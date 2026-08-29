package com.davivienda.sv.app.services;

import com.davivienda.sv.app.dto.BatchOptimizedRequest;
import com.davivienda.sv.app.dto.TransaccionesDefinicionesRequest;
import com.davivienda.sv.app.entities.db2.DetalleTransaccionAutorizacion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class DetalleTransaccionAutorizacionRestClient {
	private static final Logger LOGGER = LogManager.getLogger(DetalleTransaccionAutorizacionRestClient.class);
	 @Autowired
	    private RestTemplate restTemplate;

	    @Value("${api.tactico.depositos.base.url}")
	    private String baseUrl;
		
	    
	    /**
	     * Guardar detalle de transacción autorización
	     * @param detalleTransaccion Datos del detalle de autorización a guardar
	     * @return DetalleTransaccionAutorizacion guardado
	     */
	    public DetalleTransaccionAutorizacion save(DetalleTransaccionAutorizacion detalleTransaccion) {
	        String url = baseUrl + "/detalle-transaccion-autorizacion/save";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<DetalleTransaccionAutorizacion> request = new HttpEntity<>(detalleTransaccion, headers);

	        try {
	            ResponseEntity<DetalleTransaccionAutorizacion> response = restTemplate.exchange(
	                url,
	                HttpMethod.POST,
	                request,
	                DetalleTransaccionAutorizacion.class
	            );
	            LOGGER.info("código de estado "+url+ "" + response.getStatusCode());
	            if (response.getStatusCode() == HttpStatus.CREATED) {
	                return response.getBody();
	            } else {
	            	 LOGGER.info("Error al guardar: código de estado " + response.getStatusCode());
	                throw new RuntimeException("Error al guardar: código de estado " + response.getStatusCode());
	            }

	        } catch (org.springframework.web.client.HttpServerErrorException e) {
	        	 LOGGER.info(e.getMessage());
	        	throw new RuntimeException("Error del servidor al guardar detalle transacción: " + e.getResponseBodyAsString(), e);
	        } catch (Exception e) {
	        	LOGGER.error("Datos que fallaron al guardar: " + detalleTransaccion.toString(),e);
	        	 LOGGER.info(e.getMessage());
	            throw new RuntimeException("Error al guardar detalle transacción: " + e.getMessage(), e);
	        }
	    }
	    /**
	     * Buscar detalles de transacción por ID de transacción y definiciones
	     * @param transaccionId ID de la transacción
	     * @param definiciones Lista de IDs de definiciones de autorización
	     * @return Lista de DetalleTransaccionAutorizacion encontrados
	     */
	    public List<DetalleTransaccionAutorizacion> findAllByTransaccionAndDefinicionesIn(Integer transaccionId, List<Integer> definiciones) {
	        String url = baseUrl + "/detalle-transaccion-autorizacion/by-transaccion-definiciones?transaccion=" + transaccionId;

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<List<Integer>> request = new HttpEntity<>(definiciones, headers);

	        try {
	            ResponseEntity<List<DetalleTransaccionAutorizacion>> response = restTemplate.exchange(
	                url,
	                HttpMethod.POST,
	                request,
	                new ParameterizedTypeReference<List<DetalleTransaccionAutorizacion>>() {}
	            );

	            if (response.getStatusCode().is2xxSuccessful()) {
	                return response.getBody();
	            } else {
	                throw new RuntimeException("Error en la consulta: código de estado " + response.getStatusCode());
	            }

	        } catch (org.springframework.web.client.HttpClientErrorException.BadRequest e) {
	        	   LOGGER.info("código de estado "+url+ "" + e.getMessage());
	            throw new RuntimeException("Parámetros inválidos: " + e.getResponseBodyAsString(), e);
	        } catch (Exception e) {
	        	   LOGGER.info("código de estado "+url+ "" + e.getMessage());
	            throw new RuntimeException("Error al consultar detalles de transacción: " + e.getMessage(), e);
	        }
	    }
	    
	   
	    /**
	     * Buscar detalles de transacción por usuario y lista de IDs de transacciones
	     * @param usuario Nombre de usuario
	     * @param transacciones Lista de IDs de transacciones
	     * @return Lista de DetalleTransaccionAutorizacion encontrados
	     */
	    public List<DetalleTransaccionAutorizacion> findAllByUsernameAndTransactionIdIn(String usuario, List<Long> transacciones) {
	        String url = baseUrl + "/detalle-transaccion-autorizacion/by-usuario-transacciones?usuario=" + usuario;

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<List<Long>> request = new HttpEntity<>(transacciones, headers);

	        try {
	            ResponseEntity<List<DetalleTransaccionAutorizacion>> response = restTemplate.exchange(
	                url,
	                HttpMethod.POST,
	                request,
	                new ParameterizedTypeReference<List<DetalleTransaccionAutorizacion>>() {}
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
	            throw new RuntimeException("Error al consultar detalles por usuario '" + usuario + "': " + e.getMessage(), e);
	        }
	    }
	   
	    /**
	     * Guardar múltiples DetalleTransaccionAutorizacion
	     * @param detallesTransaccion Lista de detalles de transacción autorización a guardar
	     * @return Lista de DetalleTransaccionAutorizacion guardados
	     */
	    public List<DetalleTransaccionAutorizacion> saveAll(List<DetalleTransaccionAutorizacion> detallesTransaccion) {
	        String url = baseUrl + "/detalle-transaccion-autorizacion/saveAll";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<List<DetalleTransaccionAutorizacion>> request = new HttpEntity<>(detallesTransaccion, headers);

	        try {
	            ResponseEntity<List<DetalleTransaccionAutorizacion>> response = restTemplate.exchange(
	                url,
	                HttpMethod.POST,
	                request,
	                new ParameterizedTypeReference<List<DetalleTransaccionAutorizacion>>() {}
	            );

	            if (response.getStatusCode() == HttpStatus.CREATED) {
	                return response.getBody();
	            } else {
	                throw new RuntimeException("Error al guardar detalles de transacción: código " + response.getStatusCode());
	            }

	        } catch (org.springframework.web.client.HttpServerErrorException e) {
	        	   LOGGER.info("código de estado "+url+ "" + e.getMessage());
	            throw new RuntimeException("Error del servidor al guardar detalles de transacción: " + e.getResponseBodyAsString(), e);
	        } catch (Exception e) {
	        	   LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error al guardar detalles de transacción: " + e.getMessage(), e);
	        }
	    }
	    /**
	     * Buscar autorizaciones por lotes optimizado
	     * @param request Objeto con IDs de transacciones y definiciones
	     * @return Lista de DetalleTransaccionAutorizacion encontrados
	     */
	    public List<DetalleTransaccionAutorizacion> findAuthorizationsBatchOptimized(BatchOptimizedRequest request) {
	        String url = baseUrl + "/detalle-transaccion-autorizacion/batch-optimized";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<BatchOptimizedRequest> requestEntity = new HttpEntity<>(request, headers);

	        try {
	            ResponseEntity<List<DetalleTransaccionAutorizacion>> response = restTemplate.exchange(
	                url,
	                HttpMethod.POST,
	                requestEntity,
	                new ParameterizedTypeReference<List<DetalleTransaccionAutorizacion>>() {}
	            );

	            if (response.getStatusCode().is2xxSuccessful()) {
	                return response.getBody();
	            } else {
	                throw new RuntimeException("Error en la consulta: código " + response.getStatusCode());
	            }

	        } catch (org.springframework.web.client.HttpClientErrorException.BadRequest e) {
	        	   LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Parámetros de búsqueda inválidos: " + e.getResponseBodyAsString(), e);
	        } catch (Exception e) {
	        	   LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error al buscar autorizaciones por lotes: " + e.getMessage(), e);
	        }
	    }
	    /**
	     * Buscar detalles de transacción por transacciones y definiciones
	     * @param request Objeto con listas de transacciones y definiciones
	     * @return Lista de DetalleTransaccionAutorizacion encontrados
	     */
	    public List<DetalleTransaccionAutorizacion> findAllByTransaccionesAndDefinicionesIn(TransaccionesDefinicionesRequest request) {
	        String url = baseUrl + "/detalle-transaccion-autorizacion/by-transacciones-definiciones";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<TransaccionesDefinicionesRequest> requestEntity = new HttpEntity<>(request, headers);

	        try {
	            ResponseEntity<List<DetalleTransaccionAutorizacion>> response = restTemplate.exchange(
	                url,
	                HttpMethod.POST,
	                requestEntity,
	                new ParameterizedTypeReference<List<DetalleTransaccionAutorizacion>>() {}
	            );

	            if (response.getStatusCode().is2xxSuccessful()) {
	                return response.getBody();
	            } else {
	                throw new RuntimeException("Error en la consulta: código " + response.getStatusCode());
	            }

	        } catch (org.springframework.web.client.HttpClientErrorException.BadRequest e) {
	        	   LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Listas de transacciones o definiciones inválidas: " + e.getResponseBodyAsString(), e);
	        } catch (Exception e) {
	        	   LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error al buscar detalles por transacciones y definiciones: " + e.getMessage(), e);
	        }
	    }
	    
	    
	    
	    /**
	     * Eliminar múltiples detalles de transacción autorización
	     * @param detalleTransaccionAutorizacions Lista de detalles de autorización a eliminar
	     * @return Mensaje de confirmación
	     */
	    public String deleteAll(List<DetalleTransaccionAutorizacion> detalleTransaccionAutorizacions) {
	        String url = baseUrl + "/detalle-transaccion-autorizacion/all";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<List<DetalleTransaccionAutorizacion>> request = new HttpEntity<>(detalleTransaccionAutorizacions, headers);

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
	                throw new RuntimeException("Error al eliminar detalles de autorización: código " + response.getStatusCode());
	            }

	        } catch (org.springframework.web.client.HttpServerErrorException e) {
	        	   LOGGER.info("código de estado "+url+ "" + e.getMessage());
	            throw new RuntimeException("Error del servidor al eliminar detalles de autorización: " + e.getResponseBodyAsString(), e);
	        } catch (Exception e) {
	        	   LOGGER.info("código de estado "+url+ "" + e.getMessage());
	            throw new RuntimeException("Error al eliminar detalles de autorización: " + e.getMessage(), e);
	        }
	    }
	
}
