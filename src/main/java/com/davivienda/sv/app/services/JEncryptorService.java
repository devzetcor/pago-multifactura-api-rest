package com.davivienda.sv.app.services;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cryptonode.jncryptor.AES256JNCryptor;
import org.cryptonode.jncryptor.CryptorException;
import org.cryptonode.jncryptor.JNCryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.davivienda.sv.app.util.TpEncrip;


@Component
public class JEncryptorService {
	
	@Value("${com.davivienda.sv.app.crypto.llaveEncriptacion}")
	private String llaveEncriptacion;
	
	JNCryptor cryptor = new AES256JNCryptor();
	private static final Logger LOGGER = LogManager.getLogger(JEncryptorService.class);


	public String getLlaveEncriptacion() {
		return this.llaveEncriptacion;
	}

	/**
	 * Metodo para encriptar el texto que se envia
	 * 
	 * @param texto
	 * @return
	 * @throws Exception
	 */
	public String encriptacion(String texto) {
		AES256JNCryptor cryptor = new AES256JNCryptor();
		try {
			byte[] txtBase = texto.getBytes();
			byte[] cipherTxt = cryptor.encryptData(txtBase, llaveEncriptacion.toCharArray());
			String encrypt = Base64.getEncoder().encodeToString(cipherTxt);

			return encrypt.replaceAll("\\+","|Plus");
		} catch (CryptorException e) {
		return	e.getMessage();
		} finally {
			cryptor = null;
		}
	}

	/**
	 * Metodo para desifrar el texto enviado encriptado
	 * 
	 * @param texto
	 * @return
	 * @throws Exception
	 */
	public String desencriptar(String texto) throws Exception {
		AES256JNCryptor cryptor = new AES256JNCryptor();
		try {
			texto = texto.trim().replaceAll(TpEncrip.MAS.getValor(), TpEncrip.MAS.getKey());
//			LOGGER.debug("Texto limpio: "+texto);
			byte[] txtBase = Base64.getDecoder().decode(texto);
			byte[] cipherTxt = cryptor.decryptData(txtBase, llaveEncriptacion.toCharArray());
			return new String(cipherTxt, StandardCharsets.UTF_8);
		} catch (CryptorException e) {
			this.LOGGER.error("Excepción tratando de desencriptar datos: " + e.getMessage(), e);
			return "";
		} finally {
			cryptor = null;
		}
	}
}
