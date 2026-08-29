package com.davivienda.sv.app.entities.db2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DetalleTransaccionAutorizacion {

    private Long id;
    private Integer idRol;
    private Integer nivel;
    private String usuario;
    private Integer cliente;
    private LocalDateTime fechaEstado;
    private Integer estado;
    private Integer definicionAutorizacion;
    private DrefTransaccion transaccion;

    public DetalleTransaccionAutorizacion(
        DrefTransaccion transaccion, 
        Integer idRol, 
        String usuario, 
        Integer cliente,
        Integer definicionAutorizacion
    ) {
        this.transaccion = transaccion;
        this.nivel = 1; // Default value for nivel
        this.fechaEstado = LocalDateTime.now(); // Default to current time
        this.estado = 1; // Default value for estado (e.g., active)
        this.idRol = idRol;
        this.usuario = usuario;
        this.cliente = cliente;
        this.definicionAutorizacion = definicionAutorizacion;
    }

}
