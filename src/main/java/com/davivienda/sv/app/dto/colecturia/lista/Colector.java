package com.davivienda.sv.app.dto.colecturia.lista;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Colector {
	@JacksonXmlProperty(localName = "numCtaAbono")
	private String numCtaAbono;
	@JacksonXmlProperty(localName = "numProveedor")
	private String numProveedor;
	@JacksonXmlProperty(localName = "poseeCodigoBarra")
	private String poseeCodigoBarra;
	@JacksonXmlProperty(localName = "idColector")
	private String idColector;
	@JacksonXmlProperty(localName = "nombre")
	private String nombre;
	@JacksonXmlProperty(localName = "idCategoriaColector")
	private String idCategoriaColector;
	@JacksonXmlProperty(localName = "nombreCategoria")
	private String nombreCategoria;
	@JacksonXmlProperty(localName = "prefijoNPE")
	private String prefijoNPE;
	@JacksonXmlProperty(localName = "prefijoBarra")
	private String prefijoBarra;
	@JacksonXmlProperty(localName = "idTipologia")
	private String idTipologia;
	@JacksonXmlProperty(localName = "descripcionTipologia")
	private String descripcionTipologia;
	@JacksonXmlProperty(localName = "referenciaAudioIVR")
	private String referenciaAudioIVR;
	@JacksonXmlProperty(localName = "referenciaImagen")
	private String referenciaImagen;
	@JacksonXmlProperty(localName = "niu")
	private String niu;

	public Colector() {
	}

	

	public Colector(String numCtaAbono, String numProveedor, String poseeCodigoBarra, String idColector, String nombre,
			String idCategoriaColector, String nombreCategoria, String prefijoNPE, String prefijoBarra,
			String idTipologia, String descripcionTipologia, String referenciaAudioIVR, String referenciaImagen,
			String niu) {
		super();
		this.numCtaAbono = numCtaAbono;
		this.numProveedor = numProveedor;
		this.poseeCodigoBarra = poseeCodigoBarra;
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



	public String getNumCtaAbono() {
		return numCtaAbono;
	}



	public void setNumCtaAbono(String numCtaAbono) {
		this.numCtaAbono = numCtaAbono;
	}



	public String getNumProveedor() {
		return numProveedor;
	}



	public void setNumProveedor(String numProveedor) {
		this.numProveedor = numProveedor;
	}



	public String getPoseeCodigoBarra() {
		return poseeCodigoBarra;
	}



	public void setPoseeCodigoBarra(String poseeCodigoBarra) {
		this.poseeCodigoBarra = poseeCodigoBarra;
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