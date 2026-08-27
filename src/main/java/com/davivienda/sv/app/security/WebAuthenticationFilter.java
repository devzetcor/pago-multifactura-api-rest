package com.davivienda.sv.app.security;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext; // Usar ThreadContext de Log4j2
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class WebAuthenticationFilter extends GenericFilterBean {

	private static final Logger LOGGER = LogManager.getLogger(WebAuthenticationFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		CachedHttpServletRequest httpReq = new CachedHttpServletRequest((HttpServletRequest) request);
		CachedHttpServletResponse httpRes = new CachedHttpServletResponse((HttpServletResponse) response);

		// Seteamos las variables en ThreadContext de Log4j2 para mantener consistencia
		ThreadContext.put("idTransaccion", httpReq.getIdTransaccion());
		ThreadContext.put("idSesion", httpReq.getIdSesion());

		try {
			LOGGER.info(String.format("INICIANDO... %s", httpReq.getRequestURI()));

			String jsonBody = httpReq.getJson();
			String requestLog = (jsonBody != null) ? jsonBody.replaceAll("(\"clave\":\")[^\"]*", "$1********") : "";
			LOGGER.info("REQUEST::" + requestLog);

			// CONTINUAR LA CADENA SOLO UNA VEZ Y CON LOS WRAPPERS:
			chain.doFilter(httpReq, httpRes);

			LOGGER.info("RESPONSE::" + httpRes.getJson());
			LOGGER.info("FINALIZANDO...");
		} finally {
			// Eliminar la llamada duplicada: chain.doFilter(request, response); <--- ELIMINADA
			ThreadContext.remove("idTransaccion");
			ThreadContext.remove("idSesion");
		}
	}
}