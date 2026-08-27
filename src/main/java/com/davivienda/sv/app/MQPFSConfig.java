package com.davivienda.sv.app;

import com.davivienda.sv.app.util.MQCliente;
import com.davivienda.sv.app.util.R;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jndi.JndiObjectFactoryBean;

@Configuration
public class MQPFSConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQPFSConfig.class);

    // Propiedades JNDI PFS
    @Value("${mq.pfs.connection-factory}")
    private String pfsConnectionFactoryJndi;

    @Value("${mq.pfs.queue-request-jndi}")
    private String pfsQueueRequestJndi;

    @Value("${mq.pfs.queue-response-jndi}")
    private String pfsQueueResponseJndi;

    @Bean(name = R.MQCliente.PFS.JMS_TEMPLATE)
    public JmsTemplate jmsTemplate() throws Exception {
        LOGGER.info("Cargando PFS JNDI ConnectionFactory: {}", pfsConnectionFactoryJndi);
        JndiObjectFactoryBean jndi = new JndiObjectFactoryBean();
        jndi.setJndiName(pfsConnectionFactoryJndi);
        jndi.setResourceRef(false);
        jndi.afterPropertiesSet();

        ConnectionFactory cf = (ConnectionFactory) jndi.getObject();
        JmsTemplate jmsTemplate = new JmsTemplate(cf);
        jmsTemplate.setSessionTransacted(true);
        jmsTemplate.setReceiveTimeout(R.MQCliente.JMSPropertie.DEFAULT_JMS_TIMEOUT);
        jmsTemplate.setExplicitQosEnabled(true);
        jmsTemplate.setTimeToLive(R.MQCliente.JMSPropertie.DEFAULT_JMS_TIMEOUT);
        return jmsTemplate;
    }

    @Bean(name = R.MQCliente.PFS.QREQUEST)
    public Queue queueRequest() throws Exception {
        LOGGER.info("Cargando Cola de Peticion PFS via JNDI: {}", pfsQueueRequestJndi);
        JndiObjectFactoryBean jndi = new JndiObjectFactoryBean();
        jndi.setJndiName(pfsQueueRequestJndi);
        jndi.setResourceRef(false);
        jndi.afterPropertiesSet();
        return (Queue) jndi.getObject();
    }

    @Bean(name = R.MQCliente.PFS.QRESPONSE)
    public Queue queueResponse() throws Exception {
        LOGGER.info("Cargando Cola de Respuesta PFS via JNDI: {}", pfsQueueResponseJndi);
        JndiObjectFactoryBean jndi = new JndiObjectFactoryBean();
        jndi.setJndiName(pfsQueueResponseJndi);
        jndi.setResourceRef(false);
        jndi.afterPropertiesSet();
        return (Queue) jndi.getObject();
    }

    @Bean(name = R.MQCliente.PFS.NAME)
    public MQCliente getMQClientePFS(@Qualifier(R.MQCliente.PFS.JMS_TEMPLATE) JmsTemplate jmsTemplate) {
        return new MQCliente(jmsTemplate);
    }
}