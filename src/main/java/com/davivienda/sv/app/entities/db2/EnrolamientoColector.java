package com.davivienda.sv.app.entities.db2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EnrolamientoColector {
    private Long id;
    private Long idEmpresa;
    private Long idColector;
    private Long idAtributo;
    private String valor;
    private String descripcion;
}