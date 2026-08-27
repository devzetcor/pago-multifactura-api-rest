package com.davivienda.sv.app.controllers.exception;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.davivienda.sv.app.data.beans.Request;
import com.davivienda.sv.app.data.beans.RequestHeader;
import com.davivienda.sv.app.data.beans.Response;
import com.davivienda.sv.app.data.beans.ResponseHeader;
import com.davivienda.sv.app.util.AppException;
import com.davivienda.sv.app.util.ErrorResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LogManager.getLogger(ResponseExceptionHandler.class);

    private Response<ErrorResponse> buildErrorResponse(ErrorResponse response, WebRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        Request<Object> appRequest = new Request<Object>();

        if (request instanceof ServletWebRequest) {
            HttpServletRequest httpRequest = ((ServletWebRequest) request).getRequest();
            String path = httpRequest.getServletPath();
            response.setPath(path);
            try {
                StringBuilder sb = new StringBuilder();
                String line;
                BufferedReader reader = httpRequest.getReader();

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                Map<String, Object> bodyMap = objectMapper.readValue(sb.toString(),
                        new TypeReference<Map<String, Object>>() {
                            {
                            }
                        });
                Object headerObj = bodyMap.getOrDefault("header", "{}");

                Map<String, Object> headerMap = objectMapper.convertValue(headerObj,
                        new TypeReference<Map<String, Object>>() {
                        });

                RequestHeader requestHeader = objectMapper.convertValue(headerMap, RequestHeader.class);
                appRequest.setHeader(requestHeader);
            } catch (IOException e) {
                appRequest.setHeader(new RequestHeader());
                appRequest.setBody(null);
            }
        } else {
            appRequest.setHeader(new RequestHeader());
            appRequest.setBody(null);
        }

        Response<ErrorResponse> errorResponse = new Response<>(appRequest, new ErrorResponse());
        ResponseHeader header = errorResponse.getHeader();
        header.setCodigo(response.getCode());
        header.setDescripcion(response.getMessage());

        errorResponse.setHeader(header);
        errorResponse.setBody(response);
        return errorResponse;
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> handleAppException(AppException ex, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getStatusCode(),
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                request.getContextPath());

        Response<ErrorResponse> errorResponse = buildErrorResponse(response, request);

        return ResponseEntity.status(
                HttpStatus.valueOf(response.getHttpStatusCode())).body(errorResponse);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<?> handleSqlException(SQLException ex, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getErrorCode(),
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                request.getContextPath());

        Response<ErrorResponse> errorResponse = buildErrorResponse(response, request);

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(response.getHttpStatusCode()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleValidationException(ConstraintViolationException ex,
            WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getErrorCode(),
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                request.getContextPath());

        Response<ErrorResponse> errorResponse = buildErrorResponse(response, request);

        return ResponseEntity.status(
                HttpStatus.valueOf(response.getHttpStatusCode())).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
            WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                -1,
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                request.getContextPath());

        Response<ErrorResponse> errorResponse = buildErrorResponse(response, request);

        return ResponseEntity.status(
                HttpStatus.valueOf(response.getHttpStatusCode())).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllExceptions(Exception ex, WebRequest request) {
    	LOGGER.error("Error no controlado capturado por Global Exception Handler: " + ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                -1,
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                request.getContextPath());

        Response<ErrorResponse> errorResponse = buildErrorResponse(response, request);

        return ResponseEntity.status(
                HttpStatus.valueOf(response.getHttpStatusCode())).body(errorResponse);
    }
}
