package com.davivienda.sv.app.dto.colecturia.detalle;

public class Canal {
    private String codigoCanal;
    private String idCanal;
    private String codigoIBS;
    private String motivo;
    private String lote;
    private String descripcion;
	public Canal() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Canal(String codigoCanal, String idCanal, String codigoIBS, String motivo, String lote, String descripcion) {
		super();
		this.codigoCanal = codigoCanal;
		this.idCanal = idCanal;
		this.codigoIBS = codigoIBS;
		this.motivo = motivo;
		this.lote = lote;
		this.descripcion = descripcion;
	}

	public String getCodigoCanal() {
		return codigoCanal;
	}
	public void setCodigoCanal(String codigoCanal) {
		this.codigoCanal = codigoCanal;
	}
	public String getIdCanal() {
		return idCanal;
	}
	public void setIdCanal(String idCanal) {
		this.idCanal = idCanal;
	}
	public String getCodigoIBS() {
		return codigoIBS;
	}
	public void setCodigoIBS(String codigoIBS) {
		this.codigoIBS = codigoIBS;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public String getLote() {
		return lote;
	}
	public void setLote(String lote) {
		this.lote = lote;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

   
}
