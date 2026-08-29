package com.davivienda.sv.app.entities.db2;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FacturaTransaccion {
    private Long idDetalle;
    private Long idTransaccion;
    private String numeroFactura;
    private String referencia;
    private BigDecimal monto;
    private LocalDate fechaVencimiento;
    private String nombreCliente;
    private String estado;
    private String descripcionError;
    private String fechaActualizacion;
    private String npe;
}