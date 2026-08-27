package com.davivienda.sv.app.data.beans.mq;

import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.cysce.mq.client.CYSWMQClient;

@Service
public class InvocadorServiciosMQ {

	private static final Logger LOGGER = LogManager.getLogger(InvocadorServiciosMQ.class);


	@Value("${app.mq.cysce.ruta:/jboss/app/pagoMultifactura/}")
	private String jndiRutaXmlCysce;

	@SuppressWarnings("unchecked")
	public String invocarServicio(String servicio, String operacion, String xmlEnviar) {
		LOGGER.info("Ejecucion metodo invocadorServicio ");
		LOGGER.info("Servicio: " + servicio);
		LOGGER.info("Operacion: " + operacion);
		LOGGER.info("Ruta XML Config Cysce: " + jndiRutaXmlCysce);

		try {
			Map<String, String> adpwmqc = CYSWMQClient.CYSEXECUTE(servicio, operacion, xmlEnviar, 1, jndiRutaXmlCysce);
			
			if (adpwmqc != null && adpwmqc.containsKey("CYSRTA")) {
				return adpwmqc.get("CYSRTA").toString();
			}
			
		} catch (Throwable e) {
			LOGGER.error("ExecutionException: ",e);
		}
		return null;
	}
}