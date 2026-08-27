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
public class AutorizacionRequest {
    private long clienteId;
    private Long producto;
    private List<Long> rolesFirmados;
}
