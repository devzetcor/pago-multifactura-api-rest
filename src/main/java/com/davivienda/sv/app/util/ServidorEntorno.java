package com.davivienda.sv.app.util;

public class ServidorEntorno {
	private String ip;
	private Integer puerto;
	private String contextoServlet;
	
	public ServidorEntorno(String ip, Integer puerto) {
		super();
		this.ip = ip;
		this.puerto = puerto;
	}
	private final String CONTEXTO_ENTORNO="";
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getPuerto() {
		return puerto;
	}
	@Override
	public String toString() {
		return "ServidorEntorno [ip=" + ip + ", puerto=" + puerto
				+ ", contextoServlet=" + contextoServlet + "]";
	}
	public void setPuerto(Integer puerto) {
		this.puerto = puerto;
	}
	public String getContextoServlet() {
		if(this.contextoServlet==null)
			return CONTEXTO_ENTORNO;
		return contextoServlet;
	}
	public void setContextoServlet(String contextoServlet) {
		this.contextoServlet = contextoServlet;
	}
	
	
}
