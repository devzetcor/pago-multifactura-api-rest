package com.davivienda.sv.app.dto;

import java.util.List;

import com.davivienda.sv.app.entities.db2.TransaccionDTO;

public class Transacciones {
	List<TransaccionDTO> transacciones;

	public List<TransaccionDTO> getTransacciones() {
		return transacciones;
	}

	public void setTransacciones(List<TransaccionDTO> transacciones) {
		this.transacciones = transacciones;
	}
}
