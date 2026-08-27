package com.davivienda.sv.app.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.stereotype.Component;

@Component
public class CryptoService {

	private StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
	private static final Logger LOGGER = LogManager.getLogger(CryptoService.class);

	public String encriptar(String texto, String semilla) {
		StandardPBEStringEncryptor encryptorDos = new StandardPBEStringEncryptor();
		try {
			if (!encryptorDos.isInitialized()) {
				encryptorDos.setPassword(semilla);
			}
			String encriptado = encryptorDos.encrypt(texto);
			LOGGER.debug("ENCRYPT: "+encriptado);
			return encriptado;
		} catch (Exception e) {
			LOGGER.error("Error al encriptar: " + e.getMessage(), e);
			return "";
		}
	}

	public String desencriptar(String texto, String semilla) {
		try {
			if (!this.encryptor.isInitialized()) {
				this.encryptor.setPassword(semilla);
			}
			String desencriptado = this.encryptor.decrypt(texto);
			return desencriptado;
		} catch (Exception e) {
			LOGGER.error("Error desencriptando el texto: " + e.getMessage(), e);
			return "";
		}
	}

	public String obtenerSemilla(String nombreUsuario) {
		if (nombreUsuario.length() <= 4) {
			return nombreUsuario;
		} else {
			String caracterInicio = "";
			String caracterFin = "";
			caracterInicio = nombreUsuario.substring(0, 2);
			caracterFin = nombreUsuario.substring(nombreUsuario.length() - 2, nombreUsuario.length());
			LOGGER.debug("caracterInicio::"+caracterInicio);
			LOGGER.debug("caracterFin::"+caracterFin);
			return caracterInicio + caracterFin;
		}
	}
}
