package com.davivienda.sv.app.dto.colecturia.detalle;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.ToString;

import java.util.List;

@ToString
public class AtributoColectorFull {
	private String valorAtributoPantalla="";
	private String srvValidacion;
	private String idAtributo;
	private String tipoDato;
	private String leyendaDespliegue;
	private String secDespliegue;
	private String esLlaveBusqueda;
	private String esObligatorio;
	private String pedirPantalla;
	private String nombre;

	@JacksonXmlElementWrapper(localName = "valorPosibleAtributo",useWrapping = false)
	@JacksonXmlProperty(localName = "valorPosibleAtributo")
	private List<ValorPosibleAtributo> valorPosibleAtributo;

	public AtributoColectorFull() {
	}

	public AtributoColectorFull(String valorAtributoPantalla, String srvValidacion, String idAtributo, String tipoDato,
			String leyendaDespliegue, String secDespliegue, String esLlaveBusqueda, String esObligatorio,
			String pedirPantalla, String nombre, List<ValorPosibleAtributo> valorPosibleAtributo) {
		super();
		this.valorAtributoPantalla = valorAtributoPantalla;
		this.srvValidacion = srvValidacion;
		this.idAtributo = idAtributo;
		this.tipoDato = tipoDato;
		this.leyendaDespliegue = leyendaDespliegue;
		this.secDespliegue = secDespliegue;
		this.esLlaveBusqueda = esLlaveBusqueda;
		this.esObligatorio = esObligatorio;
		this.pedirPantalla = pedirPantalla;
		this.nombre = nombre;
		this.valorPosibleAtributo = valorPosibleAtributo;
	}

	public String getValorAtributoPantalla() {
		return valorAtributoPantalla;
	}

	public void setValorAtributoPantalla(String valorAtributoPantalla) {
		this.valorAtributoPantalla = valorAtributoPantalla;
	}

	

	public String getSrvValidacion() {
		return srvValidacion;
	}

	public void setSrvValidacion(String srvValidacion) {
		this.srvValidacion = srvValidacion;
	}

	public void setValorPosibleAtributo(List<ValorPosibleAtributo> valorPosibleAtributo) {
		this.valorPosibleAtributo = valorPosibleAtributo;
	}

	public String getIdAtributo() {
		return idAtributo;
	}

	public void setIdAtributo(String idAtributo) {
		this.idAtributo = idAtributo;
	}

	public String getTipoDato() {
		return tipoDato;
	}

	public void setTipoDato(String tipoDato) {
		this.tipoDato = tipoDato;
	}

	public String getLeyendaDespliegue() {
		return leyendaDespliegue;
	}

	public void setLeyendaDespliegue(String leyendaDespliegue) {
		this.leyendaDespliegue = leyendaDespliegue;
	}

	public String getSecDespliegue() {
		return secDespliegue;
	}

	public void setSecDespliegue(String secDespliegue) {
		this.secDespliegue = secDespliegue;
	}

	public String getEsLlaveBusqueda() {
		return esLlaveBusqueda;
	}

	public void setEsLlaveBusqueda(String esLlaveBusqueda) {
		this.esLlaveBusqueda = esLlaveBusqueda;
	}

	public String getEsObligatorio() {
		return esObligatorio;
	}

	public void setEsObligatorio(String esObligatorio) {
		this.esObligatorio = esObligatorio;
	}

	public String getPedirPantalla() {
		return pedirPantalla;
	}

	public void setPedirPantalla(String pedirPantalla) {
		this.pedirPantalla = pedirPantalla;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<ValorPosibleAtributo> getValorPosibleAtributo() {
		return valorPosibleAtributo;
	}

	public void setValorPosibleAtributos(List<ValorPosibleAtributo> valorPosibleAtributos) {
		this.valorPosibleAtributo = valorPosibleAtributos;
	}

}