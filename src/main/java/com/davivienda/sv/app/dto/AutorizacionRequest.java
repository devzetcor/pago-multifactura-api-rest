package com.davivienda.sv.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AutorizacionRequest {
    private long clienteId;
    private Long producto;
    private List<Long> rolesFirmados;
}
