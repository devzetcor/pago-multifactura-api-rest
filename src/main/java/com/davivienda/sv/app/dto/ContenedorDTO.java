package com.davivienda.sv.app.dto;

	public class ContenedorDTO {
	    private String ip;
	    private String idTransaccion;
	    private String idSesion;
	    private String token;

	    // Constructor vacío (opcional, pero recomendado)
	    public ContenedorDTO() {
	    }

	    // Constructor con argumentos para inicializar los atributos (opcional)
	    public ContenedorDTO(String ip, String idTransaccion, String idSesion, String token) {
	        this.ip = ip;
	        this.idTransaccion = idTransaccion;
	        this.idSesion = idSesion;
	        this.token = token;
	    }

	    // Getters y setters para cada atributo
	    public String getIp() {
	        return ip;
	    }

	    public void setIp(String ip) {
	        this.ip = ip;
	    }

	    public String getIdTransaccion() {
	        return idTransaccion;
	    }

	    public void setIdTransaccion(String idTransaccion) {
	        this.idTransaccion = idTransaccion;
	    }

	    public String getIdSesion() {
	        return idSesion;
	    }

	    public void setIdSesion(String idSesion) {
	        this.idSesion = idSesion;
	    }

	    public String getToken() {
	        return token;
	    }

	    public void setToken(String token) {
	        this.token = token;
	    }
	}
