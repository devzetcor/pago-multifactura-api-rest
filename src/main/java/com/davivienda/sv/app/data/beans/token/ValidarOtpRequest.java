package com.davivienda.sv.app.data.beans.token;

public class ValidarOtpRequest {
	private long niu;
	private String otp;
	private String niuString;
	private String isOperador;

	public long getNiu() {
		return niu;
	}
	public void setNiu(long niu) {
		this.niu = niu;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	
	public String getNiuString() {
		return niuString;
	}
	public void setNiuString(String niuString) {
		this.niuString = niuString;
	}
	public String getIsOperador() {
		return isOperador;
	}
	public void setIsOperador(String isOperador) {
		this.isOperador = isOperador;
	}
	@Override
	public String toString() {
		return "ValidarOtpRequest [niu=" + niu + ", otp=" + otp + "]";
	}
}
