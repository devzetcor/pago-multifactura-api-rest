package com.davivienda.sv.app.data.beans.usuario;

public class InfoUsuarioResponse {
	private long niu;
	private String correo;
	private String telefono;
	private int modoAutenticacion;
	private String profesion;
	private int edad;
	private String NIT;
	private boolean personaNatural; 
	
	public long getNiu() {
		return niu;
	}
	public void setNiu(long niu) {
		this.niu = niu;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public int getModoAutenticacion() {
		return modoAutenticacion;
	}
	public void setModoAutenticacion(int modoAutenticacion) {
		this.modoAutenticacion = modoAutenticacion;
	}
	public String getProfesion() {
		return profesion;
	}
	public void setProfesion(String profesion) {
		this.profesion = profesion;
	}
	public int getEdad() {
		return edad;
	}
	public void setEdad(int edad) {
		this.edad = edad;
	}
	public String getNIT() {
		return NIT;
	}
	public void setNIT(String nIT) {
		NIT = nIT;
	}
	public boolean isPersonaNatural() {
		return personaNatural;
	}
	public void setPersonaNatural(boolean personaNatural) {
		this.personaNatural = personaNatural;
	}
	
	@Override
	public String toString() {
		return String.format(
				"InfoUsuarioResponse [niu=%s, correo=%s, telefono=%s, modoAutenticacion=%s, profesion=%s, edad=%s, NIT=%s, personaNatural=%s]",
				niu, correo, telefono, modoAutenticacion, profesion, edad, NIT, personaNatural);
	}
}
