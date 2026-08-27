package com.davivienda.sv.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumenTransaccionesPorColectorDto {
    private Long empresa;
    private String nombreEmpresa;
    private Long colector;
    private String nombreColector;
    private int cantidadTransaccionesPendientes;
}