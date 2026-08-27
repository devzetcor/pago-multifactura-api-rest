package com.davivienda.sv.app.entities.db2;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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