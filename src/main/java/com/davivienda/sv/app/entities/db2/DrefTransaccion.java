package com.davivienda.sv.app.entities.db2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DrefTransaccion {
    private Integer idTransaccion;
    private LocalDateTime fechaCreacion;
    private BigDecimal montoTotal;
    private String estado;
    private String usuarioCreacion;
    private LocalDateTime fechaAprobacion;
    private String usuarioAprobacion;
    private Integer idColector;
    private String cuentaAbono;
    private String tipoCuentaAbono;
    private String cuentaCargo;
    private String tipoCuentaCargo;
    private Integer categoria;
    private Integer empresa;
    private String nombreCategoria;
    private String nombreColector;
    private String cuentaContable;
    private String motivoRechazo;
    private EnrolamientoColector enrolamientoColector;
    private Set<DetalleTransaccionAutorizacion> firmas;
    private Set<DrefFacturaTransaccion> facturas;

}