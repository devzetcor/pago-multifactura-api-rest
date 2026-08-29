package com.davivienda.sv.app.services;

import com.davivienda.sv.app.entities.db2.EUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EuserRestClient {
	
	 @Autowired
	    private RestTemplate restTemplate;

	    @Value("${api.tactico.depositos.base.url:http://sv4098lap:9081/tactico-depositos-rest}")
	    private String baseUrl;
		
	    /**
	     * Buscar usuario por nombre de usuario
	     * @param username Nombre de usuario a buscar
	     * @return EUser encontrado o null si no existe
	     */
	    public EUser findByUsername(String username) {
	        String url = baseUrl + "/users/by-username?username=" + username;

	        try {
	            ResponseEntity<EUser> response = restTemplate.getForEntity(url, EUser.class);
	            
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
	            throw new RuntimeException("Error al buscar usuario '" + username + "': " + e.getMessage(), e);
	        }
	    }
	  
}
