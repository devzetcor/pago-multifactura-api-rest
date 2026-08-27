package com.davivienda.sv.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultaCuentaCargoRequestDto {
    private Long idColector;
    private Long idEmpresa;
    private String usuario;
}