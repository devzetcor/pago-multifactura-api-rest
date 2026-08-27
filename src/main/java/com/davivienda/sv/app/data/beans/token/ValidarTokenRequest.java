package com.davivienda.sv.app.data.beans.token;

public class ValidarTokenRequest {
	private String usuario;
	private long otp;
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public long getOtp() {
		return otp;
	}
	public void setOtp(long otp) {
		this.otp = otp;
	}
	@Override
	public String toString() {
		return "ValidarTokenRequest [usuario=" + usuario + ", otp=" + otp + "]";
	}
}
