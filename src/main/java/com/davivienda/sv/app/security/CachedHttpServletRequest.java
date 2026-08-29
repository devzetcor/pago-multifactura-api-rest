package com.davivienda.sv.app.security;

import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.RequestHeader;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.util.stream.Collectors;

//Clase que envuelve el HTTP Request y registrar el payload del mismo
public class CachedHttpServletRequest extends HttpServletRequestWrapper {

    private byte[] cachedPayload;
	private String json = null;
	private RequestHeader header = null;
	private static final Logger LOGGER = LogManager.getLogger(CachedHttpServletRequest.class);


	public CachedHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
		try {
        InputStream requestInputStream = request.getInputStream();
        this.cachedPayload = StreamUtils.copyToByteArray(requestInputStream);
		this.json = this.getBodyRequest();
		if (json != null)
			this.header = new ObjectMapper().readValue(this.getJson(), Request.class).getHeader();
		} catch (Exception e) {
			LOGGER.error("Error al leer json request::" + e.getMessage(),e);
		}
		}
    @Override
    public ServletInputStream getInputStream() {
        return new CachedServletInputStream(this.cachedPayload);
    }

    @Override
    public BufferedReader getReader() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedPayload);
        return new BufferedReader(new InputStreamReader(byteArrayInputStream));
    }
    private String getBodyRequest() {
		return new BufferedReader(new InputStreamReader(getInputStream())).lines().collect(Collectors.joining());
	}

	public String getJson() {
		return json;
	}
	
	public String getIdTransaccion() {
		return (this.header != null) ? this.header.getIdTransaccion() : "N/A";
	}
	
	public String getIdSesion() {
		return (this.header != null) ? this.header.getIdSesion() : "N/A";
	}
}
