package com.davivienda.sv.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransaccionResumenDto {
    private Long idTransaccion;
    private LocalDateTime fechaCreacion;
    private BigDecimal montoTotal;
    private String estado;
    private String usuarioCreacion;
    private Long idColector;
    private String nombreColector;
    private Long empresa;
    private int cantidadFacturas;
}