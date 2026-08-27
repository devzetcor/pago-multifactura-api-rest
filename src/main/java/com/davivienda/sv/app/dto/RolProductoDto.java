package com.davivienda.sv.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolProductoDto {
    private String nombreRol;
    private Integer producto;
    private Integer rol;
}
