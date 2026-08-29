package com.davivienda.sv.app.util;

import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.RequestHeader;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;

@Component
public class IpInjectorRequestBodyAdvice implements RequestBodyAdvice {

    private final HttpServletRequest httpServletRequest;

    public IpInjectorRequestBodyAdvice(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                             Class<? extends HttpMessageConverter<?>> converterType) {
        return Request.class.isAssignableFrom((Class<?>) targetType);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        if (body instanceof Request) {
            Request requestBody = (Request) body;
            String clientIp = getClientIpAddress(httpServletRequest);

            RequestHeader header = requestBody.getHeader();
            if (header == null) {
                header = new RequestHeader();
                requestBody.setHeader(header);
            }
            header.setIp(clientIp);
        }
        return body;
    }

    @Override
    @Nullable
    public Object handleEmptyBody(@Nullable Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                  Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
