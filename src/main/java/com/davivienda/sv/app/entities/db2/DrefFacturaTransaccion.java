package com.davivienda.sv.app.entities.db2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DrefFacturaTransaccion {
    private Integer idDetalle;
    private String numeroFactura;
    private BigDecimal monto;
    private String colector;
    private String referencia;
    private LocalDateTime fechaVencimiento;
    private String nombreCliente;
    private String npe;
    private String estado;
    private String descripcionError;
    private LocalDateTime fechaActualizacion;
    private DrefTransaccion transaccion;
}