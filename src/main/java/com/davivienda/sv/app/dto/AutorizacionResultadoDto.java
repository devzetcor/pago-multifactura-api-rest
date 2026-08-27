package com.davivienda.sv.app.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutorizacionResultadoDto {
    private boolean autorizado;
    private Long definicionAutorizacion;
    private String creadoPor;
    private List<InfoUsuarioAutorizacionDto> usuariosFirmantes;
}
