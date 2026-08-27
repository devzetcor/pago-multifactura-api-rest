package com.davivienda.sv.app.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InfoUsuarioDto {
    private Long cliente;
    private String nombre;
    private String apellido;
    private String email;
    private String numeroDocumento;
    private String puesto;
    private Integer tipoDocumento;
    private String usuario;
    private String estado;
    private List<RolProductoDto> roles;
}
