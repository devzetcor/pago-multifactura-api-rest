package com.davivienda.sv.app.services;

import com.davivienda.sv.app.entities.db2.DrefTransaccionesAudit;
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
public class DrefTransaccionesAuditRestClient {
	private static final Logger LOGGER = LogManager.getLogger(DrefTransaccionesAuditRestClient.class);
	 @Autowired
	    private RestTemplate restTemplate;

	    @Value("${api.tactico.depositos.base.url}")
	    private String baseUrl;
	    /**
	     * Guardar múltiples auditorías de transacciones
	     * @param auditsTransacciones Lista de auditorías de transacciones a guardar
	     * @return Lista de DrefTransaccionesAudit guardadas
	     */
	    public List<DrefTransaccionesAudit> saveAll(List<DrefTransaccionesAudit> auditsTransacciones) {
	        String url = baseUrl + "/audits/saveAll";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<List<DrefTransaccionesAudit>> request = new HttpEntity<>(auditsTransacciones, headers);

	        try {
	            ResponseEntity<List<DrefTransaccionesAudit>> response = restTemplate.exchange(
	                url,
	                HttpMethod.POST,
	                request,
	                new ParameterizedTypeReference<List<DrefTransaccionesAudit>>() {}
	            );

	            if (response.getStatusCode() == HttpStatus.CREATED) {
	                return response.getBody();
	            } else {
	                throw new RuntimeException("Error al guardar auditorías de transacciones: código " + response.getStatusCode());
	            }

	        } catch (org.springframework.web.client.HttpServerErrorException e) {
	        	 LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error del servidor al guardar auditorías de transacciones: " + e.getResponseBodyAsString(), e);
	        } catch (Exception e) {
	        	LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error al guardar auditorías de transacciones: " + e.getMessage(), e);
	        }
	    }
}
