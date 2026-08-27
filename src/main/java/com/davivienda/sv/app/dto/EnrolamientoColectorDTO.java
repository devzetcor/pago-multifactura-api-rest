package com.davivienda.sv.app.dto;

import lombok.Data;

@Data
public class EnrolamientoColectorDTO {

    private Long id;
    private Long idEmpresa;
    private Long idColector;
    private Long idAtributo;
    private String valor;
    private String descripcion;
}	