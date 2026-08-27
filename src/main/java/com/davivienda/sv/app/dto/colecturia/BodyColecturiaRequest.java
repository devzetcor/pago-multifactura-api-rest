package com.davivienda.sv.app.dto.colecturia;

import com.davivienda.sv.app.dto.PeticionJ2Entorno;

public class BodyColecturiaRequest<T> {

	private com.davivienda.sv.app.dto.PeticionJ2Entorno<T> peticionEntorno;

	public BodyColecturiaRequest() {
		this.peticionEntorno = new com.davivienda.sv.app.dto.PeticionJ2Entorno<>();
	}

	public PeticionJ2Entorno<T> getPeticionEntorno() {
		return peticionEntorno;
	}

	public void setPeticionEntorno(PeticionJ2Entorno<T> peticionEntorno) {
		this.peticionEntorno = peticionEntorno;
	}

	@Override
	public String toString() {
		return "BodyColecturia [peticionEntorno=" + peticionEntorno + "]";
	}

}
