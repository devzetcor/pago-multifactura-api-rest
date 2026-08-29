package com.davivienda.sv.app.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RequestLoggerInterceptor implements HandlerInterceptor {
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = getClientIpAddress(request);
        String timestamp = LocalDateTime.now().format(formatter);
        String requestMethod = request.getMethod();
        String requestUri = request.getRequestURI();
        
        request.setAttribute("X-Request-IP", clientIp);
        request.setAttribute("X-Request-Timestamp", timestamp);
        request.setAttribute("X-Request-Method", requestMethod);
        request.setAttribute("X-Request-Path", requestUri);
        
        response.addHeader("X-Request-IP", clientIp);
        response.addHeader("X-Request-Timestamp", timestamp);
        response.addHeader("X-Request-Method", requestMethod);
        response.addHeader("X-Request-Path", requestUri);
        
        return true;
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        
        return ip;
    }
}