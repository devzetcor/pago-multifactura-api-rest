package com.davivienda.sv.app.util;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Clase que define los metodos para definir codigo y mensaje de respuesta
 *
 * @author Christian Guillen
 * @since 4 sep. 2024
 * @version 1.0
 *
 */
public interface GetStatusResponse {

	@JsonIgnore
	public String getCodigo();

	@JsonIgnore
	public void setCodigo(String codigo);

	@JsonIgnore
	public String getDescripcion();
	
	@JsonIgnore
	public void setDescripcion(String descripcion);
}
