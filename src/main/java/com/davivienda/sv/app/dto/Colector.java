package com.davivienda.sv.app.dto;

public class Colector {
	private String idColector;
	private String nombre;
	private String idCategoriaColector;
	private String nombreCategoria;
	private String prefijoNPE;
	private String prefijoBarra;
	private String idTipologia;
	private String descripcionTipologia;
	private String referenciaAudioIVR;
	private String referenciaImagen;
	private String niu;

	public Colector() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Colector(String idColector, String nombre, String idCategoriaColector, String nombreCategoria,
			String prefijoNPE, String prefijoBarra, String idTipologia, String descripcionTipologia,
			String referenciaAudioIVR, String referenciaImagen, String niu) {
		super();
		this.idColector = idColector;
		this.nombre = nombre;
		this.idCategoriaColector = idCategoriaColector;
		this.nombreCategoria = nombreCategoria;
		this.prefijoNPE = prefijoNPE;
		this.prefijoBarra = prefijoBarra;
		this.idTipologia = idTipologia;
		this.descripcionTipologia = descripcionTipologia;
		this.referenciaAudioIVR = referenciaAudioIVR;
		this.referenciaImagen = referenciaImagen;
		this.niu = niu;
	}

	public String getIdColector() {
		return idColector;
	}

	public void setIdColector(String idColector) {
		this.idColector = idColector;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getIdCategoriaColector() {
		return idCategoriaColector;
	}

	public void setIdCategoriaColector(String idCategoriaColector) {
		this.idCategoriaColector = idCategoriaColector;
	}

	public String getNombreCategoria() {
		return nombreCategoria;
	}

	public void setNombreCategoria(String nombreCategoria) {
		this.nombreCategoria = nombreCategoria;
	}

	public String getPrefijoNPE() {
		return prefijoNPE;
	}

	public void setPrefijoNPE(String prefijoNPE) {
		this.prefijoNPE = prefijoNPE;
	}

	public String getPrefijoBarra() {
		return prefijoBarra;
	}

	public void setPrefijoBarra(String prefijoBarra) {
		this.prefijoBarra = prefijoBarra;
	}

	public String getIdTipologia() {
		return idTipologia;
	}

	public void setIdTipologia(String idTipologia) {
		this.idTipologia = idTipologia;
	}

	public String getDescripcionTipologia() {
		return descripcionTipologia;
	}

	public void setDescripcionTipologia(String descripcionTipologia) {
		this.descripcionTipologia = descripcionTipologia;
	}

	public String getReferenciaAudioIVR() {
		return referenciaAudioIVR;
	}

	public void setReferenciaAudioIVR(String referenciaAudioIVR) {
		this.referenciaAudioIVR = referenciaAudioIVR;
	}

	public String getReferenciaImagen() {
		return referenciaImagen;
	}

	public void setReferenciaImagen(String referenciaImagen) {
		this.referenciaImagen = referenciaImagen;
	}

	public String getNiu() {
		return niu;
	}

	public void setNiu(String niu) {
		this.niu = niu;
	}

}
