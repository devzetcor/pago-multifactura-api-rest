package com.davivienda.sv.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutorizacionResultadoDto {
    private boolean autorizado;
    private Long definicionAutorizacion;
    private String creadoPor;
    private List<InfoUsuarioAutorizacionDto> usuariosFirmantes;
}
