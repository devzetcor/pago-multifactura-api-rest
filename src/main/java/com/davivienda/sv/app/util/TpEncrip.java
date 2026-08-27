package com.davivienda.sv.app.util;

/**
 * equivalente para descencriptar los datos.
 * 
 * @author smarroquin
 *
 */
public enum TpEncrip {

	//DOS_PUNTOS(":", "|TwoP"), PUNTO_COMA(";", "|TwoC"), IGUAL("=", "|Eq"), INTEROGANTE("?", "|SigPr"), INVERSION("/", "|Inver"), NUMERAL("#", "|Numer"), PORCENTAJE("%", "|Porce"), PARENTESI_UNO("(", "|ParentO"), PARENTESI_DOS(")", "|ParentT");
	MAS("+", "\\|Plus");
	
	private String key;
	private String valor;

	TpEncrip(String key, String valor) {
		this.key = key;
		this.valor = valor;
	}

	public String getKey() {
		return key;
	}

	public String getValor() {
		return valor;
	}

}
