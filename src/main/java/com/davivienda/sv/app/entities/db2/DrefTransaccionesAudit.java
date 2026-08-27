package com.davivienda.sv.app.entities.db2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class DrefTransaccionesAudit {
    private Long auditId;
    private Integer idTransaccion;
    private Timestamp fechaCreacion;
    private BigDecimal montoTotal;
    private String estado;
    private String usuarioCreacion;
    private Timestamp fechaAprobacion;
    private String usuarioAprobacion;
    private Integer idColector;
    private String cuentaAbono;
    private String tipoCuentaAbono;
    private String cuentaCargo;
    private String tipoCuentaCargo;
    private String cuentaPago;
    private String tipoCuentaPago;
    private Integer categoria;
    private Integer column1;
    private Integer column2;
    private Integer empresa;
    private String nombreCategoria;
    private String nombreColector;
    private String cuentaContable;
    private String motivoRechazo;
    private Timestamp auditFecha;
    private String auditAccion;
}
