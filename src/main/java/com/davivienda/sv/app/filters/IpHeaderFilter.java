package com.davivienda.sv.app.filters;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.davivienda.sv.app.security.CachedHttpServletRequest;
import com.davivienda.sv.app.security.ModifiedBodyHttpServletRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.filter.OncePerRequestFilter;

public class IpHeaderFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LogManager.getLogger(IpHeaderFilter.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest httpRequest,
                                    HttpServletResponse httpResponse,
                                    FilterChain chain) throws ServletException, IOException {
        try {
            // Obtener la IP del cliente
            String clientIp = getClientIpAddress(httpRequest);
            LOGGER.info("IP del cliente detectada: " + clientIp);

            // Envolver el request para poder modificar el cuerpo
            CachedHttpServletRequest cachedRequest = new CachedHttpServletRequest(httpRequest);

            // Procesar y modificar el JSON del request
            String modifiedJson = processAndSetIp(cachedRequest, clientIp);

            if (modifiedJson != null) {
                // Crear un nuevo wrapper con el JSON modificado
                ModifiedBodyHttpServletRequest modifiedRequest =
                        new ModifiedBodyHttpServletRequest(cachedRequest, modifiedJson);
                chain.doFilter(modifiedRequest, httpResponse);
            } else {
                // Si no se pudo procesar, continuar con el request original
                chain.doFilter(cachedRequest, httpResponse);
            }

        } catch (Exception e) {
            LOGGER.error("Error en IpHeaderFilter: " + e.getMessage(), e);
            chain.doFilter(httpRequest, httpResponse);
        }
    }

    private String processAndSetIp(CachedHttpServletRequest cachedRequest, String clientIp) {
        try {
            String originalJson = cachedRequest.getJson();
            if (originalJson == null || originalJson.trim().isEmpty()) {
                return null;
            }

            // Parsear el JSON completo
            Map<String, Object> requestMap = objectMapper.readValue(originalJson,
                    new TypeReference<Map<String, Object>>() {});

            // Obtener o crear el objeto header
            Map<String, Object> headerMap = (Map<String, Object>) requestMap.get("header");
            if (headerMap == null) {
                headerMap = new java.util.HashMap<>();
                requestMap.put("header", headerMap);
            }

            // Setear la IP en el header
            headerMap.put("ip", clientIp);

            // Convertir de vuelta a JSON
            String modifiedJson = objectMapper.writeValueAsString(requestMap);
            LOGGER.info("JSON modificado con IP: " + clientIp);

            return modifiedJson;

        } catch (Exception e) {
            LOGGER.error("Error al procesar JSON para setear IP: " + e.getMessage(), e);
            return null;
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }

        String xForwardedForCloudflare = request.getHeader("CF-Connecting-IP");
        if (xForwardedForCloudflare != null && !xForwardedForCloudflare.isEmpty()) {
            return xForwardedForCloudflare;
        }

        String xForwardedForAws = request.getHeader("X-Forwarded-For-AWS");
        if (xForwardedForAws != null && !xForwardedForAws.isEmpty()) {
            return xForwardedForAws;
        }

        return request.getRemoteAddr();
    }
}