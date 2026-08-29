package com.davivienda.sv.app.dto;

import com.davivienda.sv.app.entities.db2.TransaccionDTO;

import java.util.List;

public class Transacciones {
	List<TransaccionDTO> transacciones;

	public List<TransaccionDTO> getTransacciones() {
		return transacciones;
	}

	public void setTransacciones(List<TransaccionDTO> transacciones) {
		this.transacciones = transacciones;
	}
}
