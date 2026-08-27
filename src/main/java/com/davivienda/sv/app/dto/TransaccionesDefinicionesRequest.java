package com.davivienda.sv.app.dto;

import java.util.List;

public  class TransaccionesDefinicionesRequest {
    private List<Integer> transacciones;
    private List<Integer> definiciones;

    public TransaccionesDefinicionesRequest(List<Integer> transacciones, List<Integer> definiciones) {
		super();
		this.transacciones = transacciones;
		this.definiciones = definiciones;
	}
	// Getters y Setters
    public List<Integer> getTransacciones() { return transacciones; }
    public void setTransacciones(List<Integer> transacciones) { this.transacciones = transacciones; }

    public List<Integer> getDefiniciones() { return definiciones; }
    public void setDefiniciones(List<Integer> definiciones) { this.definiciones = definiciones; }
}