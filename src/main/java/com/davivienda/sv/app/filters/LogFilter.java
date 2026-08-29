package com.davivienda.sv.app.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.io.IOException;

@WebFilter("/*")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LogFilter implements Filter {
    private static final Logger LOGGER = LogManager.getLogger(LogFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequestCached httpReq = new HttpServletRequestCached((HttpServletRequest) request);
        HttpServletResponseCached httpRes = new HttpServletResponseCached((HttpServletResponse) response);

        // Setea idSesion e idTransaccion obtenidas del JSON al MDC
        ThreadContext.put("idSesion", httpReq.getIdSesion());
        ThreadContext.put("idTransaccion", httpReq.getIdTransaccion());

        try {
            LOGGER.info("Iniciando peticion REST");
            LOGGER.info("REQUEST::{}", httpReq.getJson());

            chain.doFilter(httpReq, httpRes);

            LOGGER.info("RESPONSE::{}", httpRes.getJson());
            LOGGER.info("Finalizando peticion REST");
        } catch (Throwable e) {
            LOGGER.error("Error inesperado en LogFilter", e);
        } finally {
            // Limpia el ThreadContext para evitar contaminar otros hilos
            ThreadContext.remove("idSesion");
            ThreadContext.remove("idTransaccion");
        }
    }
}