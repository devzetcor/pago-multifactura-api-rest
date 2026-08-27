package com.davivienda.sv.app.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;





@Configuration
public class J2EntornoConfig {
	
	private final static Logger LOGGER = LogManager.getLogger(J2EntornoConfig.class);

	@Value("${com.davivienda.sv.app.j2ebus.host}")
	private String hostCanales;
	
	@Value("${com.davivienda.sv.app.j2ebus.port}")
	private int portCanales;
	
	
	@Bean("Canales")
	public J2EntornoInvocacion getInvocadorCanales()
	{
		String contextoEntornoEMAIL = "J2EntornoWeb/EscuchadorHTTP";
		LOGGER.info("hostCanales:" + hostCanales);
		LOGGER.info("portCanales:" + portCanales);
		ServidorEntorno objServerEntorno = new ServidorEntorno(hostCanales, portCanales);
		J2EntornoInvocacion objEntornoInvoca = new J2EntornoInvocacion(objServerEntorno,contextoEntornoEMAIL);
		return objEntornoInvoca;
	}
	
	

}
