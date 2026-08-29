package com.davivienda.sv.app.services;

import com.davivienda.sv.app.entities.db2.DetalleTransaccionAutorizacion;
import com.davivienda.sv.app.entities.db2.DrefTransaccion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class BatchHelperRestClient {
	private static final Logger LOGGER = LogManager.getLogger(BatchHelperRestClient.class);
	 @Autowired
	    private RestTemplate restTemplate;

	    @Value("${api.tactico.depositos.base.url}")
	    private String baseUrl;
	
	    /**
	     * Insertar autorizaciones en lote
	     * @param authorizations Lista de autorizaciones a insertar
	     */
	    public void insertAuthorizationsBatch(List<DetalleTransaccionAutorizacion> authorizations) {
	    	LOGGER.info("Iniciando inserción en lote de autorizaciones. Cantidad de registros: " + (authorizations != null ? authorizations.size() : 0));
	        String url = baseUrl + "/batch-transactions/authorizations";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<List<DetalleTransaccionAutorizacion>> request = new HttpEntity<>(authorizations, headers);

	        try {
	            ResponseEntity<Void> response = restTemplate.exchange(
	                url,
	                HttpMethod.POST,
	                request,
	                Void.class
	            );

	            if (response.getStatusCode() != HttpStatus.CREATED) {
	                throw new RuntimeException("Error al insertar autorizaciones en lote: código " + response.getStatusCode());
	            }

	        } catch (org.springframework.web.client.HttpClientErrorException.BadRequest e) {
	        	LOGGER.error("Error al insertar autorizaciones en lote: " + e.getMessage(), e);
	            throw new RuntimeException("Solicitud inválida para inserción de autorizaciones: " + e.getResponseBodyAsString(), e);
	        } catch (org.springframework.web.client.HttpServerErrorException e) {
	        	LOGGER.error("Error al insertar autorizaciones en lote: " + e.getMessage(), e);
	            throw new RuntimeException("Error del servidor al insertar autorizaciones en lote: " + e.getResponseBodyAsString(), e);
	        } catch (Exception e) {
	        	LOGGER.error("Error al insertar autorizaciones en lote: " + e.getMessage(), e);
	            throw new RuntimeException("Error al insertar autorizaciones en lote: " + e.getMessage(), e);
	        }
	    }
	    /**
	     * Actualizar estado de transacciones en lote
	     * @param transactions Lista de transacciones a actualizar
	     */
	    public void updateTransactionStatusBatch(List<DrefTransaccion> transactions) {
	    	LOGGER.info("Iniciando actualización en lote de transacciones. Cantidad de registros: " + (transactions != null ? transactions.size() : 0));
	        String url = baseUrl + "/batch-transactions/transactions/status";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<List<DrefTransaccion>> request = new HttpEntity<>(transactions, headers);

	        try {
	            ResponseEntity<Void> response = restTemplate.exchange(
	                url,
	                HttpMethod.PUT,
	                request,
	                Void.class
	            );

	            if (!response.getStatusCode().is2xxSuccessful()) {
	                throw new RuntimeException("Error al actualizar transacciones en lote: código " + response.getStatusCode());
	            }
	            LOGGER.info("código de estado "+url+ "" + response.getStatusCode());
	        } catch (org.springframework.web.client.HttpClientErrorException.BadRequest e) {
	        	 LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Solicitud inválida para actualización de transacciones: " + e.getResponseBodyAsString(), e);
	            
	        } catch (org.springframework.web.client.HttpServerErrorException e) {
	        	 LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error del servidor al actualizar transacciones en lote: " + e.getResponseBodyAsString(), e);
	            
	        } catch (Exception e) {
	        	 LOGGER.info("código de estado "+url+ " " + e.getMessage(),e);
	            throw new RuntimeException("Error al actualizar transacciones en lote: " + e.getMessage(), e);
	        }
	    }
}
