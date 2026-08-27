package com.davivienda.sv.app.data.beans.token;

import lombok.Data;

@Data
public class GenerarOtpRequest {
	private long niu;
	private String esOperador;
	private String niuString;
	
	public long getNiu() {
		return niu;
	}

	public void setNiu(long niu) {
		this.niu = niu;
	}

	
	public String getEsOperador() {
		return esOperador;
	}

	public void setEsOperador(String esOperador) {
		this.esOperador = esOperador;
	}

	public String getNiuString() {
		return niuString;
	}

	public void setNiuString(String niuString) {
		this.niuString = niuString;
	}

	@Override
	public String toString() {
		return "GenerarOtpRequest [niu=" + niu + "]";
	}
}
