package com.davivienda.sv.app.data.beans.token;

public class CuentasRequest {
	private long niu;
	private String usuario;

	public long getNiu() {
		return niu;
	}

	public void setNiu(long niu) {
		this.niu = niu;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return "GenerarOtpRequest [niu=" + niu + ", usuario=" + usuario + "]";
	}

}
