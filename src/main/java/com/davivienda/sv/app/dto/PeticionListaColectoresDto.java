package com.davivienda.sv.app.dto;

public class PeticionListaColectoresDto {
	private String codigoCanal;
	private String flagSinNPE;

	public PeticionListaColectoresDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PeticionListaColectoresDto(String codigoCanal, String flagSinNPE) {
		super();
		this.codigoCanal = codigoCanal;
		this.flagSinNPE = flagSinNPE;
	}

	public String getCodigoCanal() {
		return codigoCanal;
	}

	public void setCodigoCanal(String codigoCanal) {
		this.codigoCanal = codigoCanal;
	}

	public String getFlagSinNPE() {
		return flagSinNPE;
	}

	public void setFlagSinNPE(String flagSinNPE) {
		this.flagSinNPE = flagSinNPE;
	}

}
