package com.davivienda.sv.app.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private int httpStatusCode;
    private int code;
    private String message;
    private String error;
    private String path;
    private LocalDateTime timestamp;

    public ErrorResponse(
        int httpStatusCode,
        int status,
        String message,
        String error,
        String path
    ) {
        this.timestamp = LocalDateTime.now();
        this.code = status;
        this.error = error;
        this.path = path;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

}
