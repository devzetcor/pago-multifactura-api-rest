package com.davivienda.sv.app.dto;

import java.util.List;

public class RespuestaConsultaColectoresDto {
	List<Colector> colectores;

	public RespuestaConsultaColectoresDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RespuestaConsultaColectoresDto(List<Colector> colectores) {
		super();
		this.colectores = colectores;
	}

	public List<Colector> getColectores() {
		return colectores;
	}

	public void setColectores(List<Colector> colectores) {
		this.colectores = colectores;
	}

}
