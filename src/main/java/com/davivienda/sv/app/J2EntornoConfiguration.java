package com.davivienda.sv.app;

import com.davivienda.sv.app.util.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;   // <-- NUEVO
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sv.com.davivienda.entorno.hsbc.client.http.J2Entorno;
import sv.com.davivienda.entorno.hsbc.client.http.J2EntornoInvocacion;
import sv.com.davivienda.entorno.hsbc.client.http.ServidorEntorno;
import sv.com.davivienda.entorno.hsbc.client.http.interconexion.Interconector;

/**
 * @author Luis Velasquez
 * @since 26 sep. 2024
 * @version 1.0
 */
@Configuration
public class J2EntornoConfiguration {

    // LOG4J 2 con SLF4J (forma correcta y recomendada)
    private static final Logger LOGGER = LoggerFactory.getLogger(J2EntornoConfiguration.class);

    @Value("${com.davivienda.sv.app.j2ebus.host}")
    private String hostBus;

    @Value("${com.davivienda.sv.app.j2ebus.port}")
    private int portBus;

    @Bean(R.J2Entorno.INVOCADOR_BUS)
    public J2EntornoInvocacion getInvocadorBus() {
        LOGGER.info("hostBus: {}", hostBus);
        LOGGER.info("portBus: {}", portBus);

        J2Entorno j2Entorno = new J2Entorno();
        Interconector interconector = new Interconector(j2Entorno);
        ServidorEntorno servidor = new ServidorEntorno(hostBus, portBus);
        J2EntornoInvocacion j2EntornoInvocacion = new J2EntornoInvocacion(j2Entorno, servidor, interconector);

        return j2EntornoInvocacion;
    }
}