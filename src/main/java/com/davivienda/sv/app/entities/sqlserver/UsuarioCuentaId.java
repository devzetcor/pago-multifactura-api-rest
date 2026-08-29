package com.davivienda.sv.app.entities.sqlserver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCuentaId implements Serializable { 
	private static final long serialVersionUID = 1L;
	private Long cliente;
    private String usuario;
    private String cuenta;
}