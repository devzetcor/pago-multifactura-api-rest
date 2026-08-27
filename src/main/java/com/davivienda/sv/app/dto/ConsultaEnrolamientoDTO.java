package com.davivienda.sv.app.dto;

import lombok.Data;

@Data
public class ConsultaEnrolamientoDTO {
    private Long idEmpresa;
    private Long idColector;
    private String usuario;
    private String motivoRechazo;
    private String usuarioAprobacion;
    private TransaccionIdsDTO transacciones;
}