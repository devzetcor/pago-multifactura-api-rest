package com.davivienda.sv.app.filters;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.springframework.web.util.ContentCachingResponseWrapper;

/* 
* @author Chrisian Guillen
* @since 2 oct 2023
* @version 1.0
*
*/

public class HttpServletResponseCached extends ContentCachingResponseWrapper {
	
	private static final Logger LOGGER = LogManager.getLogger(HttpServletResponseCached.class);
	
	String json = null;
	
	public HttpServletResponseCached(HttpServletResponse response) {
		super(response);
	}
	
	public String getJson()
	{
		if(json==null)
			json = this.getBodyResponse();
		return json;
	}

	private String getBodyResponse() {
		String json= "{}";
		try {
			byte[] responseArray = this.getContentAsByteArray();
			json = new String(responseArray, this.getCharacterEncoding());
			this.copyBodyToResponse();
		} catch (Exception e) {
			LOGGER.fatal("Error al leer json response::"+e.getMessage());
		}
		return json;
	}

}
