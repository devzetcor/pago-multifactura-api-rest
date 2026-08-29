package com.davivienda.sv.app.services;

import com.davivienda.sv.app.entities.db2.DetalleTransaccionAutorizacionAudit;
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
public class DetalleTransaccionAutorizacionAuditRestClient {
	private static final Logger LOGGER = LogManager.getLogger(DetalleTransaccionAutorizacionAuditRestClient.class);

	@Autowired
	    private RestTemplate restTemplate;

	    @Value("${api.tactico.depositos.base.url}")
	    private String baseUrl;
	    /**
	     * Guardar múltiples auditorías de detalles de transacción autorización
	     * @param auditsTransacciones Lista de auditorías de detalles de autorización a guardar
	     * @return Lista de DetalleTransaccionAutorizacionAudit guardadas
	     */
	    public List<DetalleTransaccionAutorizacionAudit> saveAll(List<DetalleTransaccionAutorizacionAudit> auditsTransacciones) {
	        String url = baseUrl + "/detalle-autorizacion-audit/saveAll";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<List<DetalleTransaccionAutorizacionAudit>> request = new HttpEntity<>(auditsTransacciones, headers);

	        try {
	            ResponseEntity<List<DetalleTransaccionAutorizacionAudit>> response = restTemplate.exchange(
	                url,
	                HttpMethod.POST,
	                request,
	                new ParameterizedTypeReference<List<DetalleTransaccionAutorizacionAudit>>() {}
	            );

	            if (response.getStatusCode() == HttpStatus.CREATED) {
	                return response.getBody();
	            } else {
	                throw new RuntimeException("Error al guardar auditorías de detalles de autorización: código " + response.getStatusCode());
	            }

	        } catch (org.springframework.web.client.HttpServerErrorException e) {
	            throw new RuntimeException("Error del servidor al guardar auditorías de detalles de autorización: " + e.getResponseBodyAsString(), e);
	        } catch (Exception e) {
	            throw new RuntimeException("Error al guardar auditorías de detalles de autorización: " + e.getMessage(), e);
	        }
	    }
}
