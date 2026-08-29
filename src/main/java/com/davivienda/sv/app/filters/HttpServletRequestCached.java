package com.davivienda.sv.app.filters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StreamUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class HttpServletRequestCached extends HttpServletRequestWrapper {
	private static final Logger LOGGER = LogManager.getLogger(HttpServletRequestCached.class);
	private byte[] cachedPayload;
	private String json = null;
	private JsonNode headerNode = null;

	public HttpServletRequestCached(HttpServletRequest request) throws IOException {
		super(request);
		try {
			this.cachedPayload = StreamUtils.copyToByteArray(request.getInputStream());
			this.json = this.getBodyRequest();
			if (this.json != null && !this.json.trim().isEmpty()) {
				JsonNode root = new ObjectMapper().readTree(this.json);
				// Lee directamente la propiedad "header" de tu JSON
				if (root.has("header")) {
					this.headerNode = root.path("header");
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error al leer JSON del request: " + e.getMessage());
		}
	}

	@Override
	public ServletInputStream getInputStream() {
		return new ServletInputStreamCached(this.cachedPayload);
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
		return get("idTransaccion");
	}

	public String getIdSesion() {
		return get("idSesion");
	}

	private String get(String field) {
		try {
			if (this.headerNode != null && this.headerNode.has(field)) {
				String valor = this.headerNode.get(field).asText();
				return (valor != null && !valor.trim().isEmpty()) ? valor : "N/A";
			}
		} catch (Exception e) {
			LOGGER.error("Error obteniendo el campo " + field + ": " + e.getMessage());
		}
		return "N/A";
	}
}