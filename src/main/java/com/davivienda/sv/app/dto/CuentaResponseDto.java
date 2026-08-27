package com.davivienda.sv.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import com.davivienda.sv.app.entities.sqlserver.TipoCuenta;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaResponseDto {
    private String cuenta;
    private Long cliente;
    private TipoCuenta tipoCuenta;
    private String tipoCuentaNombre;
    private String nombre;
    private String aliasCuenta;
    private String estatus;
    private Integer estatusHost;
    private String moneda;
    private BigDecimal limite;
    private LocalDateTime fechaOtorgado;
    private LocalDateTime fechaVencimiento;
    private Boolean esInteligente;
    private Boolean esCrediExpress;
    private String permisos1;
    private String permisos2;
    private String permisos3;
    private String permisos4;
    private String permisos5;
}