package com.davivienda.sv.app.util;

public enum TransactionStatus {
    APROBADA("APROBADA", "APROBADAS"),
    RECHAZADA("RECHAZADA", "RECHAZADAS"),
    PAGADA_CON_ERROR("PAGADA_CON_ERROR", "INCONSISTENTES"),
    PROCESANDO("PROCESANDO", "PROCESANDO"),
    PENDIENTE("PENDIENTE", "PENDIENTES"),
    PAGADA("PAGADA", "PAGADAS");

    private final String status;
    private final String pathKey;

    TransactionStatus(String status, String pathKey) {
        this.status = status;
        this.pathKey = pathKey;
    }

    public String getStatus() {
        return status;
    }

    public String getPathKey() {
        return pathKey;
    }

    public static TransactionStatus fromStatus(String status) {
        for (TransactionStatus ts : values()) {
            if (ts.getStatus().equalsIgnoreCase(status)) {
                return ts;
            }
        }
        return null;
    }

    public static TransactionStatus fromPathKey(String pathKey) {
        for (TransactionStatus ts : values()) {
            if (ts.getPathKey().equalsIgnoreCase(pathKey)) {
                return ts;
            }
        }
        return null;
    }
}
