package com.davivienda.sv.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespuetaEnrolamientoColector {
	List<EnrolamientoColectorDTO> atributos;
}
