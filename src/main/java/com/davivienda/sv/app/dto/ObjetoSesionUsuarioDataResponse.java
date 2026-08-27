package com.davivienda.sv.app.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ObjetoSesionUsuarioDataResponse {
	
	@JsonProperty("tipoIdentificacion")
    private String tipoIdentificacion;
    
    @JsonProperty("numeroIdentificacion")
    private String numeroIdentificacion;
    
    @JsonProperty("numeroCliente")
    private String numeroCliente;
    
    @JsonProperty("segmento")
    private String segmento;
    
    @JsonProperty("tipoIdentificacionOperador")
    private String tipoIdentificacionOperador;
    
    @JsonProperty("numeroIdentificacionOperador")
    private String numeroIdentificacionOperador;
    
    @JsonProperty("idioma")
    private String idioma;

	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	public String getNumeroCliente() {
		return numeroCliente;
	}

	public void setNumeroCliente(String numeroCliente) {
		this.numeroCliente = numeroCliente;
	}

	public String getSegmento() {
		return segmento;
	}

	public void setSegmento(String segmento) {
		this.segmento = segmento;
	}

	public String getTipoIdentificacionOperador() {
		return tipoIdentificacionOperador;
	}

	public void setTipoIdentificacionOperador(String tipoIdentificacionOperador) {
		this.tipoIdentificacionOperador = tipoIdentificacionOperador;
	}

	public String getNumeroIdentificacionOperador() {
		return numeroIdentificacionOperador;
	}

	public void setNumeroIdentificacionOperador(String numeroIdentificacionOperador) {
		this.numeroIdentificacionOperador = numeroIdentificacionOperador;
	}

	public String getIdioma() {
		return idioma;
	}

	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}

	@Override
	public String toString() {
		return "ObtenerObjetoSesionUsuario [tipoIdentificacion=" + tipoIdentificacion + ", numeroIdentificacion="
				+ numeroIdentificacion + ", numeroCliente=" + numeroCliente + ", segmento=" + segmento
				+ ", tipoIdentificacionOperador=" + tipoIdentificacionOperador + ", numeroIdentificacionOperador="
				+ numeroIdentificacionOperador + ", idioma=" + idioma + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(idioma, numeroCliente, numeroIdentificacion, numeroIdentificacionOperador, segmento,
				tipoIdentificacion, tipoIdentificacionOperador);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObjetoSesionUsuarioDataResponse other = (ObjetoSesionUsuarioDataResponse) obj;
		return Objects.equals(idioma, other.idioma) && Objects.equals(numeroCliente, other.numeroCliente)
				&& Objects.equals(numeroIdentificacion, other.numeroIdentificacion)
				&& Objects.equals(numeroIdentificacionOperador, other.numeroIdentificacionOperador)
				&& Objects.equals(segmento, other.segmento)
				&& Objects.equals(tipoIdentificacion, other.tipoIdentificacion)
				&& Objects.equals(tipoIdentificacionOperador, other.tipoIdentificacionOperador);
	}

}
