package com.davivienda.sv.app.entities.db2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DetalleTransaccionAutorizacionAudit {
    private Long auditId;
    private Long id;
    private Integer idTransaccion;
    private Integer idRol;
    private Integer nivel;
    private String usuario;
    private Integer cliente;
    private Timestamp fechaEstado;
    private Integer estado;
    private Integer definicionAutorizacion;
    private Timestamp auditFecha;
    private String auditAccion;

}
