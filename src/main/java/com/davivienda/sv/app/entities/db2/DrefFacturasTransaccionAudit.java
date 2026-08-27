package com.davivienda.sv.app.entities.db2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DrefFacturasTransaccionAudit {
    private Long auditId;
    private Integer idDetalle;
    private Integer idTransaccion;
    private String numeroFactura;
    private BigDecimal monto;
    private String colector;
    private String referencia;
    private Timestamp fechaVencimiento;
    private String nombreCliente;
    private String npe;
    private String estado;
    private String descripcionError;
    private Timestamp fechaActualizacion;
    private Timestamp auditFecha;
    private String auditAccion;

}
