package com.davivienda.sv.app.services;

import com.davivienda.sv.app.entities.db2.EnrolamientoColector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Component
public class EnrolamientoColectorRestClient {
	private static final Logger LOGGER = LogManager.getLogger(EnrolamientoColectorRestClient.class);
	 @Autowired
	    private RestTemplate restTemplate;

	    @Value("${api.tactico.depositos.base.url}")
	    private String baseUrl;


	   
	    public List<EnrolamientoColector> findByIdEmpresaAndIdColector(Long idEmpresa, Long idColector) {
//	        LOGGER.debug("REST Client: Consultando enrolamientos por empresa: " + idEmpresa + ", colector: " + idColector);
	        
	    	String url = baseUrl + "/enrolamiento-colector/by-empresa-colector";
	        try {
	          
	            URI uri = UriComponentsBuilder.fromHttpUrl(url)
	                    .queryParam("idEmpresa", idEmpresa)
	                    .queryParam("idColector", idColector)
	                    .build()
	                    .toUri();
	            ResponseEntity<EnrolamientoColector[]> response = restTemplate.getForEntity(
	            		uri, EnrolamientoColector[].class);
	            
	            return Arrays.asList(response.getBody());
	            
	        } catch (Exception e) {
	        	LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error consultando enrolamientos: " + e.getMessage(), e);
	        }
	    }

	    public List<EnrolamientoColector> saveAll(List<EnrolamientoColector> enrolamientos) {
//	        LOGGER.debug("REST Client: Guardando múltiples enrolamientos count: " +   (enrolamientos != null ? enrolamientos.size() : 0));
	        
	        // Validación local antes de enviar
	        if (enrolamientos == null || enrolamientos.isEmpty()) {
//	            logger.warn("Lista de enrolamientos vacía o nula para saveAll");
	            return Arrays.asList(); // Retorna lista vacía
	        }
	        
	        try {
	            // URL del endpoint POST
	            String url = baseUrl + "/enrolamiento-colector/saveAll";
	            
	            // Configurar headers para JSON
	            HttpHeaders headers = new HttpHeaders();
	            headers.setContentType(MediaType.APPLICATION_JSON);
	            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	            
	            // Crear el request entity con la lista de enrolamientos en el body
	            HttpEntity<List<EnrolamientoColector>> requestEntity = new HttpEntity<>(enrolamientos, headers);
	            
	            // Llamada POST con el cuerpo JSON
	            ResponseEntity<List<EnrolamientoColector>> response = restTemplate.exchange(
	                url,
	                HttpMethod.POST,
	                requestEntity,
	                new ParameterizedTypeReference<List<EnrolamientoColector>>() {}
	            );
	            
	            // Verificar que la respuesta fue exitosa (201 CREATED)
	            if (response.getStatusCode() == HttpStatus.CREATED) {
//	                LOGGER.info("Successfully saved " + response.getBody().size() + " enrolamientos");
	                return response.getBody();
	            } else {
//	                logger.warn("Unexpected response status: " + response.getStatusCode());
	                throw new RuntimeException("Error guardando enrolamientos: Status " + response.getStatusCode());
	            }
	            
	        } catch (Exception e) {
	            LOGGER.error("Error guardando múltiples enrolamientos: " + e.getMessage(), e);
	            throw new RuntimeException("Error guardando enrolamientos: " + e.getMessage(), e);
	        }
	    }
	    
	    public List<EnrolamientoColector> findAllByIds(List<Integer> ids) {
//	        LOGGER.debug("REST Client: Consultando enrolamientos por IDs count: " + ids.size());
	        
	        // Validación local antes de enviar (opcional)
	        if (ids == null || ids.isEmpty()) {
//	            logger.warn("Lista de IDs vacía o nula");
	            return Arrays.asList(); // Retorna lista vacía
	        }
	        
	        try {
	            // URL del endpoint POST
	            String url = baseUrl + "/enrolamiento-colector/by-ids";
	            
	            // Configurar headers para JSON
	            HttpHeaders headers = new HttpHeaders();
	            headers.setContentType(MediaType.APPLICATION_JSON);
	            
	            // Crear el request entity con los IDs en el body
	            HttpEntity<List<Integer>> requestEntity = new HttpEntity<>(ids, headers);
	            
	            // Llamada POST con el cuerpo JSON
	            ResponseEntity<EnrolamientoColector[]> response = restTemplate.exchange(
	                url,
	                HttpMethod.POST,
	                requestEntity,
	                EnrolamientoColector[].class
	            );
	            
	            // Convertir array a List
	            return Arrays.asList(response.getBody());
	            
	        } catch (Exception e) {
	            LOGGER.error("Error consultando enrolamientos por IDs: " + e.getMessage(), e);
	            throw new RuntimeException("Error consultando enrolamientos por IDs: " + e.getMessage(), e);
	        }
	    }
	    
	   
	    public void deleteAllById(List<Long> ids) {
//	        LOGGER.debug("REST Client: Eliminando enrolamientos por IDs count: " + (ids != null ? ids.size() : 0));
	        
	        // Validación local antes de enviar
	        if (ids == null || ids.isEmpty()) {
//	            logger.warn("Lista de IDs vacía o nula para deleteAllById - no se enviará petición");
	            return; // No hacer nada si la lista está vacía
	        }
	        
	        try {
	            // URL del endpoint DELETE
	            String url = baseUrl + "/enrolamiento-colector/by-ids";
	            
	            // Configurar headers para JSON
	            HttpHeaders headers = new HttpHeaders();
	            headers.setContentType(MediaType.APPLICATION_JSON);
	            
	            // Crear el request entity con la lista de IDs en el body
	            HttpEntity<List<Long>> requestEntity = new HttpEntity<>(ids, headers);
	            
	            // Llamada DELETE con el cuerpo JSON
	            ResponseEntity<Void> response = restTemplate.exchange(
	                url,
	                HttpMethod.DELETE,
	                requestEntity,
	                Void.class
	            );
	            
	            // Verificar que la respuesta fue exitosa (200 OK)
	            if (response.getStatusCode() == HttpStatus.OK) {
//	                LOGGER.info("✅ Successfully deleted " + ids.size() + " enrolamientos");
	            } else {
//	                logger.warn("⚠️ Unexpected response status for delete: " + response.getStatusCode());
	                throw new RuntimeException("Error eliminando enrolamientos: Status " + response.getStatusCode());
	            }
	            
	        } catch (Exception e) {
	            LOGGER.error("❌ Error eliminando múltiples enrolamientos: " + e.getMessage(), e);
	            throw new RuntimeException("Error eliminando enrolamientos: " + e.getMessage(), e);
	        }
	    }
	    
	  
	    public EnrolamientoColector findById(Integer id) {
	        String url = baseUrl + "/enrolamiento-colector/" + id + "/findById";

	        try {
	            ResponseEntity<EnrolamientoColector> response = restTemplate.exchange(
	                url,
	                HttpMethod.GET,
	                null,
	                EnrolamientoColector.class
	            );

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
	            throw new RuntimeException("Error del servidor al buscar enrolamiento ID " + id + 
	                ": " + e.getResponseBodyAsString(), e);
	        } catch (Exception e) {
	            throw new RuntimeException("Error al buscar enrolamiento ID " + id + 
	                ": " + e.getMessage(), e);
	        }
	    }
}
