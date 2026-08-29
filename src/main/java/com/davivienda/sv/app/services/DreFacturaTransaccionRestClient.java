package com.davivienda.sv.app.services;

import com.davivienda.sv.app.dto.ActualizarFacturaCompletaRequest;
import com.davivienda.sv.app.dto.ActualizarFacturaDescripcionRequest;
import com.davivienda.sv.app.entities.db2.DrefFacturaTransaccion;
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
public class DreFacturaTransaccionRestClient {

	private static final Logger LOGGER = LogManager.getLogger(DreFacturaTransaccionRestClient.class);

	@Autowired
	    private RestTemplate restTemplate;

	    @Value("${api.tactico.depositos.base.url}")
	    private String baseUrl;

	    /**
	     * Actualizar estado de factura con descripción
	     * @param idDetalle ID del detalle de factura a actualizar
	     * @param request Objeto con estado y descripción a actualizar
	     */
	    public void actualizarEstadoFacturaConDescripcion(Long idDetalle, ActualizarFacturaDescripcionRequest request) {
	        String url = baseUrl + "/jdbc/enrolamiento-colector/facturas/" + idDetalle + "/estado-descripcion";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<ActualizarFacturaDescripcionRequest> entity = new HttpEntity<>(request, headers);

	        try {
	            ResponseEntity<Void> response = restTemplate.exchange(
	                url,
	                HttpMethod.PUT,
	                entity,
	                Void.class
	            );

	            if (!response.getStatusCode().is2xxSuccessful()) {
	                throw new RuntimeException("Error al actualizar factura: código " + response.getStatusCode());
	            }

	        } catch (org.springframework.web.client.HttpServerErrorException e) {
	        	   LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error del servidor al actualizar factura ID " + idDetalle +   ": " + e.getResponseBodyAsString(), e);
	        } catch (Exception e) {
	        	   LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error al actualizar factura ID " + idDetalle + 
	                ": " + e.getMessage(), e);
	        }
	    }
	   
	   
	    /**
	     * Actualizar estado de factura
	     * @param idFactura ID de la factura a actualizar
	     * @param nuevoEstado Nuevo estado a asignar a la factura
	     */
	    public void actualizarEstadoFactura(Long idFactura, String nuevoEstado) {
	        String url = baseUrl + "/dref-factura-transaccion/" + idFactura + "/estado?nuevoEstado=" + nuevoEstado;

	        try {
	            ResponseEntity<Void> response = restTemplate.exchange(
	                url,
	                HttpMethod.PUT,
	                null,
	                Void.class
	            );

	            if (!response.getStatusCode().is2xxSuccessful()) {
	                throw new RuntimeException("Error al actualizar estado de factura: código " + response.getStatusCode());
	            }

	        } catch (org.springframework.web.client.HttpServerErrorException e) {
	        	   LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error del servidor al actualizar estado de factura ID " + idFactura + 
	                ": " + e.getResponseBodyAsString(), e);
	        } catch (Exception e) {
	        	   LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error al actualizar estado de factura ID " + idFactura + 
	                ": " + e.getMessage(), e);
	        }
	    }
	    /**
	     * Actualizar estado de factura con referencia
	     * @param idFactura ID de la factura a actualizar
	     * @param nuevoEstado Nuevo estado a asignar a la factura
	     * @param referencia Referencia adicional para la actualización
	     */
	    public void actualizarEstadoFacturaConReferencia(Long idFactura, String nuevoEstado, String referencia) {
	        String url = baseUrl + "/jdbc/enrolamiento-colector/facturas/" + idFactura + "/estado-referencia" +
	                    "?nuevoEstado=" + nuevoEstado + "&referencia=" + referencia;

	        try {
	            ResponseEntity<Void> response = restTemplate.exchange(
	                url,
	                HttpMethod.PUT,
	                null,
	                Void.class
	            );

	            if (!response.getStatusCode().is2xxSuccessful()) {
	                throw new RuntimeException("Error al actualizar estado de factura con referencia: código " + response.getStatusCode());
	            }

	        } catch (org.springframework.web.client.HttpServerErrorException e) {
	        	   LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error del servidor al actualizar estado de factura ID " + idFactura + 
	                ": " + e.getResponseBodyAsString(), e);
	        } catch (Exception e) {
	        	   LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error al actualizar estado de factura ID " + idFactura + 
	                ": " + e.getMessage(), e);
	        }
	    }
	    /**
	     * Actualizar estado completo de factura
	     * @param idFactura ID de la factura a actualizar
	     * @param request Objeto con todos los datos para actualizar la factura
	     */
	    public void actualizarEstadoFacturaCompleto(Long idFactura, ActualizarFacturaCompletaRequest request) {
	        String url = baseUrl + "/dref-factura-transaccion/" + idFactura + "/estado-completo";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<ActualizarFacturaCompletaRequest> entity = new HttpEntity<>(request, headers);

	        try {
	            ResponseEntity<Void> response = restTemplate.exchange(
	                url,
	                HttpMethod.PUT,
	                entity,
	                Void.class
	            );

	            if (!response.getStatusCode().is2xxSuccessful()) {
	                throw new RuntimeException("Error al actualizar estado completo de factura: código " + response.getStatusCode());
	            }

	        } catch (org.springframework.web.client.HttpServerErrorException e) {
	        	   LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error del servidor al actualizar estado completo de factura ID " + idFactura + 
	                ": " + e.getResponseBodyAsString(), e);
	        } catch (Exception e) {
	        	   LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error al actualizar estado completo de factura ID " + idFactura + 
	                ": " + e.getMessage(), e);
	        }
	    }
	
	  
	    /**
	     * Buscar facturas por colector
	     * @param colector Nombre o ID del colector
	     * @return Lista de DrefFacturaTransaccion del colector especificado
	     */
	    public List<DrefFacturaTransaccion> findByColector(String colector) {
	        String url = baseUrl + "/dref-factura-transaccion/by-colector?colector=" + colector;

	        try {
	            ResponseEntity<List<DrefFacturaTransaccion>> response = restTemplate.exchange(
	                url,
	                HttpMethod.GET,
	                null,
	                new ParameterizedTypeReference<List<DrefFacturaTransaccion>>() {}
	            );

	            if (response.getStatusCode().is2xxSuccessful()) {
	                return response.getBody();
	            } else {
	                throw new RuntimeException("Error en la consulta: código " + response.getStatusCode());
	            }

	        } catch (Exception e) {
	        	   LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error al buscar facturas por colector '" + colector + "': " + e.getMessage(), e);
	        }
	    }
	   
	    /**
	     * Eliminar múltiples facturas de transacción
	     * @param drefFacturaTransaccions Lista de facturas de transacción a eliminar
	     * @return Mensaje de confirmación
	     */
	    public String deleteAll(List<DrefFacturaTransaccion> drefFacturaTransaccions) {
	        String url = baseUrl + "/facturas/all";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<List<DrefFacturaTransaccion>> request = new HttpEntity<>(drefFacturaTransaccions, headers);

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
	                throw new RuntimeException("Error al eliminar facturas: código " + response.getStatusCode());
	            }

	        } catch (org.springframework.web.client.HttpServerErrorException e) {
	        	   LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error del servidor al eliminar facturas: " + e.getResponseBodyAsString(), e);
	        } catch (Exception e) {
	        	   LOGGER.info("código de estado "+url+ "" + e.getMessage(),e);
	            throw new RuntimeException("Error al eliminar facturas: " + e.getMessage(), e);
	        }
	    } 
	
}
