package com.davivienda.sv.app.services;

import com.davivienda.sv.app.entities.db2.DrefFacturasTransaccionAudit;
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
public class DrefFacturasTransaccionAuditRestClient {
    private static final Logger LOGGER = LogManager.getLogger(DrefFacturasTransaccionAuditRestClient.class);
	 @Autowired
	    private RestTemplate restTemplate;

	    @Value("${api.tactico.depositos.base.url}")
	    private String baseUrl;
	   /**
     * Guardar múltiples auditorías de facturas de transacción
     * @param facturasTransaccionAudits Lista de auditorías de facturas a guardar
     * @return Lista de DrefFacturasTransaccionAudit guardadas
     */
    public List<DrefFacturasTransaccionAudit> saveAll(List<DrefFacturasTransaccionAudit> facturasTransaccionAudits) {
        String url = baseUrl + "/facturas-audit/saveAll";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<List<DrefFacturasTransaccionAudit>> request = new HttpEntity<>(facturasTransaccionAudits, headers);

        try {
            ResponseEntity<List<DrefFacturasTransaccionAudit>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<List<DrefFacturasTransaccionAudit>>() {}
            );

            if (response.getStatusCode() == HttpStatus.CREATED) {
                return response.getBody();
            } else {
                throw new RuntimeException("Error al guardar auditorías de facturas: código " + response.getStatusCode());
            }

        } catch (org.springframework.web.client.HttpServerErrorException e) {
        	 LOGGER.info("código de estado "+url+ "" + e.getMessage());
            throw new RuntimeException("Error del servidor al guardar auditorías de facturas: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
        	 LOGGER.info("código de estado "+url+ "" + e.getMessage());
            throw new RuntimeException("Error al guardar auditorías de facturas: " + e.getMessage(), e);
        }
    }
}
