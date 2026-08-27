package com.davivienda.sv.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CuentaCargoResponseDto {
    private String cuentaCargo;
    private String tipoCuentaCargo;
    private Long idTransaccionReferencia;
    private LocalDateTime fechaUltimaTransaccion;
    private String nombreColector;
    private Long empresa;
    private String usuarioCreacion;
}