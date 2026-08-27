package com.davivienda.sv.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClienteDto {

    private Long cliente;
    private String usuario;
    private String nombre;
    private Integer modulos;
    private String direccion;
    private String telefono;
    private String fax;
    private String email;
    private String nombreContacto;
    private String apellidoContacto;

}
