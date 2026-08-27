package com.davivienda.sv.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AutorizacionRequestDto {
    private Integer idTransaccion;
    private String usuario;
    private Integer estado;
    private long clienteId;
    private Long producto;
}
