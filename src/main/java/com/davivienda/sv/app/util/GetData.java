package com.davivienda.sv.app.util;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Interfaz para obtener la data de la peticion
 * 
 * @author Christian Guillen
 * @since 25 ago 2023
 * @version 1.0
 * @param <T> Data
 */
public interface GetData<T> {
 
	/**
	 * Obtiene el objeto data
	 * 
	 * @return
	 */
	@JsonIgnore
	public T getData();
	
	/**
	 * Establece el objeto data
	 * @param data
	 */
	@JsonIgnore
	public void setData(T data);
	
}
