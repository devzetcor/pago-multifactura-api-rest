package com.davivienda.sv.app.dto.colecturia.lista;

import com.davivienda.sv.app.util.SubContenedor;

@SubContenedor
public class PeticionConsultaColectores {
	private String codigoCanal;
	private String categoria;
	private String npe;
	private String barra;
	private String flagSinNPE;
	private String idcolector;

	public PeticionConsultaColectores() {
		super();
	}

	public PeticionConsultaColectores(String codigoCanal, String categoria, String npe, String barra, String flagSinNPE, String idcolector) {
		super();
		this.codigoCanal = codigoCanal;
		this.categoria = categoria;
		this.npe = npe;
		this.barra = barra;
		this.flagSinNPE = flagSinNPE;
		this.idcolector = idcolector;
	}

	public String getCodigoCanal() {
		return codigoCanal;
	}

	public void setCodigoCanal(String codigoCanal) {
		this.codigoCanal = codigoCanal;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getNpe() {
		return npe;
	}

	public void setNpe(String npe) {
		this.npe = npe;
	}

	public String getBarra() {
		return barra;
	}

	public void setBarra(String barra) {
		this.barra = barra;
	}

	public String getFlagSinNPE() {
		return flagSinNPE;
	}

	public void setFlagSinNPE(String flagSinNPE) {
		this.flagSinNPE = flagSinNPE;
	}

	public String getIdcolector() {
		return idcolector;
	}

	public void setIdcolector(String idcolector) {
		this.idcolector = idcolector;
	}

}
