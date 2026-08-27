package com.davivienda.sv.app.data.beans;

public class BasicRequest {
	private int modoOperacion;

	public int getModoOperacion() {
		return modoOperacion;
	}

	public void setModoOperacion(int modoOperacion) {
		this.modoOperacion = modoOperacion;
	}

	@Override
	public String toString() {
		return "BasicRequest [modoOperacion=" + modoOperacion + "]";
	}
	
}
